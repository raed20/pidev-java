package controllers;
import entities.Opportunite;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import services.OpportuniteService;
import tools.MyConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class OpportuniteaddBackController implements Initializable {


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
    private Label nameValidationLabel;

    @FXML
    private Label lastPriceValidationLabel;

    @FXML
    private Label changeRateValidationLabel;

    @FXML
    private Label marketCapValidationLabel;

    @FXML
    private Label descriptionValidationLabel;

    @FXML
    private Label priceValidationLabel;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    private OpportuniteService opportuniteService;

    private boolean validationRequired = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the service with the database connection
        MyConnection myConnection = new MyConnection(); // Creating an instance of MyConnection
        opportuniteService = new OpportuniteService(myConnection); // Passing myConnection to the constructor
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validateName();
        });
        lastPriceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validateLastPrice();
        });
        changeRateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validateChangeRate();
        });
        marketCapField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validateMarketCap();
        });
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validateDescription();
        });
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validatePrice();
        });
        // Set event handler for the "Add" button
        addButton.setOnAction(event -> handleAddButtonAction());
        cancelButton.setOnMouseClicked(event -> handleCancelButtonAction());

    }


    // This method is called when the "Add" button is clicked

    private void validateName() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            nameValidationLabel.setText("Name is required.");
        } else if (!name.matches("[a-zA-Z0-9]+")) {
            nameValidationLabel.setText("Name can only contain letters and numbers.");
        } else {
            nameValidationLabel.setText("");
        }
    }


    // Method to validate the Last Price field
    private void validateLastPrice() {
        if (!isValidFloat(lastPriceField.getText())) {
            lastPriceValidationLabel.setText("Last Price must be a valid number.");
        } else {
            lastPriceValidationLabel.setText("");
        }
    }

    // Method to validate the Change Rate field
    private void validateChangeRate() {
        if (!isValidFloat(changeRateField.getText())) {
            changeRateValidationLabel.setText("Change Rate must be a valid number.");
        } else {
            changeRateValidationLabel.setText("");
        }
    }

    // Method to validate the Market Cap field
    private void validateMarketCap() {
        if (!isValidFloat(marketCapField.getText())) {
            marketCapValidationLabel.setText("Market Cap must be a valid number.");
        } else {
            marketCapValidationLabel.setText("");
        }
    }

    // Method to validate the Description field
    private void validateDescription() {
        String description = descriptionField.getText();
        if (description.isEmpty()) {
            descriptionValidationLabel.setText("Description is required.");
        } else if (!description.matches("[a-zA-Z0-9\\s]+")) {
            descriptionValidationLabel.setText("Description can only contain letters and numbers.");
        } else {
            descriptionValidationLabel.setText("");
        }
    }


    // Method to validate the Price field
    private void validatePrice() {
        if (!isValidFloat(priceField.getText())) {
            priceValidationLabel.setText("Price must be a valid number.");
        } else {
            priceValidationLabel.setText("");
        }
    }

    // Method to validate if a string can be parsed as a float
    private boolean isValidFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Method to perform validation before adding the opportunity
    private boolean validateInputs() {
        validationRequired = true;
        validateName();
        validateLastPrice();
        validateChangeRate();
        validateMarketCap();
        validateDescription();
        validatePrice();

        // Check if any validation message is displayed
        boolean isValid = nameValidationLabel.getText().isEmpty() &&
                lastPriceValidationLabel.getText().isEmpty() &&
                changeRateValidationLabel.getText().isEmpty() &&
                marketCapValidationLabel.getText().isEmpty() &&
                descriptionValidationLabel.getText().isEmpty() &&
                priceValidationLabel.getText().isEmpty();

        // Reset validationRequired flag
        validationRequired = false;

        return isValid;
    }

    private void handleAddButtonAction() {
        if (validateInputs()) {
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
            loadPage("/JavaFX/BackOffice/investissement/opportuniteback.fxml");
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        loadPage("/JavaFX/BackOffice/investissement/opportuniteback.fxml");
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
}
