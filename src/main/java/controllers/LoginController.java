package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;
import tools.MyConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import java.net.URL;

public class LoginController implements Initializable {

    @FXML
    private Button RegistrationButton;
    @FXML
    private Label LoginMessage;
    @FXML
    private ImageView ProSyncImageView;
    @FXML
    private TextField EmailTextField;
    @FXML
    private PasswordField EnterPasswordField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        File ProSyncFile = new File("Images/438032592_1466819873939024_8254370732506893689_n.jpg");
        Image ProSyncImage = new Image(ProSyncFile.toURI().toString());
        ProSyncImageView.setImage(ProSyncImage);
    }

    public void LoginButtonOnAction(ActionEvent event) {


        if (EmailTextField.getText().isBlank() == false && EnterPasswordField.getText().isBlank() == false) {

            validateLogin();
        } else if (EmailTextField.getText().isBlank() == false && EnterPasswordField.getText().isBlank() == true){
            LoginMessage.setText("Please enter your Password");
        } else if (EmailTextField.getText().isBlank() == true && EnterPasswordField.getText().isBlank() == false){
            LoginMessage.setText("Please enter your Email");
        } else {

            LoginMessage.setText("Please enter your Email and Password");
        }
    }

    public void RegistrationButtonOnAction(ActionEvent event) {
        //Stage stage = (Stage) RegistrationButton.getScene().getWindow();
        //stage.close();
        createAccountForm();
    }

    public void validateLogin(){

        MyConnection connectNow = new MyConnection();
        Connection connectDB = connectNow.getConnection();


        String verifyLogin = "SELECT * FROM user WHERE email ='" + EmailTextField.getText() + "' AND password ='" + EnterPasswordField.getText() + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);



                if (queryResult.next()) {

                    LoginMessage.setText("You are Logged in!");
                    LoginMessage.setStyle("-fx-text-fill: green;");


                } else {

                    LoginMessage.setText("Invalid login. try again !");
                }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();

        }
    }

    public void createAccountForm(){
        try{

            Parent root = FXMLLoader.load(getClass().getResource("/FrontOffice.Login/registration.fxml"));
            Stage RegisterStage = new Stage();
            RegisterStage.initStyle(StageStyle.UNDECORATED);
            RegisterStage.setScene(new Scene(root,600,400));
            RegisterStage.show();

        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

}
