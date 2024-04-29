package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import models.StockQuote;

import java.io.IOException;

public class CustomCellFactoryFront implements Callback<ListView<StockQuote>, ListCell<StockQuote>> {

    @Override
    public ListCell<StockQuote> call(ListView<StockQuote> listView) {
        return new ListCell<>() {
            @FXML
            private Label nameLabel;
            @FXML
            private Label openLabel;
            @FXML
            private Label highLabel;
            @FXML
            private Label lowLabel;
            @FXML
            private Label closeLabel;
            @FXML
            private Label volumeLabel;
            @FXML
            private AnchorPane listviewCollum;

            private FXMLLoader loader;

            {
                loader = new FXMLLoader(getClass().getResource("/JavaFX/FrontOffice/investissement/listviewcollum.fxml"));
                loader.setController(this);
                try {
                    listviewCollum = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void updateItem(StockQuote item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    nameLabel.setText(item.getName());
                    openLabel.setText("" + item.getOpen());
                    highLabel.setText("" + item.getHigh());
                    lowLabel.setText("" + item.getLow());
                    closeLabel.setText("" + item.getClose());
                    volumeLabel.setText("" + item.getVolume());
                    setGraphic(listviewCollum);
                }
            }
        };
    }
}
