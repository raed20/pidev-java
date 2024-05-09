package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import models.StockQuote;

public class CustomCellFactoryFront implements Callback<ListView<StockQuote>, ListCell<StockQuote>> {

    private BorderPane borderPane;

    public CustomCellFactoryFront(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

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

            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/FrontOffice/investissement/listviewcollum.fxml"));
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
                    nameLabel.setText(""+item.getName());
                    openLabel.setText("" + item.getOpen());
                    highLabel.setText("" + item.getHigh());
                    lowLabel.setText("" + item.getLow());
                    closeLabel.setText("" + item.getClose());
                    volumeLabel.setText("" + item.getVolume());
                    setGraphic(listviewCollum);
                    nameLabel.setOnMouseClicked(event -> handleLabelClick(event, item.getName()));

                }
            }

            private void handleLabelClick(MouseEvent event, String selectedName) {
                navigateToInvestissementFront(selectedName);
            }
        };
    }

    private void navigateToInvestissementFront(String selectedName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/FrontOffice/investissement/investissementfront.fxml"));
            AnchorPane investissementFrontView = loader.load();
            InvestissementFrontController controller = loader.getController();
            controller.setSelectedName(selectedName);

            // Get the current scene from any node within the current scene
            Scene currentScene = borderPane.getScene();

            // Retrieve the BorderPane by its ID
            BorderPane mainBorderPane = (BorderPane) currentScene.lookup("#borderPane");

            // Replace the center of the BorderPane with the investissement front view
            mainBorderPane.setCenter(investissementFrontView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}




