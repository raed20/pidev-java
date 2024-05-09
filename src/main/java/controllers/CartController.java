package controllers;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import javafx.scene.control.Alert;
import entities.Panier;
import entities.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.PanierService;
import services.ProductService;
import tools.MyConnection;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CartController {
    @FXML
    private Label panierAlert;

    @FXML
    private Button btnPDF;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox cartPane;

    @FXML
    private Button deleteButton;

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
    @FXML
    private Button btnStripePayment;


    private PanierService panierService;
    private ProductService productService;

    public CartController() {
        MyConnection connection = new MyConnection();
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

                Label finalPriceLabel = new Label(String.format("%.2f TND", calculateFinalPrice(product, quantity)));
                finalPriceLabel.setPrefWidth(140);
                finalPriceLabel.setStyle("-fx-font-size: 14pt; -fx-padding: 5px");

                Button deleteButton = new Button("Delete");
                deleteButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> deleteFromCart(panier, product));

                productRow.getChildren().addAll(productNameLabel, productQuantityLabel, quantitySpinner, finalPriceLabel, deleteButton);
                cartPane.getChildren().add(productRow);

                initializeSpinner(quantitySpinner, productQuantityLabel, finalPriceLabel, product, panier);
            }
        }


        double totalPrice = calculateTotalPrice(paniers);
        total.setStyle("-fx-font-weight: bold;-fx-text-fill: #ff0000;");
        total.setText(String.format("%.2f TND", totalPrice));
    }

    private void initializeSpinner(Spinner<Integer> spinner, Label quantityLabel, Label finalPriceLabel, Product product, Panier panier) {
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            int oldQuantity = oldValue != null ? oldValue : 0;
            int newQuantity = newValue != null ? newValue : 0;

            quantityLabel.setText(" X " + newQuantity);

            double newFinalPrice = calculateFinalPrice(product, newQuantity);
            finalPriceLabel.setText(String.format("%.2f TND", newFinalPrice));

            panier.getProducts().put(product, newQuantity);
            panierService.update(panier);

            double totalPrice = calculateTotalPrice(panierService.getAll());
            total.setText(String.format("%.2f TND", totalPrice));
        });
    }

    private void deleteFromCart(Panier panier, Product product) {
        panier.getProducts().remove(product);
        panierService.update(panier);
        panierService.DeleteFromCart(product.getId());
        refreshCartView();
    }

    private void refreshCartView() {
        cartPane.getChildren().clear();
        initialize();
    }

    private double calculateFinalPrice(Product product, int quantity) {
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

    public void cancelCart(ActionEvent actionEvent) {
        panierService.clearPanierTable();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/FrontOffice/Command/Market.fxml"));
            Parent root = loader.load();
            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void exportPDF(javafx.scene.input.MouseEvent mouseEvent) {
        List<Panier> paniers = panierService.getAll();
        if (paniers.isEmpty()) {
            // Display message in panierAlert label
            panierAlert.setText("The cart is empty !");
        } else {
            String downloadsPath = System.getProperty("user.home") + "/Downloads/";
            String fileName = "Cart.pdf";
            String filePath = downloadsPath + fileName;
            File selectedFile = new File(filePath);

            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                document.open();

                Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 32, Font.BOLD, new BaseColor(56, 116, 255));
                Paragraph title = new Paragraph("Receipt", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingBefore(50);
                title.setSpacingAfter(30);
                document.add(title);

                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setSpacingBefore(20);
                table.setSpacingAfter(20);

                addTableHeader(table, "Product Name", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.WHITE));
                addTableHeader(table, "Quantity", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.WHITE));
                addTableHeader(table, "Price", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.WHITE));

                addCartItems(table, paniers);

                document.add(table);
                document.close();

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(selectedFile);
                } else {
                    System.out.println("Desktop is not supported on this platform.");
                }

                System.out.println("Le fichier PDF a été généré avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void addCartItems(PdfPTable table, List<Panier> paniers) {
        double totalPrice = 0;
        for (Panier panier : paniers) {
            for (Map.Entry<Product, Integer> entry : panier.getProducts().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                double finalPrice = calculateFinalPrice(product, quantity);
                totalPrice += finalPrice;

                addDataCell(table, product.getName(), new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL), new BaseColor(255, 255, 255));
                addDataCell(table, String.valueOf(quantity), new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL), new BaseColor(255, 255, 255));
                addDataCell(table, String.format("%.2f TND", finalPrice), new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL), new BaseColor(255, 255, 255));
            }
        }

        // Add a row for the total price
        addTableHeader(table, "Total Price", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.WHITE));
        PdfPCell totalPriceCell = new PdfPCell(new Paragraph(String.format("%.2f TND", totalPrice), new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL)));
        totalPriceCell.setBackgroundColor(new BaseColor(255, 255, 255));
        totalPriceCell.setBorderColor(new BaseColor(255, 255, 255));
        totalPriceCell.setPaddingTop(10);
        totalPriceCell.setPaddingBottom(10);
        totalPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalPriceCell.setColspan(2); // Spanning across 2 columns
        table.addCell(totalPriceCell);
    }


    private void addTableHeader(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(header, font));
        cell.setBackgroundColor(new BaseColor(56, 116, 255));
        cell.setBorderColor(new BaseColor(56, 116, 255));
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addDataCell(PdfPTable table, String data, Font font, BaseColor color) {
        PdfPCell cell = new PdfPCell(new Paragraph(data, font));
        cell.setBackgroundColor(color);
        cell.setBorderColor(color);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    @FXML
    private void handleStripePayment(ActionEvent event) {
        List<Panier> paniers = panierService.getAll();
        if (paniers.isEmpty()) {
            // Display message that the cart is empty
            showAlert("Error", "The cart is empty!");
        } else {
            double totalPrice = calculateTotalPrice(paniers);

            try {
                Stripe.apiKey = "sk_test_51PDwNDP6gcmiyTDaf8oJd5hUwlGUDSH46x4a3NEalpMdZA78jPqmqh93ZSoleW6Drnydkla6PcdlLSI6etrzhr6d00B5D4NGRs";
                PaymentIntent intent = PaymentIntent.create(Map.of(
                        "amount", (int) (totalPrice * 100), // Amount is in cents
                        "currency", "usd"
                ));

                // If payment is successful, show success message
                showAlert("Success", "Payment successful. Command added with success.");

                // Clear panier table
                if (!paniers.isEmpty()) {
                    panierService.clearPanierTable();
                }

                // Navigate back to Market.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/FrontOffice/Command/Market.fxml"));
                Parent root = loader.load();
                anchorPane.getChildren().setAll(root);
            } catch (StripeException | IOException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while processing payment. Please try again later.");
            }
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
