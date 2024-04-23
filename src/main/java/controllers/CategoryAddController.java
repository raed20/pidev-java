package controllers;

import entities.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import services.CategoryService;
import tools.MyConnection;

import java.io.IOException;

public class CategoryAddController {

    @FXML
    private TextField catTF;

    private Category selectedCategory;
    private final CategoryService cs;

    public CategoryAddController() {
        // Initialize the CategoryService with a MyConnection object
        MyConnection connection = new MyConnection();
        cs = new CategoryService(connection);
    }

    @FXML
    void AddCategory(ActionEvent event) {
        String categoryName = catTF.getText();
        if (!categoryName.isEmpty()) {
            try {
                if (selectedCategory != null) {
                    // If a category is selected, update it
                    selectedCategory.setName(categoryName);
                    cs.update(selectedCategory);
                } else {
                    // If no category is selected, add a new one
                    cs.add(new Category(categoryName));
                }
                // Clear the TextField after adding or updating
                catTF.setText("");
                // Navigate back to the CategoryList.fxml
                navigate(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Show an alert if the category name is empty
            showAlert("Invalid Category Name", "Please enter a category name.");
        }
    }

    @FXML
    void navigate(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/BackOffice/CategoryList.fxml"));
            catTF.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setCategory(Category category) {
        this.selectedCategory = category;
        // Set the name of the selected category in the TextField for updating
        catTF.setText(category.getName());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
