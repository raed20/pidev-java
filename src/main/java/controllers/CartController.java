package controllers;

import entities.Panier;
import entities.Product;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.PanierService;
import services.ProductService;
import tools.MyConnection;

import java.util.List;
import java.util.Map;

public class CartController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox cartPane;

    @FXML
    private Button deletButton;

    @FXML
    private Label finalPrice;

    @FXML
    private Label productName;

    @FXML
    private Label productQuantity;

    @FXML
    private HBox productRow;

    @FXML
    private Spinner<?> spinQuantity;

    @FXML
    private Label total;
    private PanierService panierService;
    private ProductService productService;


    public CartController() {
        MyConnection connection = new MyConnection(); // Initialize your connection
        panierService = new PanierService(connection);
        productService = new ProductService(connection);
    }
    public void initialize() {
        List<Panier> paniers = panierService.getAll();
            for (Panier panier : paniers) {
            for (Map.Entry<Product, Integer> entry : panier.getProducts().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                HBox productRow = new HBox();
                productRow.setAlignment(Pos.CENTER);

                Label productNameLabel = new Label(product.getName());
                productNameLabel.setPrefWidth(200);
                productNameLabel.setStyle("-fx-font-size: 14pt; -fx-padding: 5px");

                Label productQuantityLabel = new Label(" X " + quantity);
                productQuantityLabel.setPrefWidth(70);
                productQuantityLabel.setStyle("-fx-font-size: 14pt; -fx-padding: 5px");

                Spinner<Integer> quantitySpinner = new Spinner<>(0, Integer.MAX_VALUE, quantity);
                quantitySpinner.setPrefWidth(60);

                Label finalPriceLabel = new Label(calculateFinalPrice(product, quantity) + " TND");
                finalPriceLabel.setPrefWidth(140);
                finalPriceLabel.setStyle("-fx-font-size: 14pt; -fx-padding: 5px");

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> deleteFromCart(panier, product));

                productRow.getChildren().addAll(productNameLabel, productQuantityLabel, quantitySpinner, finalPriceLabel, deleteButton);
                cartPane.getChildren().add(productRow);

                // Initialize the spinner with listeners
                initializeSpinner(quantitySpinner, productQuantityLabel, finalPriceLabel, product, panier);
            }
        }

        double totalPrice = calculateTotalPrice(paniers);
        total.setText("Total: " + String.format("%.2f TND", totalPrice));
    }


    private void initializeSpinner(Spinner<Integer> spinner, Label quantityLabel, Label finalPriceLabel, Product product, Panier panier) {
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            int oldQuantity = oldValue != null ? oldValue : 0;
            int newQuantity = newValue != null ? newValue : 0;

            // Update the quantity label
            quantityLabel.setText(" X " + newQuantity);

            // Update the final price label
            double newFinalPrice = calculateFinalPrice(product, newQuantity);
            finalPriceLabel.setText(String.format("%.2f TND", newFinalPrice));

            // Update the quantity in the cart
            panier.getProducts().put(product, newQuantity);
            // Update the cart in the database
            panierService.update(panier);
            // Refresh the total price
            double totalPrice = calculateTotalPrice(panierService.getAll());
            total.setText("Total: " + String.format("%.2f TND", totalPrice));
        });
    }


    private void deleteFromCart(Panier panier, Product product) {
        // Remove the product from the cart
        panier.getProducts().remove(product);
        // Update the cart in the database
        panierService.update(panier);
        // Delete the product from the Panier table
        panierService.DeleteFromCart(product.getId());
        // Refresh the cart view
        refreshCartView();
    }

    private void refreshCartView() {
        // Clear the current cart view
        cartPane.getChildren().clear();
        // Re-populate the cart view
        initialize();
    }


    private double calculateFinalPrice(Product product, int quantity) {
        // Calculate the final price after discount
        double discountPrice = product.getPrice() - ((product.getPrice() * product.getDiscount()) / 100);
        return discountPrice * quantity;
    }


    private double calculateTotalPrice(List<Panier> paniers) {
        double totalPrice = 0;
        for (Panier panier : paniers) {
            for (Product product : panier.getProducts().keySet()) {
                int quantity = panier.getProducts().get(product);
                totalPrice += calculateFinalPrice(product, quantity);
            }
        }
        return totalPrice;
    }
}
