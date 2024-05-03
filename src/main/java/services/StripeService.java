package services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class StripeService {
    private final String stripeSecretKey;

    public StripeService(String stripeSecretKey) {
        this.stripeSecretKey = stripeSecretKey;
        Stripe.apiKey = stripeSecretKey;
    }

    public Session createPaymentSession(String productName, int amountInCents, String successUrl, String cancelUrl) throws StripeException {
        Session session = Session.create(
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("usd")
                                                        .setUnitAmount((long) amountInCents)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName(productName)
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .setQuantity(1L)
                                        .build()
                        )
                        .setSuccessUrl(successUrl)
                        .setCancelUrl(cancelUrl)
                        .build()
        );

        return session;
    }
}