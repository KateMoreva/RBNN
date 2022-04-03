package polytech.RBNN.ml;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

import static polytech.RBNN.Utils.*;

@Component
@Slf4j
public class U2BackgroundRemover implements BackgroundRemoverNN {

    @Override
    public Path removeBackground(Path image, String fileName) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "python " + ML_FOLDER + "u2net_test.py");

        try {
            Process process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            try {
                FileUtils.cleanDirectory(new File(IMG_FOLDER));
                FileUtils.cleanDirectory(new File(TMP_RES_FOLDER));
            } catch (IOException exception) {
                log.warn("Failed to clean working folders ", exception);
            }
            if (exitCode == 0) {
                log.debug("Successfully removed bg from photo ", fileName);

                String name = fileName.split("\\.")[0] + ".png";
                return Path.of(RESULT_FOLDER + name);
            }

        } catch (IOException | InterruptedException e) {
            log.warn("Failed to remove bg, model process error ", e);
        }

        return Path.of("");
    }
}
