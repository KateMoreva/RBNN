package polytech.RBNN.vault;

import java.io.IOException;
import java.nio.file.Path;

public interface VaultTransitService {

    String encryptPath(Path path) throws IOException;

    Path decryptPath(String encryptedPath) throws IOException;

}
