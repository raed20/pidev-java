package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {

    @FXML
    private BorderPane border_pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if (border_pane == null) {
                throw new IllegalStateException("border_pane is not injected.");
            }

            Parent sidebar = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Javafx/FrontOffice/Sidebar.fxml")));
           // Parent menubar = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Javafx/FrontOffice/Menubar.fxml")));
            Parent content = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Javafx/FrontOffice/Investissement/polygonshow.fxml")));
            //border_pane.setTop(menubar);
            border_pane.setCenter(content);
            border_pane.setLeft(sidebar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
