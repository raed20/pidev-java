package controllers;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
            total.setText("Total: " + String.format("%.2f TND", totalPrice));
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
    public void command(javafx.scene.input.MouseEvent mouseEvent) {
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

                Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 32, Font.BOLD, new BaseColor(114, 0, 0));
                Paragraph title = new Paragraph("Panier", titleFont);
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
        for (Panier panier : paniers) {
            for (Map.Entry<Product, Integer> entry : panier.getProducts().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                addDataCell(table, product.getName(), new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL), new BaseColor(255, 255, 255));
                addDataCell(table, String.valueOf(quantity), new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL), new BaseColor(255, 255, 255));
                addDataCell(table, String.format("%.2f TND", calculateFinalPrice(product, quantity)), new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL), new BaseColor(255, 255, 255));
            }
        }
    }

    private void addTableHeader(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(header, font));
        cell.setBackgroundColor(new BaseColor(114, 0, 0));
        cell.setBorderColor(new BaseColor(114, 0, 0));
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


}
