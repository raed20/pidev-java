package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Menubar implements Initializable {

    @FXML
    private Button search;

    @FXML
    private TextField searchBar;

    @FXML
    private Button creditCardButton;

    @FXML
    private Button cogButton;
    @FXML
    private VBox sidebar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFontAwesomeIcon(search, FontAwesomeIcon.SEARCH);
        setFontAwesomeIcon(creditCardButton, FontAwesomeIcon.CREDIT_CARD);
        setFontAwesomeIcon(cogButton, FontAwesomeIcon.COG);


    }


    private void setFontAwesomeIcon(Button button, FontAwesomeIcon iconType) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(iconType);
        iconView.getStyleClass().add("menu-bar-btns-icon");
        iconView.setSize("3em");

        button.setGraphic(iconView);
        button.setGraphicTextGap(10);
    }
}
