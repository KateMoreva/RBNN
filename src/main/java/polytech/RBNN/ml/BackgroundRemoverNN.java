package polytech.RBNN.ml;

import java.nio.file.Path;

public interface BackgroundRemoverNN {

    /**
     * Remove background from the image.
     * @param image - the path to the image to be processed.
     * @return the path to the image with removed background.
     */
    Path removeBackground(Path image);

}
