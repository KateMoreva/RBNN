package polytech.RBNN.vault;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class VaultTransitServiceStub implements VaultTransitService {

    @Override
    public String encryptPath(Path path) {
        return path.toString();
    }

    @Override
    public Path decryptPath(String encryptedPath) {
        return Paths.get(encryptedPath);
    }

}
