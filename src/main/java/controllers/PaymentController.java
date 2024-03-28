package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

public class PaymentController {
    @FXML
    private TextField productNameField;

    @FXML
    private TextField amountField;

    @FXML
    private Button payButton;

    @FXML
    private Text resultText;

    private StripeService stripeService;

    public PaymentController() {
        this.stripeService = new StripeService("sk_test_51Oo6YNBLhdhPWlxZDdaZpVmnXQ9zlSfLraLx3jxcIaNJJvz5OHQcp2mBARahWN7hXyN3HbqHvk14yFVJppB6lHPc00LWqUxvap");
    }

    @FXML
    private void initialize() {
        // Initialize your controller
    }

    @FXML
    private void handlePayment() {
        String productName = productNameField.getText();
        int amountInCents = Integer.parseInt(amountField.getText());

        try {
            Session session = stripeService.createPaymentSession(productName, amountInCents, "https://twitter.com/home", "https://twitter.com/home");
            resultText.setText("Payment session created successfully!");
            // Redirect user to Stripe checkout page using session.getId()
        } catch (StripeException e) {
            String errorMessage = "Error creating payment session: " + e.getMessage();
            resultText.setText(errorMessage);
        }
    }
}
