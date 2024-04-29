package controllers;
import controllers.CustomCellFactory;
import entities.Opportunite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private AnchorPane listviewPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button addButton;

    @FXML
    private Pagination pagination;

    private ObservableList<Opportunite> allOpportunites;

    private final OpportuniteService opportuniteService;
    private static final int ITEMS_PER_PAGE = 6;

    public OpportuniteBackController() {
        // Initialize the service for managing opportunities
        MyConnection connection = new MyConnection();
        this.opportuniteService = new OpportuniteService(connection);
    }

    @FXML
    public void initialize() {
        // Set custom cell factory for the ListView
        opportuniteList.setCellFactory(new CustomCellFactory(opportuniteService, anchorPane));

        // Initialize allOpportunites and load initial data into ListView
        allOpportunites = FXCollections.observableArrayList();
        loadOpportunites();

        // Set up pagination
        pagination.setPageCount(calculatePageCount());
        pagination.setPageFactory(this::createPage);

        // Event handler for adding new opportunities
        addButton.setOnAction(event -> loadPage("/JavaFX/BackOffice/investissement/opportuniteaddback.fxml"));
    }

    private void loadOpportunites() {
        List<Opportunite> opportunites = opportuniteService.getAllOpportunites();
        allOpportunites.addAll(opportunites);
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

    private int calculatePageCount() {
        int pageCount = allOpportunites.size() / ITEMS_PER_PAGE;
        if (allOpportunites.size() % ITEMS_PER_PAGE != 0) {
            pageCount++;
        }
        return pageCount;
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allOpportunites.size());

        ListView<Opportunite> pageListView = new ListView<>();
        pageListView.setItems(FXCollections.observableArrayList(allOpportunites.subList(fromIndex, toIndex)));
        pageListView.setCellFactory(opportuniteList.getCellFactory());

        // Replace the existing ListView with the new ListView
        listviewPane.getChildren().setAll(pageListView);

        return pageListView;
    }



}
