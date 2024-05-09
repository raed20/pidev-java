package controllers;


import tools.PDFGeneratorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Reclamations;
import services.ReclamationsService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ReclamationsController {

    @FXML
    private TableView<Reclamations> reclamationTable;

    private ReclamationsService reclamationsService;

    private PDFGeneratorService pdfGeneratorService;

    public void initialize() {
        reclamationsService = new ReclamationsService();
        setupTable();
        loadReclamationsData();
        pdfGeneratorService = new PDFGeneratorService();
    }

    @FXML
    private void addReclamation() {
        try {
            // Load the "Add Book" FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/AddReclamation.fxml"));
            Parent root = loader.load();

            // Get the controller of the "Add Book" interface
            AddReclamationsController addReclamationsController = loader.getController();
            addReclamationsController.setParentController(this);

            // Create a new stage for the "Add Book" interface
            Stage addReclamationsStage = new Stage();
            addReclamationsStage.setScene(new Scene(root));
            addReclamationsStage.setTitle("Ajouter Réclamation");

            // Show the "Add Book" interface
            addReclamationsStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void updateReclamations() {
        // Get the selected book from the table
        Reclamations selectedReclamations = reclamationTable.getSelectionModel().getSelectedItem();
        if (selectedReclamations != null) {
            try {
                // Load the "Update Book" FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UpdateReclamation.fxml"));
                Parent root = loader.load();

                // Get the controller of the "Update Book" interface
                UpdateReclamationsController updateReclamationsController = loader.getController();
                updateReclamationsController.setParentController(this);
                updateReclamationsController.setReclamationsService(reclamationsService);
                updateReclamationsController.setReclamationsIdToUpdate(selectedReclamations.getId());

                // Set the book data in the UpdateBookController
                updateReclamationsController.setReclamationsData(selectedReclamations);

                // Create a new stage for the "Update Book" interface
                Stage updateReclamationsStage = new Stage();
                updateReclamationsStage.setScene(new Scene(root));
                updateReclamationsStage.setTitle("Update Reclamations");

                // Show the "Update Book" interface
                updateReclamationsStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Display an error message if no book is selected
            // (you can implement this according to your UI design)
        }
    }

    @FXML
    private void deleteReclamations() {
        Reclamations selectedReclamations = reclamationTable.getSelectionModel().getSelectedItem();
        if (selectedReclamations != null) {
            reclamationsService.deleteReclamation(selectedReclamations.getId());
            //clearFields();
            loadReclamationsData();
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

    @FXML
    public void generatePDF(ActionEvent actionEvent) {
        List<Reclamations> reclamations = reclamationTable.getItems();

        // Define the file path for the PDF
        String filePath = "reclamations_list.pdf";

        // Generate the PDF
        try {
            pdfGeneratorService.generatePDF(reclamations, filePath);
            System.out.println("PDF file generated successfully.");
        } catch (IOException e) {
            System.out.println("Failed to generate PDF file: " + e.getMessage());
        }
    }

    @FXML
    public void adminSide(ActionEvent actionEvent) {
        try {
            // Load the "Add Book" FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ReclamationsAdmin.fxml"));
            Parent root = loader.load();

            // Get the controller of the "Add Book" interface
            ReclamationsAdminController reclamationsAdminController = loader.getController();
            reclamationsAdminController.setParentController(this);

            // Create a new stage for the "Add Book" interface
            Stage reclamationsAdminStage = new Stage();
            reclamationsAdminStage.setScene(new Scene(root));
            reclamationsAdminStage.setTitle("Reclamations Admin");

            // Show the "Add Book" interface
            reclamationsAdminStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

