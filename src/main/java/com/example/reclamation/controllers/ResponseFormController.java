package com.example.reclamation.controllers;

import com.example.reclamation.models.Reclamations;
import com.example.reclamation.services.ReclamationsService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ResponseFormController {
    @FXML
    private TextArea responseTextArea;

    private Reclamations selectedReclamation;

    private ReclamationsService reclamationsService;

    private ReclamationsController parentController;

    public void initialize() {
        reclamationsService = new ReclamationsService();
    }

    public void setSelectedReclamation(Reclamations reclamation) {
        this.selectedReclamation = reclamation;
        // You can use the selected reclamation to populate fields or perform other actions
    }

    @FXML
    void saveResponse(ActionEvent event) {
        String responseText = responseTextArea.getText();
        
        // Call the service layer function
        reclamationsService.saveResponseAndUpdateEtat(selectedReclamation, responseText);
        
        // Close the form
        ((Stage) responseTextArea.getScene().getWindow()).close();
        
        // Refresh the parent controller's reclamations list
        if (parentController != null) {
            parentController.refreshReclamationsList();
        }
    }

    public void setParentController(ReclamationsController reclamationsController) {
        this.parentController = reclamationsController;
    }
}

