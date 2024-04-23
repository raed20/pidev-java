package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class LoginController {

    @FXML
    private Button RegistrationButton;

    public void RegistrationButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) RegistrationButton.getScene().getWindow();
        stage.close();
    }
}
