package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import models.StockQuote;
import services.PolygonApiService;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.List;

public class CustomCellFactoryFront {
    @FXML
    private ListView<StockQuote> listView;

    private final PolygonApiService polygonApiService;

    public CustomCellFactoryFront() {
        this.polygonApiService = new PolygonApiService() ;   }

    @FXML
    public void initialize() {
        // Set custom cell factory for the ListView
        listView.setCellFactory(param -> new ListCell<StockQuote>() {
            @Override
            protected void updateItem(StockQuote item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        // Load the FXML file
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/FrontOffice/investissement/opportuniteupdateback.fxml"));
                        AnchorPane root = loader.load();

                        // Access the elements of the custom cell
                        Label nameLabel = (Label) root.lookup("#nameLabel");
                        Label lastpriceLabel = (Label) root.lookup("#lastpriceLabel");

                        // Set values to the elements
                        nameLabel.setText(item.getName());
                        lastpriceLabel.setText(String.valueOf(item.getOpen()));

                        // Set the cell content
                        setGraphic(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        // Load and populate data
        populateListView();
    }

    private void populateListView() {
        // Fetch data from API
        List<StockQuote> stockQuotes = polygonApiService.fetchStockQuotes();

        // Add data to ListView
        listView.getItems().addAll(stockQuotes);
    }
}
