package controllers;

import com.google.api.services.gmail.model.LabelColor;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tools.MyConnection;
import weka.core.converters.DatabaseConnection;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;


public class RegistrationController implements Initializable {

    ObservableList<String> roleList = FXCollections.observableArrayList("Client","Fournisseur");

    @FXML
    private ImageView shieldImageView;
    @FXML
    private Button RegisterButton;
    @FXML
    private Button LoginButton;
    @FXML
    private Label RegistrationMessageLabel;
    @FXML
    private PasswordField SetPasswordField;
    @FXML
    private PasswordField ConfirmPasswordField;
    @FXML
    private Label ConfirmPasswordLabel;
    @FXML
    private TextField EmailAddressTextField;
    @FXML
    private TextField FullNameTextField;
    @FXML
    private TextField PhoneNumberTextField;
    @FXML
    private TextField AddressTextField;
    @FXML
    private ChoiceBox RoleBox;




    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){

        File ProSyncFile = new File("Images/438032592_1466819873939024_8254370732506893689_n.jpg");
        Image ProSyncImage = new Image(ProSyncFile.toURI().toString());
        shieldImageView.setImage(ProSyncImage);
        RoleBox.setItems(roleList);
        RoleBox.setValue("RoleBox");
    }

    public void registerButtonOnAction(ActionEvent event){

        if (SetPasswordField.getText().equals(ConfirmPasswordField.getText())) {

            registerUser();
            ConfirmPasswordLabel.setText("");


        } else {

            ConfirmPasswordLabel.setText("Pwd not match");
            ConfirmPasswordLabel.setStyle("-fx-text-fill: red;");
        }

    }

    public void registerUser(){

        MyConnection connectNow = new MyConnection();
        Connection connectDB = connectNow.getConnection();


        String EmailAddress = EmailAddressTextField.getText();
        String FullName = FullNameTextField.getText();
        String PhoneNum = PhoneNumberTextField.getText();
        String Address = AddressTextField.getText();
        String Password = SetPasswordField.getText();
        String Role = (String) RoleBox.getValue();


        String insertFields = "INSERT INTO user (email, lastname, numtel, adresse, password, roles) VALUES ('";
        String insertValues = EmailAddress + "','" + FullName + "','" + PhoneNum + "','" + Address + "','" + Password + "','" + Role + "')";
        String insertToRegister = insertFields + insertValues;

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate((insertToRegister));

            RegistrationMessageLabel.setText("User register done!");
            ConfirmPasswordLabel.setStyle("-fx-text-fill: green;");

        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }





    }


    public void LoginButtonOnAction(ActionEvent event) {
        //Stage stage = (Stage) RegistrationButton.getScene().getWindow();
        //stage.close();
        loginForm();
    }


    public void loginForm(){
        try{

            Parent root = FXMLLoader.load(getClass().getResource("/FrontOffice.Login/login.fxml"));
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
