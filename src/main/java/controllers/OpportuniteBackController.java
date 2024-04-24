package controllers;

import entities.Opportunite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import services.OpportuniteService;
import tools.MyConnection;

import java.io.IOException;
import java.util.List;

public class OpportuniteBackController {

    @FXML
    private ListView<Opportunite> opportuniteList;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button addButton;

    private final OpportuniteService opportuniteService;

    public OpportuniteBackController() {
        // Initialize the service for managing opportunities
        MyConnection connection = new MyConnection();
        this.opportuniteService = new OpportuniteService(connection);
    }

    @FXML
    public void initialize() {
        // Set custom cell factory for the ListView
        opportuniteList.setCellFactory(new CustomCellFactory(opportuniteService,anchorPane));

        // Load initial data into ListView
        loadOpportunites();

        // Event handler for adding new opportunities
        addButton.setOnMouseClicked(event -> loadPage("/JavaFX/BackOffice/investissement/opportuniteaddback.fxml"));
    }

    private void loadOpportunites() {
        // Retrieve list of opportunities from the service
        List<Opportunite> opportunites = opportuniteService.getAllOpportunites();
        // Convert to observable list for use in the ListView
        ObservableList<Opportunite> observableOpportunites = FXCollections.observableArrayList(opportunites);
        // Set the items in the ListView
        opportuniteList.setItems(observableOpportunites);
    }

    private void loadPage(String fxmlPath) {
        // Load a new FXML page into the anchor pane
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }
    }
}
