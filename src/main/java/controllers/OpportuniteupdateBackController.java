package controllers;

import entities.Opportunite;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import services.OpportuniteService;
import tools.MyConnection;

import java.io.IOException;

public class OpportuniteupdateBackController {

    @FXML
    private AnchorPane anchorPane;

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
    private Button updateButton;

    @FXML
    private Button cancelButton;

    private OpportuniteService opportuniteService;
    private boolean validationRequired = false;
    private Opportunite opportuniteToUpdate;

    public void initialize() {
        MyConnection myConnection = new MyConnection();
        opportuniteService = new OpportuniteService(myConnection);

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

        updateButton.setOnAction(event -> handleUpdateButtonAction());
        cancelButton.setOnAction(event -> handleCancelButtonAction());
    }

    private void validateName() {
        if (nameField.getText().isEmpty()) {
            nameValidationLabel.setText("Name is required.");
        } else if (!nameField.getText().matches("[a-zA-Z0-9]+")) {
            nameValidationLabel.setText("Name can only contain letters and numbers.");
        } else {
            nameValidationLabel.setText("");
        }
    }

    private void validateLastPrice() {
        if (!isValidFloat(lastPriceField.getText())) {
            lastPriceValidationLabel.setText("Last Price must be a valid number.");
        } else {
            lastPriceValidationLabel.setText("");
        }
    }

    private void validateChangeRate() {
        if (!isValidFloat(changeRateField.getText())) {
            changeRateValidationLabel.setText("Change Rate must be a valid number.");
        } else {
            changeRateValidationLabel.setText("");
        }
    }

    private void validateMarketCap() {
        if (!isValidFloat(marketCapField.getText())) {
            marketCapValidationLabel.setText("Market Cap must be a valid number.");
        } else {
            marketCapValidationLabel.setText("");
        }
    }

    private void validateDescription() {
        String description = descriptionField.getText();
        if (description.isEmpty()) {
            descriptionValidationLabel.setText("Description is required.");
        } else if (!description.matches("[a-zA-Z0-9\\s]+")) {
            descriptionValidationLabel.setText("Description can only contain letters, numbers, and spaces.");
        } else {
            descriptionValidationLabel.setText("");
        }
    }

    private void validatePrice() {
        if (!isValidFloat(priceField.getText())) {
            priceValidationLabel.setText("Price must be a valid number.");
        } else {
            priceValidationLabel.setText("");
        }
    }

    private boolean isValidFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateInputs() {
        validationRequired = true;
        validateName();
        validateLastPrice();
        validateChangeRate();
        validateMarketCap();
        validateDescription();
        validatePrice();

        return nameValidationLabel.getText().isEmpty() &&
                lastPriceValidationLabel.getText().isEmpty() &&
                changeRateValidationLabel.getText().isEmpty() &&
                marketCapValidationLabel.getText().isEmpty() &&
                descriptionValidationLabel.getText().isEmpty() &&
                priceValidationLabel.getText().isEmpty();
    }
    public void setOpportunite(Opportunite opportunite) {
        this.opportuniteToUpdate = opportunite;

        // Pre-populate fields with opportunity data
        nameField.setText(opportunite.getName());
        lastPriceField.setText(String.valueOf(opportunite.getLastprice()));
        changeRateField.setText(String.valueOf(opportunite.getYesterdaychange()));
        marketCapField.setText(String.valueOf(opportunite.getMarketcap()));
        descriptionField.setText(opportunite.getDescription());
        priceField.setText(String.valueOf(opportunite.getPrix()));
    }
    private void handleUpdateButtonAction() {
        if (validateInputs()) {
            // Retrieve data from text fields
            String name = nameField.getText();
            float lastPrice = Float.parseFloat(lastPriceField.getText());
            float changeRate = Float.parseFloat(changeRateField.getText());
            float marketCap = Float.parseFloat(marketCapField.getText());
            String description = descriptionField.getText();
            float price = Float.parseFloat(priceField.getText());

            // Create a new Opportunite object
            Opportunite updatedOpportunite = new Opportunite();
            updatedOpportunite.setName(name);
            updatedOpportunite.setLastprice(lastPrice);
            updatedOpportunite.setYesterdaychange(changeRate);
            updatedOpportunite.setMarketcap(marketCap);
            updatedOpportunite.setDescription(description);
            updatedOpportunite.setPrix(price);

            // Perform the update operation
            opportuniteService.updateOpportunite(updatedOpportunite);
            // You can handle the success case here, such as showing a success message
            System.out.println("Opportunite updated successfully.");
            loadPage("/JavaFX/BackOffice/investissement/opportuniteback.fxml");
        }
    }

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
