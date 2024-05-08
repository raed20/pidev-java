package controllers;

import entities.Utilisateurs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.UtilisateurService;
import tools.MyConnection;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {

    ObservableList<String> roleList = FXCollections.observableArrayList("Client","Fournisseur","Admin");



    @FXML
    private TextField FullNameTextField;
    @FXML
    private TextField EmailAddressTextField;
    @FXML
    private ChoiceBox RoleBox;
    @FXML
    private TextField PhoneNumberTextField;
    @FXML
    private TextField AddressTextField;
    @FXML
    private TextField SetPasswordField;
    @FXML
    private Label AddMessageLabel;

    private Connection connectDB;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RoleBox.setItems(roleList);
        RoleBox.setValue("Choose a role here...");

    }


    public AddUserController() {
        // Constructeur par défaut
    }

    public void ajouterUtilisateur(ActionEvent event) {
        if (EmailAddressTextField.getText().isEmpty() ||
                FullNameTextField.getText().isEmpty() ||
                PhoneNumberTextField.getText().isEmpty() ||
                AddressTextField.getText().isEmpty() ||
                SetPasswordField.getText().isEmpty() ||
                RoleBox.getValue() == null) {


            AddMessageLabel.setText("Please fill in all fields.");
            AddMessageLabel.setStyle("-fx-text-fill: red;");

            return;
        }

        String password = SetPasswordField.getText();

        MyConnection connectNow = new MyConnection();
        Connection connectDB = connectNow.getConnection();


        String EmailAddress = EmailAddressTextField.getText();
        String FullName = FullNameTextField.getText();
        String PhoneNum = PhoneNumberTextField.getText();
        String Address = AddressTextField.getText();
        String Password = SetPasswordField.getText();
        String Role = (String) RoleBox.getValue();
        String hashedPassword = hashPassword(Password);



        String insertFields = "INSERT INTO user (email, lastname, numtel, adresse, password, roles) VALUES ('";
        String insertValues = EmailAddress + "','" + FullName + "','" + PhoneNum + "','" + Address + "','" + hashedPassword + "','" + Role + "')";
        String insertToRegister = insertFields + insertValues;

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate((insertToRegister));

            AddMessageLabel.setText("User added done!");
            AddMessageLabel.setStyle("-fx-text-fill: green;");


        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }


    }

    private String hashPassword(String password) {
        try {
            // Créer un objet de hachage avec SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Mettre le mot de passe dans l'objet de hachage
            byte[] hashBytes = digest.digest(password.getBytes());
            // Convertir le hachage en une chaîne hexadécimale
            String hashedPassword = Base64.getEncoder().encodeToString(hashBytes);
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            // Gérer les erreurs d'algorithme de hachage indisponible
            System.out.println("Algorithme de hachage non disponible.");
            e.printStackTrace();
            return null;
        }
    }


}
