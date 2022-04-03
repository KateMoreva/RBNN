package polytech.RBNN.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytech.RBNN.dto.ImageDataDto;
import polytech.RBNN.dto.RequestDto;
import polytech.RBNN.entity.ImageData;
import polytech.RBNN.ml.BackgroundRemoverNN;
import polytech.RBNN.repository.ImageDataRepository;
import polytech.RBNN.vault.VaultTransitService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static polytech.RBNN.Utils.IMG_FOLDER;

@RestController
public class RBNNController {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;

    private final BackgroundRemoverNN backgroundRemover;
    private final VaultTransitService transitService;
    private final ImageDataRepository imageDataRepository;

    public RBNNController(BackgroundRemoverNN backgroundRemover,
                          VaultTransitService transitService,
                          ImageDataRepository imageDataRepository) {
        this.backgroundRemover = backgroundRemover;
        this.transitService = transitService;
        this.imageDataRepository = imageDataRepository;
    }

    @PostMapping("/process-image")
    public ResponseEntity<ImageDataDto> processImage(@RequestBody RequestDto requestDto) throws IOException {
        String fileName = UUID.randomUUID() + ".jpg";
        Path path = saveImage(requestDto.getImageUrl(), fileName);
        Path result = backgroundRemover.removeBackground(path, fileName);
        if (result != Path.of("")) {
            String encryptedImage = transitService.encryptPath(result);

            ImageData imageData = new ImageData();
            imageData.setData(encryptedImage);
            imageData.setTimestamp(new Timestamp(System.currentTimeMillis()));
            imageData.setUsername(requestDto.getUsername());
            imageDataRepository.save(imageData);

            return ResponseEntity.ok(new ImageDataDto(imageData.getTimestamp().getTime(), result.toString()));
        } else {
            return ResponseEntity.of(java.util.Optional.empty());
        }
    }

    @GetMapping("/get-image")
    public ResponseEntity<byte[]> getImage(@RequestParam String imageName) throws IOException {
        return getPhoto(imageName);
    }

    @GetMapping("/get-all-images")
    public ResponseEntity<List<ImageDataDto>> getAllImages(@RequestParam String username) throws IOException {
        List<ImageData> encryptedImagesOfUser = imageDataRepository.findAllByUsername(username);
        return getAllPhotos(encryptedImagesOfUser);
    }

    private static Path saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(IMG_FOLDER+destinationFile);

        byte[] b = new byte[2048];
        int length;
        int size = 0;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
            size += length;
            if (size > MAX_IMAGE_SIZE) {
                is.close();
                os.close();
                throw new IOException("Размер загружаемой картинки не должен превышать " + MAX_IMAGE_SIZE / 1024 / 1024 + " мегабайт");
            }
        }

        is.close();
        os.close();
        return Paths.get(destinationFile);
    }

    private ResponseEntity<byte[]> getPhoto(String imageUrl) throws IOException {
        Path pathToImage = Paths.get(imageUrl);
        InputStream in = Files.newInputStream(pathToImage);
        byte[] image = in.readAllBytes();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    private ResponseEntity<List<ImageDataDto>> getAllPhotos(List<ImageData> encryptedImagesOfUser) throws IOException {
        List<ImageDataDto> imagesToReturn = new ArrayList<>(encryptedImagesOfUser.size());
        for(ImageData image : encryptedImagesOfUser) {
            Path pathToDecryptedImage = transitService.decryptPath(image.getData());
            imagesToReturn.add(
                    new ImageDataDto(
                            image.getTimestamp().getTime(),
                            pathToDecryptedImage.getFileName().toString()
                    )
            );
        }
        return ResponseEntity.ok(imagesToReturn);
    }
}
