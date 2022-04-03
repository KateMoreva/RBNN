package polytech.RBNN.vault;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import polytech.RBNN.dto.CiphertextDto;
import polytech.RBNN.dto.PlaintextDto;
import polytech.RBNN.dto.VaultResponseDto;
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
    public String encryptPath(Path path) throws IOException {
        byte[] bytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        String base64Data = new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        Call<VaultResponseDto<CiphertextDto>> encryptCall = vaultService.encrypt(getToken(), new PlaintextDto(base64Data));
        Response<VaultResponseDto<CiphertextDto>> response = encryptCall.execute();
        if (response.isSuccessful()) {
            return response.body().getData().getCiphertext();
        }
        throw new IOException(response.code() + " " + response.message());
    }

    @Override
    public Path decryptPath(String encryptedPath) throws IOException {
        Call<VaultResponseDto<PlaintextDto>> decryptCall = vaultService.decrypt(getToken(), new CiphertextDto(encryptedPath));
        Response<VaultResponseDto<PlaintextDto>> response = decryptCall.execute();
        if (response.isSuccessful()) {
            return decodeFromBase64(response.body().getData().getPlaintext());
        }
        throw new IOException(response.code() + " " + response.message());
    }

    private Path decodeFromBase64(String base64Data) {
        String fileName = new String(Base64.decodeBase64(base64Data), StandardCharsets.UTF_8);
        return Paths.get(fileName);
    }

    private String getToken() {
        return System.getenv("APP_ORDER_TOKEN");
    }
}
