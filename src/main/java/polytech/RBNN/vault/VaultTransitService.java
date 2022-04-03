package polytech.RBNN.vault;

import java.io.IOException;
import java.nio.file.Path;

public interface VaultTransitService {

    String encryptImage(Path path) throws IOException;

    Path decryptImage(String encryptedData) throws IOException;

}
