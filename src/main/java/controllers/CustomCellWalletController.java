package controllers;

import entities.Investissement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import services.InvestissementService;
import javafx.util.Callback;

import java.io.IOException;
public class CustomCellWalletController implements Callback<ListView<Investissement>, ListCell<Investissement>>{

    private final InvestissementService investissementService;
    private final AnchorPane anchorPane; // Reference to the AnchorPane

    public CustomCellWalletController(InvestissementService investissementService, AnchorPane anchorPane)
    {
        this.investissementService = investissementService;
        this.anchorPane = anchorPane; // Initialize the AnchorPane reference
    }

    public ListCell<Investissement> call(ListView<Investissement> param) {
        return new ListCell<>() {
            private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/FrontOffice/investissement/walletlistviewcollum.fxml"));

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
            private Label priceLabel;
            @FXML
            private Label chgrateLabel;
            @FXML
            private Label totalvaluelLabel;
            @FXML
            private AnchorPane listviewCollum;

            @Override
            protected void updateItem(Investissement item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    idLabel.setText(String.valueOf(item.getId()));
                    nameLabel.setText(item.getStockName());
                    priceLabel.setText(String.valueOf(item.getPrice()));
                    chgrateLabel.setText(String.valueOf(item.getChangerate()));
                    totalvaluelLabel.setText(String.valueOf(item.getTotalValue()));

                    setGraphic(listviewCollum);
                }
            }
};};}
