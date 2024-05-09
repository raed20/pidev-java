package controllers;

import entities.Investissement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import javafx.scene.layout.AnchorPane;
import services.InvestissementService;
import services.PolygonApiService;
import services.StripeService;
import models.StockQuote;

import java.io.IOException;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;
import tools.MyConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class PaymentController {

    @FXML
    private TextField amountField;

    @FXML
    private TextField cardNumberField;

    @FXML
    private DatePicker cardDateField;

    @FXML
    private TextField cvcField;

    @FXML
    private TextField cardholderNameField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField cityField;
    @FXML
    private Label amountLabel;

    @FXML
    private Label cardNumberLabel;

    @FXML
    private Label cardDateLabel;

    @FXML
    private Label cvcLabel;

    @FXML
    private Label cardholderNameLabel;

    @FXML
    private  Label countryLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label postalCodeLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Button payButton;
    @FXML
    private Button cancelButton;

    @FXML
    private Label selectedNameLabel;
    @FXML
    private AnchorPane anchorPane;

    MyConnection myConnection = new MyConnection(); // Creating an instance of MyConnection
    InvestissementService investissement = new InvestissementService(myConnection);

    private final StripeService stripeService;
    private final PolygonApiService polygonApiService;
    private final CountryController countryController;

    public PaymentController() {
        this.stripeService = new StripeService("sk_test_51Oo6YNBLhdhPWlxZDdaZpVmnXQ9zlSfLraLx3jxcIaNJJvz5OHQcp2mBARahWN7hXyN3HbqHvk14yFVJppB6lHPc00LWqUxvap");
        this.polygonApiService = new PolygonApiService();
        this.countryController = new CountryController();
    }

    public void setSelectedName(String selectedName) {
        selectedNameLabel.setText(selectedName);
    }

    @FXML
    private void initialize() throws IOException {
     /*   TextFields.bindAutoCompletion(countryField, (inputText) -> {
            try {
                return countryController.getSuggestions(String.valueOf(inputText));
            } catch (IOException e) {
                e.printStackTrace(); // Handle exception appropriately
                return Collections.emptyList(); // Return an empty list in case of error
            }
        });*/

        payButton.setOnAction(event -> handlePayment());
        payButton.setOnMouseClicked(event -> handleAddButtonAction());
        cancelButton.setOnMouseClicked(event -> handleCancelButtonAction());


    }

    @FXML
    private void handleAddButtonAction() {
        String productName = selectedNameLabel.getText();
        StockQuote stockQuote = polygonApiService.fetchStockQuoteBySymbol(productName);
        if (!validateAmount() || !validateCardNumber() || !validateCardDate() || !validateCVC() ||
                !validateCardholderName() || !validateAddress() || !validatePostalCode() || !validateCity() || !validateCountry()) {
            return; // If any validation fails, stop processing further
        }
        Investissement newinv = new Investissement();
        double price = stockQuote.getClose();
        int quantity = Integer.parseInt(amountField.getText());
        int amountInCents = (int) (price * quantity); // Converting to cents
        float chgrate = (float) (((stockQuote.getClose()- stockQuote.getOpen())/ stockQuote.getOpen())*100);
        newinv.setChangerate(chgrate);
        newinv.setOpport(1);
        newinv.setMontant((long) quantity);
        newinv.setPrice((float) price);
        newinv.setStockName(productName);
        newinv.setTotalValue((float) amountInCents);
        investissement.addInvestissement(newinv);
        loadPage("/Javafx/FrontOffice/investissement/Wallet.fxml");

    }
    @FXML
    private void handleCancelButtonAction() {
        navigateToInvestissementFront(selectedNameLabel.getText());
    }


    @FXML
    private void handlePayment() {
        String productName = selectedNameLabel.getText();
        StockQuote stockQuote = polygonApiService.fetchStockQuoteBySymbol(productName);
        if (!validateAmount() || !validateCardNumber() || !validateCardDate() || !validateCVC() ||
                !validateCardholderName() || !validateAddress() || !validatePostalCode() || !validateCity() || !validateCountry()) {
            return; // If any validation fails, stop processing further
        }

        double price = stockQuote.getClose();
        int quantity = Integer.parseInt(amountField.getText());
        int amountInCents = (int) (price * quantity); // Converting to cents

        long cardNumber = Long.parseLong(cardNumberField.getText());
        LocalDate cardDate = cardDateField.getValue();
        int cvc = Integer.parseInt(cvcField.getText());
        String cardholderName = cardholderNameField.getText();
        String country = countryField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String city = cityField.getText();


        try {
            // Validate country input


            Session session = stripeService.createPaymentSession(
                    productName,
                    amountInCents,
                    quantity,
                    "https://dashboard.stripe.com/test/logs?showIP=false", // Replace with your success URL
                    "https://dashboard.stripe.com/test/logs?showIP=false" // Replace with your cancel URL
            );
            System.out.println("Session created");
            // Redirect user to Stripe checkout page using session.getId()
        } catch (StripeException e) {
            String errorMessage = "Error creating payment session: " + e.getMessage();
            // Handle the error
            System.out.println("Error creating payment session: " + e.getMessage());
        }
    }
    private boolean validateAmount() {
        String amountText = amountField.getText().trim(); // Trim whitespace
        if (amountText.isEmpty()) {
            amountLabel.setText("Amount is required.");
            return false;
        }
        try {
            int amount = Integer.parseInt(amountText);
            if (amount <= 0) {
                amountLabel.setText("Amount must be greater than zero.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            amountLabel.setText("Invalid amount format.");
            return false;
        }
    }
    private boolean validateCardNumber() {
        String cardNumber = cardNumberField.getText().replaceAll("\\s+", ""); // Remove whitespace
        if (!cardNumber.matches("[0-9]+") || cardNumber.length() < 12 || cardNumber.length() > 19) {
            cardNumberLabel.setText("Please enter a valid card number.");
            return false;
        }

        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            alternate = !alternate;
        }

        if (sum % 10 != 0) {
            cardNumberLabel.setText("Invalid card number.");
            return false;
        }

        return true;
    }
    private boolean validateCardDate() {
        LocalDate cardDate = cardDateField.getValue();
        if (cardDate == null) {
            cardDateLabel.setText("Card expiration date is required.");
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        if (cardDate.isBefore(currentDate) || cardDate.isEqual(currentDate)) {
            cardDateLabel.setText("Card expired");
            return false;
        }
        // Implement card date validation here...
        return true;
    }

    private boolean validateCVC() {
        String cvcText = cvcField.getText();
        if (cvcText.isEmpty()) {
            cvcLabel.setText("CVC is required.");
            return false;
        }
        if (!cvcText.matches("\\d{3,4}")) {
            cvcLabel.setText("Invalid CVC format.");
            return false;
        }
        return true;
    }

    private boolean validateCardholderName() {
        String cardholderName = cardholderNameField.getText();
        if (cardholderName.isEmpty()) {
            cardholderNameLabel.setText("Cardholder name is required.");
            return false;
        }
        // No specific validation required for cardholder name
        return true;
    }

    private boolean validateAddress() {
        String address = addressField.getText();
        if (address.isEmpty()) {
            addressLabel.setText("Address is required.");
            return false;
        }
        // No specific validation required for address
        return true;
    }

    private boolean validateCountry() {
        String country = countryField.getText();
        if (country.isEmpty()) {
            countryLabel.setText("Country is required.");
            return false;
        }
      /* if (!validateCountry(country)) {
            countryLabel.setText("Invalid country, city, or postal code combination.");
            return false;
        }*/
        return true;
    }

    private boolean validateCity() {
        String city = cityField.getText();
        if (city.isEmpty()) {
            cityLabel.setText("City is required.");
            return false;
        }
       /* if (!validateCity(city)) {
            cityLabel.setText("Invalid city.");
            return false;
        }*/
        return true;
    }

    private boolean validatePostalCode() {
        String postalCode = postalCodeField.getText();
        if (postalCode.isEmpty()) {
            postalCodeLabel.setText("Postal code is required.");
            return false;
        }
      /*  if (!validatePostalCode( postalCode)) {
            postalCodeLabel.setText("Invalid country, city, or postal code combination.");
            return false;
        }*/
        return true;
    }

        private boolean validateCountry(String country) {
            try {
                TimeUnit.SECONDS.sleep(1); // Introduce a delay of 1 second
                String url = "http://api.geonames.org/searchJSON?country=" + country + "&featureClass=P&orderby=relevance&type=json&username=demo";
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                System.out.println("Country Validation Response Code: " + responseCode); // Debugging statement

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray geonames = jsonResponse.getJSONArray("geonames");
                    boolean isValid = !geonames.isEmpty();
                    System.out.println("Country Validation Result: " + isValid); // Debugging statement
                    return isValid;
                } else {
                    // Handle HTTP error
                    System.out.println("Country Validation HTTP Error: " + responseCode); // Debugging statement
                    return false;
                }
            } catch (Exception e) {
                // Handle exception
                e.printStackTrace(); // Print stack trace for debugging
                return false;
            }
        }

    private boolean validateCity(String city) {
        try {
            TimeUnit.SECONDS.sleep(1); // Introduce a delay of 1 second
            String url = "http://api.geonames.org/searchJSON?name=" + city + "&featureClass=P&orderby=relevance&type=json&username=demo";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            System.out.println("City Validation Response Code: " + responseCode); // Debugging statement

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                System.out.println(jsonResponse);

                JSONArray geonames = jsonResponse.getJSONArray("geonames");
                boolean isValid = !geonames.isEmpty();
                System.out.println("City Validation Result: " + isValid); // Debugging statement
                return isValid;
            } else {
                // Handle HTTP error
                System.out.println("City Validation HTTP Error: " + responseCode); // Debugging statement
                return false;
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace(); // Print stack trace for debugging
            return false;
        }
    }
/*
    private boolean validatePostalCode(String postalCode) {
        try {
            TimeUnit.SECONDS.sleep(1); // Introduce a delay of 1 second
            String url = "http://api.geonames.org/searchJSON?postalcode=" + postalCode + "&featureClass=P&orderby=relevance&type=json&username=demo";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            System.out.println("Postal Code Validation Response Code: " + responseCode); // Debugging statement

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray geonames = jsonResponse.getJSONArray("geonames");
                boolean isValid = !geonames.isEmpty();
                System.out.println("Postal Code Validation Result: " + isValid); // Debugging statement
                return isValid;
            } else {
                // Handle HTTP error
                System.out.println("Postal Code Validation HTTP Error: " + responseCode); // Debugging statement
                return false;
            }
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace(); // Print stack trace for debugging
            return false;
        }
    }*/

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void navigateToInvestissementFront(String selectedName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/FrontOffice/investissement/investissementfront.fxml"));
            AnchorPane investissementFrontView = loader.load();
            InvestissementFrontController controller = loader.getController();
            controller.setSelectedName(selectedName);
            anchorPane.getChildren().setAll(investissementFrontView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
