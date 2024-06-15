package controllers;

import entities.Category;
import entities.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import services.CategoryService;
import services.ProductService;
import tools.MyConnection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAddController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView imageTF;

    @FXML
    private Label catAlert;

    @FXML
    private ChoiceBox<String> catTF;

    @FXML
    private Label descAlert;

    @FXML
    private TextArea descTF;

    @FXML
    private Label disAlert;

    @FXML
    private TextField discountTF;

    @FXML
    private Label imgAlert;

    @FXML
    private Label nameAlert;

    @FXML
    private TextField nameTF;

    @FXML
    private Text imgPath;

    @FXML
    private Label priceAlert;

    @FXML
    private TextField priceTF;

    private Map<String, Integer> categoryMap = new HashMap<>();
    private ProductService productService;
    private CategoryService categoryService;
    private Product selectedProduct;


    public void setProduct(Product product) {
        selectedProduct = product;
        nameTF.setText(selectedProduct.getName());
        priceTF.setText(String.valueOf(selectedProduct.getPrice()));
        discountTF.setText(String.valueOf(selectedProduct.getDiscount()));
        descTF.setText(selectedProduct.getDescription());

        Integer categoryId = selectedProduct.getCategory();
        String categoryName = String.valueOf(categoryMap.get(categoryId));
        catTF.setValue(categoryName);
    }

    public ProductAddController() {
        this.productService = new ProductService(new MyConnection());
        this.categoryService = new CategoryService(new MyConnection());
    }

    @FXML
    void initialize() throws SQLException {
        // Retrieve categories from the database
        List<Category> categories = categoryService.getAll();
        // Populate the ChoiceBox with category names
        populateCategoryChoiceBox(categories);
    }

    private void populateCategoryChoiceBox(List<Category> categories) {
        for (Category category : categories) {
            // Add category name to ChoiceBox items
            catTF.getItems().add(category.getName());
            // Store category ID and name in the map
            categoryMap.put(category.getName(), category.getId());
        }
    }

    @FXML
    void AddProduct(ActionEvent event) {
        // Validate inputs before attempting to add or update the product
        if (!validateInputs()) {
            return; // Exit method if inputs are not valid
        }

        // Check if the selectedProduct is null
        if (selectedProduct == null) {
            // If selectedProduct is null, it means a new product is being added
            addNewProduct();
        } else {
            // If selectedProduct is not null, it means an existing product is being updated
            updateProduct();
        }
    }

    private void addNewProduct() {
        // Create a new Product object with data from the form
        Product product = new Product();
        product.setName(nameTF.getText());

        // Check if priceTF is not empty before parsing
        String priceText = priceTF.getText();
        if (!priceText.isEmpty()) {
            product.setPrice(Double.parseDouble(priceText));
        } else {
            // Handle case where price is empty
            priceAlert.setText("Please enter a price");
            return; // Exit method
        }

        product.setDescription(descTF.getText());
        product.setImage(imgPath.getText());
        String categoryName = catTF.getValue();
        Integer categoryId = categoryMap.get(categoryName);
        product.setCategory(categoryId);
        double discount = Double.parseDouble(discountTF.getText());
        product.setDiscount(discount);

        productService.add(product);

        // Navigate to ProductList.fxml after adding the product
        navigate(null);
    }

    private void updateProduct() {
        // Update the selectedProduct with new data from the form
        selectedProduct.setName(nameTF.getText());
        selectedProduct.setPrice(Double.parseDouble(priceTF.getText()));
        selectedProduct.setDescription(descTF.getText());
        selectedProduct.setImage(imgPath.getText());
        String categoryName = catTF.getValue();
        Integer categoryId = categoryMap.get(categoryName);
        selectedProduct.setCategory(categoryId);
        selectedProduct.setDiscount(Double.parseDouble(discountTF.getText()));

        // Call the ProductService to update the product in the database
        productService.update(selectedProduct);

        // Navigate to ProductList.fxml after updating the product
        navigate(null);
    }


    private boolean validateInputs() {
        boolean isValid = true;

        // Check if the product name starts with a letter
        if (nameTF.getText().isEmpty()) {
            nameAlert.setText("Name must not be empty");
            isValid = false;
        } else if (!Character.isLetter(nameTF.getText().charAt(0))) {
            nameAlert.setText("Please enter a name starting with a letter");
            isValid = false;
        } else {
            nameAlert.setText("");
        }

        // Check if the price is a valid double
        if (priceTF.getText().isEmpty()) {
            priceAlert.setText("Price must not be empty");
            isValid = false;
        } else {
            try {
                double price = Double.parseDouble(priceTF.getText());
                if (price < 0) { // Check if price is negative
                    priceAlert.setText("Price must be non-negative");
                    isValid = false;
                } else {
                    priceAlert.setText("");
                }
            } catch (NumberFormatException e) {
                priceAlert.setText("Please enter a valid price");
                isValid = false;
            }
        }


        // Check if the discount is between 0 and 100
        try {
            double discount = Double.parseDouble(discountTF.getText());
            if (discount < 0 || discount > 100) {
                disAlert.setText("Discount must be between 0 and 100");
                isValid = false;
            } else {
                disAlert.setText("");
            }
        } catch (NumberFormatException e) {
            disAlert.setText("Please enter a valid discount");
            isValid = false;
        }

        // Check if a category is selected
        if (catTF.getValue() == null || catTF.getValue().isEmpty()) {
            catAlert.setText("Please select a category");
            isValid = false;
        } else {
            catAlert.setText("");
        }

        // Check if the description has at least 20 characters
        if (descTF.getText().isEmpty()) {
            descAlert.setText("Description must not be empty");
            isValid = false;
        } else if (descTF.getText().length() < 20 || descTF.getText().length() > 250) {
            descAlert.setText("Description must have between 20 and 250 characters");
            isValid = false;
        } else {
            descAlert.setText("");
        }

        // Check if an image is uploaded
        if (imgPath.getText().isEmpty()) {
            imgAlert.setText("Please upload an image");
            isValid = false;
        } else {
            imgAlert.setText("");
        }

        return isValid;
    }

    @FXML
    void Upload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Extract just the name of the file without the path
                String fileName = selectedFile.getName();

                File destDir = new File("src/main/resources/Images");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                File destination = new File(destDir, fileName);
                Files.copy(selectedFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Image image = new Image(destination.toURI().toString());
                imageTF.setImage(image);
                imgPath.setText(fileName); // Set the text property to just the file name
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    @FXML
    void navigate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/BackOffice/Product/ProductList.fxml"));
            Parent root = loader.load();

            // Get the controller of the loaded FXML
            ProductListController productListController = loader.getController();

            // Pass the category map to ProductListController
            productListController.setCategoryMap(categoryMap);

            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
