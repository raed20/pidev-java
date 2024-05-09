package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Reclamations;
import entities.Responses;
import services.ReclamationsService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ReclamationsAdminController {

    private ObservableList<Reclamations> reclamationsList = FXCollections.observableArrayList();

    @FXML
    private Button updateStatusButton;
    @FXML
    private TableView<Reclamations> reclamationTable;
    @FXML
    private Button addResponseButton;

    private ReclamationsService reclamationsService;
    private ReclamationsController parentController;

    public void initialize() {
        reclamationsService = new ReclamationsService();
        setupTable();
        loadReclamationsData();
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

        ContextMenu contextMenu = new ContextMenu();
        MenuItem showResponses = new MenuItem("Show Responses");
        showResponses.setOnAction(event -> showResponses());
        contextMenu.getItems().add(showResponses);

        reclamationTable.setContextMenu(contextMenu);
        reclamationTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(reclamationTable, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    private void showResponses() {
        Reclamations selectedReclamation = reclamationTable.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            List<Responses> responses = reclamationsService.getResponsesForReclamation(selectedReclamation.getId());
            showDialogWithResponses(responses);
        }
    }

    private void showDialogWithResponses(List<Responses> responses) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Responses");
        alert.setHeaderText("Responses for the Reclamation");

        StringBuilder content = new StringBuilder();
        for (Responses response : responses) {
            content.append("Date: ").append(response.getDate()).append("\n");
            content.append("Message: ").append(response.getMessage()).append("\n\n");
        }

        alert.setContentText(content.toString());
        alert.showAndWait();
    }

    private void loadReclamationsData() {
        reclamationTable.getItems().clear();
        List<Reclamations> reclamations = reclamationsService.getAllReclamations();
        reclamationTable.getItems().addAll(reclamations);
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
            controller.setParentController(this.parentController);

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
