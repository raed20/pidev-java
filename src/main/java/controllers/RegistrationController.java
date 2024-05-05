package controllers;

import com.google.api.services.gmail.model.LabelColor;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import tools.MyConnection;
import weka.core.converters.DatabaseConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;


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
    private Label selectedImageLabel;




    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){

        File ProSyncFile = new File("src/main/resources/Images/438032592_1466819873939024_8254370732506893689_n.jpg");
        Image ProSyncImage = new Image(ProSyncFile.toURI().toString());
        shieldImageView.setImage(ProSyncImage);
        RoleBox.setItems(roleList);
        RoleBox.setValue("Choose a role here...");
    }
    @FXML
    private void browseImageButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        // Configurez le fileChooser pour qu'il ne montre que les fichiers d'images
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        // Afficher la boîte de dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            // Mettez à jour l'étiquette pour afficher le chemin du fichier sélectionné
            selectedImageLabel.setText(selectedFile.getAbsolutePath());
        } else {
            selectedImageLabel.setText("No image selected");
        }
    }


    public String evaluatePasswordStrength(String password) {
        // Vérifiez la longueur du mot de passe
        int passwordLength = password.length();
        if (passwordLength < 6) {
            return "weak";
        } else if (passwordLength >= 6 && passwordLength < 10) {
            return "good";
        } else {
            return "strong";
        }
    }




    public void registerButtonOnAction(ActionEvent event){


        if (EmailAddressTextField.getText().isEmpty() ||
                FullNameTextField.getText().isEmpty() ||
                PhoneNumberTextField.getText().isEmpty() ||
                AddressTextField.getText().isEmpty() ||
                SetPasswordField.getText().isEmpty() ||
                ConfirmPasswordField.getText().isEmpty() ||
                RoleBox.getValue() == null) {


            RegistrationMessageLabel.setText("Please fill in all fields.");
            RegistrationMessageLabel.setStyle("-fx-text-fill: red;");

            return;
        }



        String password = SetPasswordField.getText();
        String strength = evaluatePasswordStrength(password);




        if ("weak".equals(strength)) {
            RegistrationMessageLabel.setText("Pwd is too weak");
            ConfirmPasswordLabel.setStyle("-fx-text-fill: red;");

        } else {
            if (SetPasswordField.getText().equals(ConfirmPasswordField.getText())) {
                registerUser();
                ConfirmPasswordLabel.setText("");
            } else {
                ConfirmPasswordLabel.setText("Pwd not match.");
                ConfirmPasswordLabel.setStyle("-fx-text-fill: red;");
            }
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
        String hashedPassword = hashPassword(Password);
        String imagePath = selectedImageLabel.getText();


        String insertFields = "INSERT INTO user (email, lastname, numtel, adresse, password, roles, image) VALUES ('";
        String insertValues = EmailAddress + "','" + FullName + "','" + PhoneNum + "','" + Address + "','" + hashedPassword + "','" + Role + "','" + imagePath + "')";
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


    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());

            // Convert byte array to hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }



    public void LoginButtonOnAction(ActionEvent event) {
        //Stage stage = (Stage) RegistrationButton.getScene().getWindow();
        //stage.close();
        loginForm(((Node) event.getSource()).getScene().getWindow());
    }


    public void loginForm(Window previousWindow){
        try{

            Parent root = FXMLLoader.load(getClass().getResource("/javafx/FrontOffice/Login/login.fxml"));
            Stage RegisterStage = new Stage();
            RegisterStage.setTitle("ProSync");
            RegisterStage.setScene(new Scene(root,600,400));
            previousWindow.hide();
            RegisterStage.show();




        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

}
