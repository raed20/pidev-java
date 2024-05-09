package controllers;
import entities.Opportunite;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import services.OpportuniteService;
import tools.MyConnection;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class InvetissementaddBackController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private TextField lastPriceField;

    @FXML
    private TextField changeRateField;

    @FXML
    private TextField marketCapField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    private OpportuniteService opportuniteService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the service with the database connection
        MyConnection myConnection = new MyConnection(); // Creating an instance of MyConnection
        opportuniteService = new OpportuniteService(myConnection); // Passing myConnection to the constructor

        // Set event handler for the "Add" button
        addButton.setOnMouseClicked(event -> handleAddButtonAction());
        cancelButton.setOnMouseClicked(event -> handleCancelButtonAction());

    }

    // This method is called when the "Add" button is clicked
    private void handleAddButtonAction() {
        // Retrieve data from text fields
        String name = nameField.getText();
        float lastPrice = Float.parseFloat(lastPriceField.getText());
        float changeRate = Float.parseFloat(changeRateField.getText());
        float marketCap = Float.parseFloat(marketCapField.getText());
        String description = descriptionField.getText();
        float price = Float.parseFloat(priceField.getText());

        // Create a new Opportunite object
        Opportunite newOpportunite = new Opportunite();
        newOpportunite.setName(name);
        newOpportunite.setLastprice(lastPrice);
        newOpportunite.setYesterdaychange(changeRate);
        newOpportunite.setMarketcap(marketCap);
        newOpportunite.setDescription(description);
        newOpportunite.setPrix(price);

        // Add the new opportunity to the database
        opportuniteService.addOpportunite(newOpportunite);
    }

    // This method is called when the "Cancel" button is clicked
    @FXML
    private void handleCancelButtonAction() {
        // You can add code here to navigate back to invetissementback.fxml if needed
    }
}

