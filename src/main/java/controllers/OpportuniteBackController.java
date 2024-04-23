package controllers;

import entities.Opportunite;
import interfaces.IOpportunite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import services.OpportuniteService;
import tools.MyConnection;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class OpportuniteBackController {

    @FXML
    private TableView<Opportunite> opportuniteTable;

    @FXML
    private TableColumn<Opportunite, Integer> idColumn;

    @FXML
    private TableColumn<Opportunite, String> descriptionColumn;

    @FXML
    private TableColumn<Opportunite, Float> prixColumn;

    @FXML
    private TableColumn<Opportunite, String> nameColumn;

    @FXML
    private TableColumn<Opportunite, Float> lastpriceColumn;

    @FXML
    private TableColumn<Opportunite, Float> yesterdaychangeColumn;

    @FXML
    private TableColumn<Opportunite, Float> marketcapColumn;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button addButton;

    private IOpportunite opportuniteService;

    public OpportuniteBackController() {
        MyConnection connection = new MyConnection();
        this.opportuniteService = new OpportuniteService(connection);
    }

    @FXML
    public void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastpriceColumn.setCellValueFactory(new PropertyValueFactory<>("lastprice"));
        yesterdaychangeColumn.setCellValueFactory(new PropertyValueFactory<>("yesterdaychange"));
        marketcapColumn.setCellValueFactory(new PropertyValueFactory<>("marketcap"));

        // Load data into TableView
        loadOpportunites();

        // Add buttons to each row for update and delete
        addButtonToTable();
        addButton.setOnMouseClicked(event -> loadPage("/JavaFX/BackOffice/investissement/opportuniteaddback.fxml"));

    }

    private void loadOpportunites() {
        List<Opportunite> opportunites = opportuniteService.getAllOpportunites();
        ObservableList<Opportunite> observableOpportunites = FXCollections.observableArrayList(opportunites);
        opportuniteTable.setItems(observableOpportunites);
    }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }
    }

    private void addButtonToTable() {
        TableColumn<Opportunite, Void> colBtn = new TableColumn<>("Actions");

        Callback<TableColumn<Opportunite, Void>, TableCell<Opportunite, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Opportunite, Void> call(final TableColumn<Opportunite, Void> param) {
                final TableCell<Opportunite, Void> cell = new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");
                    private final Button updateButton = new Button("Update");

                    {
                        deleteButton.setOnAction(event -> {
                            Opportunite opportunite = getTableView().getItems().get(getIndex());
                            deleteOpportunite(opportunite);
                        });

                        updateButton.setOnAction(event -> {
                            Opportunite opportunite = getTableView().getItems().get(getIndex());
                            updateOpportunite(opportunite);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox(updateButton, deleteButton);
                            setGraphic(buttons);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        opportuniteTable.getColumns().add(colBtn);
    }

    private void deleteOpportunite(Opportunite opportunite) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Opportunite");
        alert.setContentText("Are you sure you want to delete this opportunite?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            opportuniteService.deleteOpportunite(opportunite.getId());
            opportuniteTable.getItems().remove(opportunite);
            showAlert(Alert.AlertType.INFORMATION, "Opportunite Deleted", "Opportunite deleted successfully.");
        }
    }

    private void updateOpportunite(Opportunite opportunite) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/BackOffice/investissement/opportuniteupdateback.fxml"));
        Parent root;
        try {
            root = loader.load();
            OpportuniteupdateBackController controller = loader.getController();
            controller.setOpportunite(opportunite);
            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
