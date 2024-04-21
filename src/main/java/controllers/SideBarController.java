package controllers;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SideBarController implements Initializable {

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
    private Label stockLabel; // Label for the Stock button

    @FXML
    private Button searchButton; // Label for the search button
    @FXML
    private Button creditCardButton; // Label for the search button
    @FXML
    private Button cogButton; // Label for the search button


    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private AnchorPane slider;
    @FXML
    private BorderPane borderPane;




    @Override
    public void initialize(URL location, ResourceBundle resources) {


        if (homeLabel != null) {
            setIconAndLabel(homeLabel, FontAwesomeIcon.HOME, "/path/to/home/page.fxml");
        }
        if (searchButton != null) {
            setIconMenu(searchButton, FontAwesomeIcon.SEARCH);
        }
        if (creditCardButton != null) {
            setIconMenu(creditCardButton, FontAwesomeIcon.CREDIT_CARD);
        }
        if (cogButton != null) {
            setIconMenu(cogButton, FontAwesomeIcon.COG);
        }
        if (investmentLabel != null) {
            setIconAndLabel(investmentLabel, FontAwesomeIcon.LINE_CHART, "/path/to/investment/page.fxml");
        }
        if (pretLabel != null) {
            setIconAndLabel(pretLabel, FontAwesomeIcon.MONEY, "/path/to/pret/page.fxml");
        }
        if (crmLabel != null) {
            setIconAndLabel(crmLabel, FontAwesomeIcon.GROUP, "/path/to/crm/page.fxml");
        }
        if (blogLabel != null) {
            setIconAndLabel(blogLabel, FontAwesomeIcon.NEWSPAPER_ALT, "/path/to/blog/page.fxml");
        }
        if (stockLabel != null) {
            setIconAndLabel(stockLabel, FontAwesomeIcon.DROPBOX, "/path/to/stock/page.fxml");
        }

        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(false);
                MenuBack.setVisible(true);
            });
        });

        MenuBack.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(true);
                MenuBack.setVisible(false);
            });
        });
        homeLabel.setOnMouseClicked(event -> loadPage("/path/to/homePage.fxml"));
        investmentLabel.setOnMouseClicked(event -> loadPage("/Javafx/FrontOffice/Investissement/polygonshow.fxml"));



    }
    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            borderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace(); // Handle any potential errors loading the FXML file
        }
    }




private void setIconAndLabel(Label label, FontAwesomeIcon iconType, String pagePath) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(iconType);
        iconView.getStyleClass().add("sidebar-btn-icon");
        iconView.setSize("2.5em"); // Set the size of the icon
        iconView.setFill(Color.WHITE); // Set icon color to white

        label.setGraphic(iconView);
        label.setGraphicTextGap(10); // Set the gap between the icon and text
        label.setTextFill(Color.WHITE); // Set text color to white
        label.setStyle("-fx-font-size: 16px;"); // Set font size
        label.setAlignment(Pos.BASELINE_LEFT); // Align text to the left
        label.setPrefWidth(200); // Set a fixed width for the label

        // Add event handler to navigate to the page when label is clicked
        label.setOnMouseClicked(event -> {
            // Navigate to the corresponding page
            // You need to implement the page navigation logic here
            // For example, you can use FXMLLoader to load the FXML file and switch scenes
        });



        // Add hover effect
        label.setOnMouseEntered(event -> {
            label.setStyle("-fx-background-color: white; -fx-text-fill: rgb(56, 116, 255);");
            iconView.setFill(Color.rgb(56, 116, 255)); // Change icon color on hover

            // Add circular effect
            Circle beforeCircle = new Circle(25, Color.WHITE);
            //beforeCircle.setTranslateX(-33); // Translate circle to left end
            StackPane.setAlignment(beforeCircle, Pos.TOP_LEFT); // Position the circle
            StackPane pane = new StackPane(beforeCircle, label.getGraphic());
            label.setGraphic(pane);
            label.getScene().setCursor(Cursor.HAND);
        });

        label.setOnMouseExited(event -> {
            label.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            iconView.setFill(Color.WHITE); // Reset icon color on mouse exit

            // Remove circular effect
            label.setGraphic(iconView);
            label.getScene().setCursor(Cursor.DEFAULT);
        });
    }
    private void setIconMenu(Button button, FontAwesomeIcon iconType) {
        FontAwesomeIconView iconViewMenu = new FontAwesomeIconView(iconType);
        iconViewMenu.getStyleClass().add("sidebar-btn-icon");
        iconViewMenu.setSize("2.3em"); // Set the size of the icon
        iconViewMenu.setFill(Color.rgb(56, 116, 255)); // Set icon color to white

        button.setGraphic(iconViewMenu);
        button.setGraphicTextGap(10); // Set the gap between the icon and text
        button.setTextFill(Color.WHITE); // Set text color to white
        button.setStyle("-fx-font-size: 16px;"); // Set font size
        button.setAlignment(Pos.BASELINE_CENTER); // Align text to the left

        // Add event handler to navigate to the page when label is clicked
        button.setOnMouseClicked(event -> {
            // Navigate to the corresponding page
            // You need to implement the page navigation logic here
            // For example, you can use FXMLLoader to load the FXML file and switch scenes
        });
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: rgb(56, 116, 255); -fx-text-fill: rgb(56, 116, 255);");
            iconViewMenu.setFill(Color.WHITE); // Change icon color on hover

            // Add circular effect


            button.getScene().setCursor(Cursor.HAND);
        });

        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            iconViewMenu.setFill(Color.rgb(56, 116, 255)); // Reset icon color on mouse exit

            // Remove circular effect
            button.setGraphic(iconViewMenu);
            button.getScene().setCursor(Cursor.DEFAULT);
        });
    }


}
