package polytech.RBNN.ml;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

@Component
public class BackgroundRemoverNNStub implements BackgroundRemoverNN {

    @Override
    public Path removeBackground(Path image) {
        return image;
    }

}
