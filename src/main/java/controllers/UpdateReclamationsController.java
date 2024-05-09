package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Reclamations;
import services.ReclamationsService;

public class UpdateReclamationsController {
    @FXML
    public TextField objetField;

    @FXML
    public TextField descriptionField;

    private ReclamationsController parentController;
    private ReclamationsService reclamationsService;
    private int reclamationsIdToUpdate;

    public void setParentController(ReclamationsController parentController) {
        this.parentController = parentController;
    }

    public void setReclamationsService(ReclamationsService reclamationsService) {
        this.reclamationsService = reclamationsService;
    }

    public void setReclamationsIdToUpdate(int reclamationsIdToUpdate) {
        this.reclamationsIdToUpdate = reclamationsIdToUpdate;
    }

    // Method to pre-fill input fields with reclamation data
    public void setReclamationsData(Reclamations reclamations) {
        objetField.setText(reclamations.getObjet());
        descriptionField.setText(reclamations.getDescription());
    }

    @FXML
    private void updateReclamations() {
        String objet = objetField.getText();
        String description = descriptionField.getText();

        // Validate input fields
        if (objet.isEmpty() || description.isEmpty()) {
            showAlert("Please fill all fields.");
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

        // Create a new Reclamations object with the provided data and the reclamation ID
        Reclamations updatedReclamations = new Reclamations(reclamationsIdToUpdate, objet, description);

        // Call ReclamationsService to update the reclamation in the database
        reclamationsService.updateReclamation(updatedReclamations);

        // Notify the reclamation list interface to refresh the reclamation list
        parentController.refreshReclamationsList();

        // Close the Update Reclamation interface
        objetField.getScene().getWindow().hide();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
