package polytech.RBNN.vault;

import java.nio.file.Path;

public interface VaultTransitService {

    String encryptImage(Path path);

    Path decryptImage(String encryptedData);

}
