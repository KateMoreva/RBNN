package polytech.RBNN.vault;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import polytech.RBNN.retrofit.VaultService;
import retrofit2.Call;
import retrofit2.Response;

@Component
public class VaultTransitServiceImpl implements VaultTransitService {

    private final VaultService vaultService;

    public VaultTransitServiceImpl(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @Override
    public String encryptImage(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        String base64Data = new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        Call<String> encryptCall = vaultService.encrypt(getToken(), base64Data);
        Response<String> response = encryptCall.execute();
        if (response.isSuccessful()) {
            return response.body();
        }
        throw new IOException(response.code() + " " + response.message());
    }

    @Override
    public Path decryptImage(String encryptedData) throws IOException {
        Call<String> decryptCall = vaultService.decrypt(getToken(), encryptedData);
        Response<String> response = decryptCall.execute();
        if (response.isSuccessful()) {
            return createImage(response.body());
        }
        throw new IOException(response.code() + " " + response.message());
    }

    private Path createImage(String base64Data) throws IOException {
        String data = new String(Base64.decodeBase64(base64Data), StandardCharsets.UTF_8);
        String fileName = UUID.randomUUID() + ".png";
        return Files.write(Paths.get(fileName), data.getBytes(StandardCharsets.UTF_8));
    }

    private String getToken() {
        return System.getenv("APP_ORDER_TOKEN");
    }
}
