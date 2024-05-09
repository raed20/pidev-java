package controllers;

import entities.Utilisateurs;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Window;
import services.UtilisateurService;
import tools.MyConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import java.net.URL;

import javafx.scene.control.CheckBox;

import java.util.prefs.Preferences;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController implements Initializable {

    @FXML
    private Button RegistrationButton;
    @FXML
    private Button loginbtn;
    @FXML
    private Label LoginMessage;
    @FXML
    private ImageView ProSyncImageView;
    @FXML
    private TextField EmailAddressTextField;
    @FXML
    private PasswordField EnterPasswordField;
    @FXML
    private CheckBox rememberMeCheckBox;
    @FXML
    private ImageView passwordVisibilityIcon;

    private boolean passwordVisible = false;


    private Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
    private static final String REMEMBER_EMAIL_KEY = "rememberedEmail";
    private static final String REMEMBER_PASSWORD_KEY = "rememberedPassword";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        File ProSyncFile = new File("src/main/resources/Images/438032592_1466819873939024_8254370732506893689_n.jpg");
        Image ProSyncImage = new Image(ProSyncFile.toURI().toString());
        ProSyncImageView.setImage(ProSyncImage);

        String rememberedEmail = prefs.get(REMEMBER_EMAIL_KEY, null);
        String rememberedPassword = prefs.get(REMEMBER_PASSWORD_KEY, null);

        if (rememberedEmail != null && !rememberedEmail.isEmpty()) {
            EmailAddressTextField.setText(rememberedEmail);
            rememberMeCheckBox.setSelected(true);
        }
        if (rememberedPassword != null && !rememberedPassword.isEmpty()) {
            EnterPasswordField.setText(rememberedPassword);
        }
        passwordVisibilityIcon.setOnMouseClicked(event -> togglePasswordVisibility());
    }


    private void togglePasswordVisibility() {
        if (passwordVisible) {
            // Si le mot de passe est actuellement visible, le masquer
            EnterPasswordField.setManaged(true);
            EnterPasswordField.setVisible(true);
            passwordVisible = false;
        } else {
            // Si le mot de passe est actuellement masqué, le rendre visible
            EnterPasswordField.setManaged(false);
            EnterPasswordField.setVisible(false);
            passwordVisible = true;
        }
    }
    @FXML
    public void emailTextFieldOnKeyReleased(KeyEvent event) {
        String enteredEmail = EmailAddressTextField.getText();
        // Vérifie si une adresse e-mail correspondante est enregistrée dans les préférences
        String rememberedPassword = prefs.get(enteredEmail, null);
        if (rememberedPassword != null && !rememberedPassword.isEmpty()) {
            // Remplit automatiquement le champ de mot de passe avec le mot de passe correspondant
            EnterPasswordField.setText(rememberedPassword);
        } else {
            // Efface le champ de mot de passe si aucune correspondance n'est trouvée
            EnterPasswordField.clear();
        }
    }


    public void LoginButtonOnAction() {


        if (EmailAddressTextField.getText().isBlank() && EnterPasswordField.getText().isBlank()) {

            LoginMessage.setText("Please enter your Email and Password");
        } else if (EmailAddressTextField.getText().isBlank() == false && EnterPasswordField.getText().isBlank() == true){
            LoginMessage.setText("Please enter your Password");
        } else if (EmailAddressTextField.getText().isBlank() == true && EnterPasswordField.getText().isBlank() == false){
            LoginMessage.setText("Please enter your Email");
        } else {




            validateLogin();
        }
        boolean rememberMe = rememberMeCheckBox.isSelected();
        if (rememberMe) {
            // Enregistre l'adresse e-mail et le mot de passe dans les préférences si "Remember Me" est coché
            prefs.put(REMEMBER_EMAIL_KEY, EmailAddressTextField.getText());
            prefs.put(REMEMBER_PASSWORD_KEY, EnterPasswordField.getText());
        } else {
            // Supprime l'adresse e-mail et le mot de passe des préférences si "Remember Me" n'est pas coché
            prefs.remove(REMEMBER_EMAIL_KEY);
            prefs.remove(REMEMBER_PASSWORD_KEY);
        }

    }

    public void RegistrationButtonOnAction(ActionEvent event) {
        //Stage stage = (Stage) RegistrationButton.getScene().getWindow();
        //stage.close();
        createAccountForm(((Node) event.getSource()).getScene().getWindow());
    }

    public void validateLogin(){

        MyConnection connectNow = new MyConnection();
        Connection connectDB = connectNow.getConnection();

        String hashedPassword = hashPassword(EnterPasswordField.getText());

        String verifyLogin = "SELECT * FROM user WHERE email ='" + EmailAddressTextField.getText() + "' AND password ='" + hashedPassword + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);



                if (queryResult.next()) {
                    Utilisateurs utilisateurConnecte = new Utilisateurs();
                    utilisateurConnecte.setLastname(queryResult.getString("lastname"));
                    utilisateurConnecte.setNumtel(queryResult.getInt("numtel"));
                    utilisateurConnecte.setEmail(queryResult.getString("email"));
                    utilisateurConnecte.setAdresse(queryResult.getString("adresse"));
                    utilisateurConnecte.setRoles(queryResult.getString("roles"));

                    // récupérer l'utilisateur connecté depuis la base de données ou d'où vous stockez les utilisateurs
                    UtilisateurService.setUtilisateurConnecte(utilisateurConnecte);

                    LoginMessage.setText("You are Logged in!");
                    LoginMessage.setStyle("-fx-text-fill: green;");


                    // Load and display the homepage FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/FrontOffice/FrontSidebar.fxml"));
                    Parent root = loader.load();
                    Stage homepageStage = new Stage();
                    homepageStage.setTitle("Homepage");
                    homepageStage.setScene(new Scene(root));
                    homepageStage.show();

                    // Close the login window
                    Stage loginStage = (Stage) loginbtn.getScene().getWindow();
                    loginStage.close();





                } else {

                    LoginMessage.setText("Invalid login. try again !");
                }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();

        }


    }
    private void saveLoginCredentials(String email, String password) {
        // Code pour enregistrer les informations de connexion dans un emplacement sécurisé
    }

    private static String hashPassword(String password) {
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

    public void createAccountForm(Window previousWindow){
        try{

            Parent root = FXMLLoader.load(getClass().getResource("/javafx/FrontOffice/Login/registration.fxml"));
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

    public Utilisateurs getUtilisateurConnecte() {
        if (EmailAddressTextField == null) {
            return null; // Return null or handle the case appropriately
        }
        MyConnection connectNow = new MyConnection();
        Connection connectDB = connectNow.getConnection();

        Utilisateurs utilisateurConnecte = null;

        String email = EmailAddressTextField.getText();
        String hashedPassword = hashPassword(EnterPasswordField.getText());

        String query = "SELECT * FROM user WHERE email ='" + email + "' AND password ='" + hashedPassword + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                utilisateurConnecte = new Utilisateurs();
                utilisateurConnecte.setLastname(resultSet.getString("lastname"));
                utilisateurConnecte.setNumtel(resultSet.getInt("numtel"));
                utilisateurConnecte.setEmail(resultSet.getString("email"));
                utilisateurConnecte.setAdresse(resultSet.getString("adresse"));
                utilisateurConnecte.setRoles(resultSet.getString("roles"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return utilisateurConnecte;
    }
}
