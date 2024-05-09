package controllers;

import entities.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import services.CategoryService;
import tools.MyConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategoryListController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableColumn<Category, String> catCol;
    @FXML
    private TableColumn<Category, Integer> idCol;

    @FXML
    private TableView<Category> tableview;

    public final CategoryService cs;
    public CategoryListController(){
        MyConnection connection = new MyConnection();
        cs = new CategoryService(connection);
    }

    @FXML
    void initialize() throws SQLException {
        List<Category> categories = cs.getAll();
        ObservableList<Category> observableList = FXCollections.observableList(categories);
        tableview.setItems(observableList);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        catCol.setCellValueFactory(new PropertyValueFactory<>("name")); // Set cell value factory for category name
    }


    @FXML
    void navigate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/BackOffice/Category/CategoryAdd.fxml"));
            Parent root = loader.load();

            // Get the controller of the loaded FXML
            CategoryAddController categoryAddController = loader.getController();

            // If CategoryController has a method to initialize or handle navigation, you can call it here


            anchorPane.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void delete(ActionEvent event) {
        // Get the selected category from the TableView
        Category selectedCategory = tableview.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            // Delete the selected category from the database
            cs.delete(selectedCategory.getId());
            // Refresh the TableView after deletion
            try {
                refreshTable();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Show an alert if no category is selected
            showAlert("No Category Selected", "Please select a category to delete.");
        }
    }

    @FXML
    void update(ActionEvent event) {
        // Get the selected category from the TableView
        Category selectedCategory = tableview.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                // Load the CategoryAdd.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/BackOffice/Category/CategoryAdd.fxml"));
                Parent root = loader.load();

                // Get the controller of the loaded FXML
                CategoryAddController categoryAddController = loader.getController();

                // Set the selected category in the CategoryController
                categoryAddController.setCategory(selectedCategory);

                // Get the stage and set the new scene
                anchorPane.getChildren().setAll(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Show an alert if no category is selected
            showAlert("No Category Selected", "Please select a category to update.");
        }
    }

    // Method to refresh the TableView
    private void refreshTable() throws SQLException {
        List<Category> categories = cs.getAll();
        ObservableList<Category> observableList = FXCollections.observableList(categories);
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
