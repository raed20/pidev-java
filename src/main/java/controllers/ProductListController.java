package controllers;

import entities.Product;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import services.ProductService;
import tools.MyConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableColumn<Product, String> catCol;

    @FXML
    private TableColumn<Product, String> descCol;

    @FXML
    private TableColumn<Product, Double> discountCol;

    @FXML
    private TableColumn<Product, Integer> idCol;

    @FXML
    private TableColumn<Product, String> imgCol;

    @FXML
    private TableColumn<Product, String> nameCol;

    @FXML
    private TableColumn<Product, Double> priceCol;

    @FXML
    private TableView<Product> tableview;

    private final ProductService productService;
    private Map<String, Integer> categoryMap = new HashMap<>();

    public void setCategoryMap(Map<String, Integer> categoryMap) {
        this.categoryMap = categoryMap;
    }

    public ProductListController() {
        MyConnection connection = new MyConnection();
        productService = new ProductService(connection);
    }

    @FXML
    void initialize() throws SQLException {
        refreshTable();
        List<Product> products = productService.getAll();
        ObservableList<Product> observableList = FXCollections.observableList(products);
        tableview.setItems(observableList);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Set cell value factory for category column
        catCol.setCellValueFactory(cellData -> {
            Integer categoryId = cellData.getValue().getCategory();
            String categoryName = getCategoryName(categoryId);
            return new SimpleStringProperty(categoryName);
        });

        imgCol.setCellValueFactory(new PropertyValueFactory<>("image")); // Assuming image is a String containing image path
        imgCol.setCellFactory(param -> new TableCell<Product, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(getClass().getResourceAsStream("/Images/" + imagePath));
                        imageView.setImage(image);
                        imageView.setFitWidth(100); // Set the width of the image
                        imageView.setFitHeight(100); // Set the height of the image
                        setGraphic(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                        setGraphic(null); // Clear the graphic if image loading fails
                    }
                }
            }

        });
    }

    private String getCategoryName(Integer categoryId) {
        for (Map.Entry<String, Integer> entry : categoryMap.entrySet()) {
            if (entry.getValue().equals(categoryId)) {
                return entry.getKey();
            }
        }
        return ""; // Return empty string if category ID is not found
    }
    @FXML
    void navigate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/BackOffice/Product/ProductAdd.fxml"));
                Parent root = loader.load();

                // Get the controller of the loaded FXML
                ProductAddController productAddController = loader.getController();
                anchorPane.getChildren().setAll(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @FXML
    void delete(ActionEvent event) {
        // Get the selected product from the TableView
        Product selectedProduct = tableview.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Delete the selected product from the database
            productService.delete(selectedProduct.getId());
            // Refresh the TableView after deletion
            try {
                refreshTable();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Show an alert if no product is selected
            showAlert("No Product Selected", "Please select a product to delete.");
        }
    }

    @FXML
    void update(ActionEvent event) {
        // Get the selected product from the TableView
        Product selectedProduct = tableview.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                // Load the AddProduct.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/BackOffice/Product/ProductAdd.fxml"));
                Parent root = loader.load();

                // Get the controller of the loaded FXML
                ProductAddController productAddController = loader.getController();

                // Set the selected product in the AddProductController
                productAddController.setProduct(selectedProduct);

                anchorPane.getChildren().setAll(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Show an alert if no product is selected
            showAlert("No Product Selected", "Please select a product to update.");
        }
    }

    // Method to refresh the TableView
    private void refreshTable() throws SQLException {
        List<Product> products = productService.getAll();
        ObservableList<Product> observableList = FXCollections.observableList(products);
        tableview.setItems(observableList);
    }

    // Method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
