package com.example.reclamation.controllers;

import com.example.reclamation.models.Reclamations;
import com.example.reclamation.services.ReclamationsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ReclamationsAdminController {

    private ObservableList<Reclamations> reclamationsList = FXCollections.observableArrayList();
    @FXML
    public Button updateStatusButton;
    @FXML
    private TableView<Reclamations> reclamationTable;
    
    @FXML
    private Button addResponseButton;

    private Reclamations selectedReclamation;

    private ReclamationsService reclamationsService;
    private ReclamationsController parentController;

    public void initialize() {
        reclamationsService = new ReclamationsService();
        setupTable();
        loadReclamationsData();
    }

    @FXML
    private void updateStatus() {
        Reclamations selectedReclamation = reclamationTable.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            // Toggle the status
            boolean newStatus = !selectedReclamation.isEtat();
            // Update the status in the database
            reclamationsService.updateReclamationEtat(selectedReclamation.getId(), newStatus);
            // Update the status in the TableView
            selectedReclamation.setEtat(newStatus);
            reclamationTable.refresh(); // Refresh the TableView to reflect the changes
        }
    }

    private void setupTable() {
        TableColumn<Reclamations, String> objetColumn = new TableColumn<>("Objet");
        objetColumn.setCellValueFactory(new PropertyValueFactory<>("objet"));

        TableColumn<Reclamations, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Reclamations, Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_reclamation"));

        TableColumn<Reclamations, Boolean> etatColumn = new TableColumn<>("Etat");
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));
        etatColumn.setCellFactory(column -> new TableCell<Reclamations, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item ? "Processed" : "Not Processed");
                }
            }
        });

         reclamationTable.getColumns().addAll(objetColumn, descriptionColumn, dateColumn, etatColumn);
    }

    private void loadReclamationsData() {
        reclamationTable.getItems().clear();
        List<Reclamations> reclamations = reclamationsService.getAllReclamations();
        reclamationTable.getItems().addAll(reclamations);
    }

    public void refreshReclamationsList() {
        // Implement logic to refresh the book list (e.g., reload data from the database)
        loadReclamationsData(); // Call the method responsible for loading book data into the table
    }

    public void setParentController(ReclamationsController parentController) {
        this.parentController = parentController;
    }

    @FXML
    void addResponse(ActionEvent event) {
        Reclamations selectedReclamation = reclamationTable.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            openResponseForm(selectedReclamation);
        }
    }

    private void openResponseForm(Reclamations reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ResponseForm.fxml"));
            AnchorPane root = loader.load();

            ResponseFormController controller = loader.getController();
            controller.setSelectedReclamation(reclamation);
            controller.setParentController(parentController); // Pass the parent controller

            Stage responseFormStage = new Stage();
            responseFormStage.setTitle("Add Response");
            responseFormStage.initModality(Modality.APPLICATION_MODAL);
            responseFormStage.setScene(new Scene(root));
            responseFormStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


