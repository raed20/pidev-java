package controllers;

import entities.Utilisateurs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import services.UtilisateurService;
import tools.MyConnection;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class ProfilController implements Initializable {


   // @FXML
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
        Utilisateurs utilisateurConnecte = UtilisateurService.getUtilisateurConnecte();

        if (utilisateurConnecte != null) {
            FullNameTextField.setText(utilisateurConnecte.getLastname());
            PhoneNumberTextField.setText(String.valueOf(utilisateurConnecte.getNumtel()));
            EmailAddressTextField.setText(utilisateurConnecte.getEmail());
            AddressTextField.setText(utilisateurConnecte.getAdresse());
            RoleBox.setValue(utilisateurConnecte.getRoles());
            // Update other labels with additional user profile information
        } else {
            // Handle the case where utilisateurConnecte is null
        }
    }







    public void editerProfil(ActionEvent event) {
        // Implémentez ici la logique pour éditer le profil de l'utilisateur
        System.out.println("Édition du profil en cours...");
        // Vous pouvez ouvrir une nouvelle fenêtre pour permettre à l'utilisateur de modifier ses informations, par exemple.
    }

    public void supprimerProfil(ActionEvent event) {

        Utilisateurs utilisateurConnecte = UtilisateurService.getUtilisateurConnecte();
        if (utilisateurConnecte != null) {
            try {
                MyConnection myConnection = new MyConnection();
                Connection connection = myConnection.getConnection();
                // Vérifier si la connexion existe déjà
                if (connection != null) {
                    // Supprimer le profil de l'utilisateur de la base de données
                    int userId = utilisateurConnecte.getId(); // Récupérer l'ID de l'utilisateur connecté

                    String query = "DELETE FROM user WHERE id = ?";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, userId);
                        statement.executeUpdate();
                    }
                    UtilisateurService.setUtilisateurConnecte(null); // Appeler deleteUtilisateur sur l'instance de UtilisateurService
                } else {
                    System.out.println("La connexion à la base de données n'a pas pu être établie.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Gérer l'exception
            }
        }
    }
}

