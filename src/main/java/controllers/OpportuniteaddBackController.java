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
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import services.OpportuniteService;
import tools.MyConnection;

import java.io.IOException;
import java.net.URL;
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

    @FXML
    private VBox card;

    private OpportuniteService opportuniteService;

    private boolean validationRequired = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MyConnection myConnection = new MyConnection();
        opportuniteService = new OpportuniteService(myConnection);
        applyBoxShadow();
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

        addButton.setOnAction(event -> handleAddButtonAction());
        cancelButton.setOnMouseClicked(event -> handleCancelButtonAction());
    }

    private void applyBoxShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(30);
        dropShadow.setHeight(30);
        dropShadow.setRadius(15);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setSpread(0.5);
        dropShadow.setColor(javafx.scene.paint.Color.BLACK);

        card.setEffect(dropShadow);
    }

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
            descriptionValidationLabel.setText("Description can only contain letters and numbers.");
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

        boolean isValid = nameValidationLabel.getText().isEmpty() &&
                lastPriceValidationLabel.getText().isEmpty() &&
                changeRateValidationLabel.getText().isEmpty() &&
                marketCapValidationLabel.getText().isEmpty() &&
                descriptionValidationLabel.getText().isEmpty() &&
                priceValidationLabel.getText().isEmpty();

        validationRequired = false;

        return isValid;
    }

    private void handleAddButtonAction() {
        if (validateInputs()) {
            String name = nameField.getText();
            if (opportuniteService.opportuniteExists(name)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplicate Opportunity");
                alert.setHeaderText(null);
                alert.setContentText("An opportunity with the same name already exists.");
                alert.showAndWait();
            } else {
                float lastPrice = Float.parseFloat(lastPriceField.getText());
                float changeRate = Float.parseFloat(changeRateField.getText());
                float marketCap = Float.parseFloat(marketCapField.getText());
                String description = descriptionField.getText();
                float price = Float.parseFloat(priceField.getText());

                Opportunite newOpportunite = new Opportunite();
                newOpportunite.setName(name);
                newOpportunite.setLastprice(lastPrice);
                newOpportunite.setYesterdaychange(changeRate);
                newOpportunite.setMarketcap(marketCap);
                newOpportunite.setDescription(description);
                newOpportunite.setPrix(price);

                opportuniteService.addOpportunite(newOpportunite);
                loadPage("/JavaFX/BackOffice/investissement/opportuniteback.fxml");
            }
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
        }
    }
}
