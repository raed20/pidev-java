package controllers;

import controllers.LoginController;
import entities.Utilisateurs;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfilController implements Initializable {


    @FXML
    private TextField EmailAddressTextField;
    @FXML
    private TextField FullNameTextField;
    @FXML
    private TextField PhoneNumberTextField;
    @FXML
    private TextField AddressTextField;
    @FXML
    private ChoiceBox<String> RoleBox;
   // @FXML
    //private Label selectedImageLabel;




    // Autres champs de votre contrôleur

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoginController loginController = new LoginController();
        // Accédez aux données de l'utilisateur connecté
        Utilisateurs utilisateurConnecte = loginController.getUtilisateurConnecte(); // Récupérez les données de l'utilisateur connecté depuis votre application

                // Mettez à jour les libellés avec les données du profil de l'utilisateur
                FullNameTextField.setText(utilisateurConnecte.getLastname());
                PhoneNumberTextField.setText(String.valueOf(utilisateurConnecte.getNumtel()));
                EmailAddressTextField.setText(utilisateurConnecte.getEmail());
                AddressTextField.setText(utilisateurConnecte.getAdresse());
                RoleBox.setValue(utilisateurConnecte.getRoles());
                //selectedImageLabel.setText(utilisateurConnecte.get);
        // Mettez à jour d'autres libellés avec d'autres informations du profil de l'utilisateur
    }

}
