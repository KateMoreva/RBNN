package polytech.RBNN;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RBNNController {

    @PostMapping("/process-image")
    public ResponseEntity<String> processImage() {
        return ResponseEntity.ok("wow");
    }

}
