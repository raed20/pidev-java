package com.example.reclamation.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageUploadService {
    private static final String UPLOAD_DIR = "upload";

    public String uploadImage(File imageFile) {
        // Create the upload directory if it doesn't exist
        createUploadDirectory();

        try {
            // Generate a unique file name
            String fileName = imageFile.getName();

            // Construct the destination path
            Path destinationPath = Paths.get(UPLOAD_DIR, fileName);

            // Copy the file to the destination
            Files.copy(imageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // Return the relative path of the uploaded image
            return UPLOAD_DIR + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createUploadDirectory() {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
