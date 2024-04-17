package controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set FontAwesome icons to labels
        setIconAndLabel(homeLabel, FontAwesomeIcon.HOME, "/path/to/home/page.fxml");
        setIconAndLabel(investmentLabel, FontAwesomeIcon.LINE_CHART, "/path/to/investment/page.fxml");
        setIconAndLabel(pretLabel, FontAwesomeIcon.MONEY, "/path/to/pret/page.fxml");
        setIconAndLabel(crmLabel, FontAwesomeIcon.GROUP, "/path/to/crm/page.fxml");
        setIconAndLabel(blogLabel, FontAwesomeIcon.NEWSPAPER_ALT, "/path/to/blog/page.fxml");
        setIconAndLabel(stockLabel, FontAwesomeIcon.DROPBOX, "/path/to/stock/page.fxml");
    }

    private void setIconAndLabel(Label label, FontAwesomeIcon iconType, String pagePath) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(iconType);
        iconView.getStyleClass().add("sidebar-btn-icon");
        iconView.setSize("3em"); // Set the size of the icon
        iconView.setFill(Color.WHITE); // Set icon color to white

        label.setGraphic(iconView);
        label.setGraphicTextGap(10); // Set the gap between the icon and text
        label.setTextFill(Color.WHITE); // Set text color to white
        label.setStyle("-fx-font-size: 20px;"); // Set font size
        label.setAlignment(Pos.BASELINE_LEFT); // Align text to the left
        label.setPrefWidth(300); // Set a fixed width for the label

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
            beforeCircle.setTranslateX(-33); // Translate circle to left end
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



}
