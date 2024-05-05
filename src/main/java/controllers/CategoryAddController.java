package controllers;

import entities.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import services.CategoryService;
import tools.MyConnection;

import java.io.IOException;

public class CategoryAddController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField catTF;
    @FXML
    private Label catAlert;
    private Category selectedCategory;
    private final CategoryService cs;

    public CategoryAddController() {
        MyConnection connection = new MyConnection();
        cs = new CategoryService(connection);
    }

    @FXML
    void AddCategory(ActionEvent event) {
        String categoryName = catTF.getText();
        if (isValidInput(categoryName)) {
            try {
                if (selectedCategory != null) {
                    // update
                    selectedCategory.setName(categoryName);
                    cs.update(selectedCategory);
                    navigate(event); // Navigate back to category list after updating
                } else {
                    addNewCategory(categoryName, event);
                }
            } catch (RuntimeException e) {
                showAlert("Error", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addNewCategory(String categoryName, ActionEvent event) {
        if (cs.isCategoryExists(categoryName)) {
            catAlert.setText("Category already exists!");
        } else {
            cs.add(new Category(categoryName));
            catTF.setText("");
            navigate(event);
        }
    }

    private boolean isValidInput(String input) {
        if (input.isEmpty()) {
            catAlert.setText("Category name cannot be empty!");
            return false;
        } else if (!input.matches("[a-zA-Z]+")) {
            catAlert.setText("Only alphabetic characters!");
            return false;
        } else if (input.length() < 2) {
            catAlert.setText("At least two letters!");
            return false;
        }
        return true;
    }


    @FXML
    void navigate(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Javafx/BackOffice/Category/CategoryList.fxml"));
            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setCategory(Category category) {
        this.selectedCategory = category;
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
