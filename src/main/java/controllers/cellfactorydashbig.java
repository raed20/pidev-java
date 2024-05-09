package controllers;

import entities.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import services.ProductService;

import java.io.IOException;

public class cellfactorydashbig implements Callback<ListView<Product>, ListCell<Product>> {

    private final ProductService productService;
    private final AnchorPane anchorPane; // Reference to the AnchorPane

    public cellfactorydashbig(ProductService productService, AnchorPane anchorPane) {
        this.productService = productService;
        this.anchorPane = anchorPane; // Initialize the AnchorPane reference
    }

    @Override
    public ListCell<Product> call(ListView<Product> param) {
        return new ListCell<>() {
            private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/BackOffice/investissement/recentrequestlistviewcollum.fxml"));

            {
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @FXML
            private Label nameLabel;

            @FXML
            private Label descriptionLabel;

            @FXML
            private Label dateLabel;

            @FXML
            private Label statusLabel;

            @FXML
            private AnchorPane listviewCollum;

            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    nameLabel.setText(item.getName());
                    descriptionLabel.setText(item.getDescription());
                    dateLabel.setText(String.valueOf(item.getPrice()));
                    statusLabel.setText(String.valueOf(item.getDiscount()));
                    setGraphic(listviewCollum);
                }
            }
        };
    }
}
