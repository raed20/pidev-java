package controllers;

import entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.ProductService;
import tools.MyConnection;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductListController {

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

    public ProductListController() {
        MyConnection connection = new MyConnection();
        productService = new ProductService(connection);
    }

    @FXML
    void initialize() throws SQLException {
        refreshTable();
        List<Product> products=productService.getAll();
        ObservableList<Product> observableList=FXCollections.observableList(products);
        tableview.setItems(observableList);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        catCol.setCellValueFactory(new PropertyValueFactory<>("category_id"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        imgCol.setCellValueFactory(new PropertyValueFactory<>("image"));
    }

    @FXML
    void navigate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/BackOffice/Product/AddProduct.fxml"));
                Parent root = loader.load();

                // Get the controller of the loaded FXML
                CategoryAddController categoryAddController = loader.getController();

                // If CategoryController has a method to initialize or handle navigation, you can call it here

                Stage window = (Stage) tableview.getScene().getWindow();
                window.setScene(new Scene(root));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/BackOffice/Product/AddProduct.fxml"));
                Parent root = loader.load();

                // Get the controller of the loaded FXML
                ProductAddController productAddController = loader.getController();

                // Set the selected product in the AddProductController
                productAddController.setProduct(selectedProduct);

                // Get the stage and set the new scene
                Stage window = (Stage) tableview.getScene().getWindow();
                window.setScene(new Scene(root));
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
