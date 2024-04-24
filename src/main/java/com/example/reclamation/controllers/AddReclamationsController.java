package com.example.reclamation.controllers;

import com.example.reclamation.config.EmailService;
import com.example.reclamation.models.Reclamations;
import com.example.reclamation.services.ReclamationsService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddReclamationsController {
    @FXML
    private TextField objetField;
    
    @FXML
    private TextField descriptionField;

    @FXML
    private ImageView imageView;

    @FXML
    private Button uploadImageButton;

    private ReclamationsService reclamationsService;
    private ReclamationsController parentController;

    private EmailService emailService;

    private File selectedFile;

    public AddReclamationsController() {
        reclamationsService = new ReclamationsService();
        emailService = new EmailService("tesnimlassoued2@gmail.com","qqfhrfqrvyhhwkhk");
    }

    public void setParentController(ReclamationsController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void addReclamations() {
        String objet = objetField.getText();
        String description = descriptionField.getText();

        // Validate input fields
        if (objet.isEmpty() || description.isEmpty() || selectedFile == null) {
            showAlert("Please fill all fields and select an image.");
            return;
        }

        // Check minimum lengths
        if (objet.length() < 4) {
            showAlert("Object must be at least 4 characters long.");
            return;
        }
        
        if (description.length() < 8) {
            showAlert("Description must be at least 8 characters long.");
            return;
        }

        // Create a new Reclamations object with the provided data
        Reclamations newReclamations = new Reclamations(0, objet, description);

        try {
            // Call the ReclamationsService to add the reclamation to the database
            reclamationsService.addReclamation(newReclamations, selectedFile);

            // Refresh the Reclamations list
            parentController.refreshReclamationsList();

            // Close the Add Reclamations interface after adding the reclamation
            objetField.getScene().getWindow().hide();
        } catch (Exception e) {
            showAlert("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            // Call the ReclamationsService to add the reclamation to the database
            reclamationsService.addReclamation(newReclamations, selectedFile);

            // Attach the selected image file to the email
            emailService.sendEmail("tesnimlassoued2@gmail.com", objet, description, selectedFile);

            System.out.println("Email notification sent successfully.");

            // Refresh the Reclamations list
            parentController.refreshReclamationsList();

            // Close the Add Reclamations interface after adding the reclamation
            objetField.getScene().getWindow().hide();
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) objetField.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("Image selected: " + selectedFile.getName());
            // Display the selected image in the ImageView
            imageView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
