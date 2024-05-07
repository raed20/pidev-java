package controllers;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BackSideBarController  implements Initializable{
    @FXML
    private Label homeLabel; // Assuming this is the label for the Home button

    @FXML
    private Label investmentLabel; // Label for the Investment button
    @FXML
    private Label pretLabel; // Label for the pret button

    @FXML
    private Label crmLabel; // Label for the CRM button

    @FXML
    private Label blogLabel; // Label for the Blog button
    @FXML
    private Label categoryLabel; // Label for the Stock button
    @FXML
    private Label productLabel; // Label for the Stock button



    @FXML
    private MenuButton cogButton; // Label for the search button

    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private AnchorPane slider;
    @FXML
    private BorderPane borderPane;

    private boolean isSidebarOpen = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSidebar();
        initializeLabels();
        //Add the path of your fxml file
        homeLabel.setOnMouseClicked(event -> loadPage("/path/to/homePage.fxml"));
        investmentLabel.setOnMouseClicked(event -> loadPage("/Javafx/BackOffice/Investissement/opportuniteback.fxml"));
        pretLabel.setOnMouseClicked(event -> loadPage("/Javafx/FrontOffice/Investissement/polygonshow.fxml"));
        crmLabel.setOnMouseClicked(event -> loadPage("/Javafx/FrontOffice/Investissement/polygonshow.fxml"));
        blogLabel.setOnMouseClicked(event -> loadPage("/Javafx/FrontOffice/Investissement/polygonshow.fxml"));
        categoryLabel.setOnMouseClicked(event -> loadPage("/Javafx/BackOffice/Category/CategoryAdd.fxml"));
        productLabel.setOnMouseClicked(event -> loadPage("/Javafx/BackOffice/Product/ProductAdd.fxml"));

        //drop down menu for user settings
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem disconnectItem = new MenuItem("Disconnect");
        boolean isAdmin = checkIfAdmin(); // Implement this method to check if the user is an admin
        if (isAdmin) {
            addAdminMenuItem();
        }
        // event handlers for menu items
        settingsItem.setOnAction(event -> handleSettingsClicked());
        disconnectItem.setOnAction(event -> handleDisconnectClicked());

        // Add menu items to the cogButton
        cogButton.getItems().addAll(settingsItem, disconnectItem);





    }

    private void initializeSidebar() {
        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event -> toggleSidebar());
        MenuBack.setOnMouseClicked(event -> toggleSidebar());
    }

    private void initializeLabels() {
        setIconAndLabel(homeLabel, FontAwesomeIcon.DESKTOP);
        setIconMenucog(cogButton, FontAwesomeIcon.COG);
        setIconAndLabel(investmentLabel, FontAwesomeIcon.LINE_CHART);
        setIconAndLabel(pretLabel, FontAwesomeIcon.MONEY);
        setIconAndLabel(crmLabel, FontAwesomeIcon.GROUP);
        setIconAndLabel(blogLabel, FontAwesomeIcon.NEWSPAPER_ALT);
        setIconAndLabel(categoryLabel, FontAwesomeIcon.DROPBOX);
        setIconAndLabel(productLabel, FontAwesomeIcon.SHOPPING_BAG);

    }

    private void toggleSidebar() {
        TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), slider);
        TranslateTransition centerSlide = new TranslateTransition(Duration.seconds(0.4), borderPane.getCenter());

        if (isSidebarOpen) {
            slide.setToX(-176);
            centerSlide.setToX(-100);
            Menu.setVisible(true);
            MenuBack.setVisible(false);
        } else {
            slide.setToX(0);
            centerSlide.setToX(20);
            Menu.setVisible(false);
            MenuBack.setVisible(true);
        }

        slide.play();
        centerSlide.play();
        isSidebarOpen = !isSidebarOpen;
    }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            borderPane.setCenter(root);

            // Manually set the translation of the center content
            if (isSidebarOpen) {
                borderPane.getCenter().setTranslateX(20);
            } else {
                borderPane.getCenter().setTranslateX(-20);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential errors loading the FXML file
        }
    }

    private void setIconAndLabel(Label label, FontAwesomeIcon iconType) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(iconType);
        setIconProperties(label, iconView);
    }

    private void setIconMenu(Button button, FontAwesomeIcon iconType) {
        FontAwesomeIconView iconViewMenu = new FontAwesomeIconView(iconType);
        setIconProperties(button, iconViewMenu);
        button.setOnMouseClicked(event -> {
            // Implement logic to handle button click
        });
    }
    private void setIconMenucog(MenuButton button, FontAwesomeIcon iconType) {
        FontAwesomeIconView iconViewMenu = new FontAwesomeIconView(iconType);
        setIconmenu(button, iconViewMenu);
        button.setOnMouseClicked(event -> {
            // Implement logic to handle button click
        });
    }

    private void setIconProperties(ButtonBase button, FontAwesomeIconView iconView) {
        iconView.getStyleClass().add("sidebar-btn-icon");
        iconView.setSize("2.3em");
        iconView.setFill(Color.rgb(56, 116, 255));
        button.setGraphic(iconView);
        button.setGraphicTextGap(10);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-font-size: 16px;");
        button.setAlignment(Pos.BASELINE_CENTER);
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: rgb(56, 116, 255); -fx-text-fill: rgb(56, 116, 255);");
            iconView.setFill(Color.WHITE);
            button.getScene().setCursor(Cursor.HAND);
        });
        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            iconView.setFill(Color.rgb(56, 116, 255));
            button.getScene().setCursor(Cursor.DEFAULT);
        });
    }
    private void setIconmenu(MenuButton button, FontAwesomeIconView iconView) {
        iconView.getStyleClass().add("sidebar-btn-icon");
        iconView.setSize("2.3em");
        iconView.setFill(Color.rgb(56, 116, 255));
        button.setGraphic(iconView);
        button.setGraphicTextGap(10);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-font-size: 16px;");
        button.setAlignment(Pos.BASELINE_CENTER);
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: rgb(56, 116, 255); -fx-text-fill: rgb(56, 116, 255);");
            iconView.setFill(Color.WHITE);
            button.getScene().setCursor(Cursor.HAND);
        });
        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            iconView.setFill(Color.rgb(56, 116, 255));
            button.getScene().setCursor(Cursor.DEFAULT);
        });
    }

    private void setIconProperties(Label label, FontAwesomeIconView iconView) {
        iconView.getStyleClass().add("sidebar-btn-icon");
        iconView.setSize("2.5em");
        iconView.setFill(Color.WHITE);
        label.setGraphic(iconView);
        label.setGraphicTextGap(10);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 16px;");
        label.setAlignment(Pos.BASELINE_LEFT);
        label.setPrefWidth(200);
        label.setOnMouseEntered(event -> {
            label.setStyle("-fx-background-color: white; -fx-text-fill: rgb(56, 116, 255);");
            iconView.setFill(Color.rgb(56, 116, 255));
            Circle beforeCircle = new Circle(25, Color.WHITE);
            StackPane.setAlignment(beforeCircle, Pos.TOP_LEFT);
            StackPane pane = new StackPane(beforeCircle, label.getGraphic());
            label.setGraphic(pane);
            label.getScene().setCursor(Cursor.HAND);
        });
        label.setOnMouseExited(event -> {
            label.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            iconView.setFill(Color.WHITE);
            label.setGraphic(iconView);
            label.getScene().setCursor(Cursor.DEFAULT);
        });
    }
    private boolean checkIfAdmin() {
        // Implement the logic to check if the connected user is an admin
        return true; // For demonstration, always return true
    }

    private void addAdminMenuItem() {
        MenuItem adminItem = new MenuItem("FrontOffice");
        adminItem.setOnAction(event -> handleAdminClicked());
        cogButton.getItems().add(adminItem);
    }
    private void handleAdminClicked( ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Javafx/FrontOffice/FrontSidebar.fxml"));
            Parent root = loader.load();

            // Create a new scene with the sidebar
            Scene newScene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) borderPane.getScene().getWindow();

            // Store the current scene size
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            // Set the size of the new scene to match the current scene size
            newScene.setRoot(root);
            newScene.setFill(Color.TRANSPARENT); // Optionally set the fill color to transparent
            stage.setScene(newScene);

            // Set the size of the stage to match the stored size
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);

            // Request focus on the scene
            newScene.getRoot().requestFocus();
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential errors loading the FXML file
        }
    }

    // Event handler for Settings menu item
    private void handleSettingsClicked() {
        // Implement logic for when Settings menu item is clicked
        System.out.println("Settings clicked");
    }

    // Event handler for Disconnect menu item
    private void handleDisconnectClicked() {
        // Implement logic for when Disconnect menu item is clicked
        System.out.println("Disconnect clicked");
    }
}
