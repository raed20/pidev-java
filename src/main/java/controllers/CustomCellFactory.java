package controllers;

import entities.Opportunite;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import services.OpportuniteService;
import javafx.util.Callback;

import java.io.IOException;

public class CustomCellFactory implements Callback<ListView<Opportunite>, ListCell<Opportunite>> {

    private final OpportuniteService opportuniteService;
    private final AnchorPane anchorPane; // Reference to the AnchorPane

    public CustomCellFactory(OpportuniteService opportuniteService, AnchorPane anchorPane) {
        this.opportuniteService = opportuniteService;
        this.anchorPane = anchorPane; // Initialize the AnchorPane reference
    }

    @Override
    public ListCell<Opportunite> call(ListView<Opportunite> param) {
        return new ListCell<>() {
            private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/BackOffice/investissement/listviewcollum.fxml"));

            {
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @FXML
            private Label idLabel;

            @FXML
            private Label nameLabel;

            @FXML
            private Label descriptionLabel;

            @FXML
            private Label lastpriceLabel;

            @FXML
            private Label yesterdaychangeLabel;

            @FXML
            private Label marketcapLabel;

            @FXML
            private Button editButton;

            @FXML
            private Button deleteButton;

            @FXML
            private AnchorPane listviewCollum;

            @Override
            protected void updateItem(Opportunite item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    idLabel.setText(String.valueOf(item.getId()));
                    nameLabel.setText(item.getName());
                    descriptionLabel.setText(item.getDescription());
                    lastpriceLabel.setText(String.valueOf(item.getLastprice()));
                    yesterdaychangeLabel.setText(String.valueOf(item.getYesterdaychange()));
                    marketcapLabel.setText(String.valueOf(item.getMarketcap()));

                    editButton.setOnAction(event -> handleEdit(item));
                    deleteButton.setOnAction(event -> handleDelete(item));

                    setGraphic(listviewCollum);
                }
            }

            private void handleEdit(Opportunite item) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/BackOffice/investissement/opportuniteupdateback.fxml"));
                    AnchorPane editView = loader.load();

                    // Set the controller for the loaded FXML file
                    OpportuniteupdateBackController controller = loader.getController();
                    controller.setOpportunite(item);
                    controller.setOpportuniteService(opportuniteService);

                    // Show the edit view
                    anchorPane.getChildren().setAll(editView); // Use the AnchorPane reference
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void handleDelete(Opportunite item) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Delete Opportunite");
                alert.setContentText("Are you sure you want to delete this opportunite?");

                alert.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == ButtonType.OK) {
                        opportuniteService.deleteOpportunite(item.getId());
                        // Refresh the list view after deletion
                        ListView<Opportunite> listView = param;
                        listView.getItems().remove(item);
                    }
                });
            }
        };
    }
}
