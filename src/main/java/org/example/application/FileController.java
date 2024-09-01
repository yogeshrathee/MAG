package org.example.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {

    String filePath="jar/MagicalArena.jar";
    @PostMapping("/execute")
    public ResponseEntity<String> executeFile() {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File not found: " + filePath);
            }

            String command;
            if (filePath.endsWith(".jar")) {
                command = "java -jar " + filePath;
            }
             else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported file type: " + filePath);
            }

            // Execute the command
            ProcessBuilder pb = new ProcessBuilder(command.split(" "));
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();

            return ResponseEntity.ok("File executed successfully: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File execution failed: " + filePath);
        }
    }
}
