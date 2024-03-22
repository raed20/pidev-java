package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import services.StripeService;
public class PaymentController {
    @FXML
    private TextField productNameField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField successUrlField;

    @FXML
    private TextField cancelUrlField;

    @FXML
    private Text resultText;

    private StripeService stripeService;

    public PaymentController() {
        // Initialize your StripeService with the Stripe secret key
        this.stripeService = new StripeService("sk_test_51Oo6YNBLhdhPWlxZDdaZpVmnXQ9zlSfLraLx3jxcIaNJJvz5OHQcp2mBARahWN7hXyN3HbqHvk14yFVJppB6lHPc00LWqUxvap");
    }

    @FXML
    private void initialize() {
        // Optional: You can initialize UI elements or set default values here
    }

    @FXML
    private void createPaymentSession(ActionEvent event) {
        String productName = productNameField.getText();
        int amountInCents = Integer.parseInt(amountField.getText());
        String successUrl = successUrlField.getText();
        String cancelUrl = cancelUrlField.getText();

        try {
            // Call the StripeService to create a payment session
            stripeService.createPaymentSession(productName, amountInCents, successUrl, cancelUrl);
            resultText.setText("Payment session created successfully!");
        } catch (Exception e) {
            resultText.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}