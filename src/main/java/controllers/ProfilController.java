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
import java.sql.SQLException;
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
    UtilisateurService utilisateurService=new UtilisateurService();
    int userId;


   // Utilisateurs utilisateurConnecte= new Utilisateurs();



    // Autres champs de votre contrôleur

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoginController loginController = new LoginController();
        Utilisateurs utilisateurConnecte = UtilisateurService.getUtilisateurConnecte();



        if (utilisateurConnecte != null) {
            //this.utilisateurConnecte = utilisateurConnecte;
            userId=utilisateurConnecte.getId();
            System.out.println(userId);
            System.out.println(utilisateurConnecte.getEmail());
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

        if (utilisateurConnecte!= null) {
            try {
                System.out.println(userId);
                this.utilisateurService.deleteUtilisateur(utilisateurConnecte.getEmail());
// Appeler deleteUtilisateur sur l'instance de UtilisateurService
                UtilisateurService.setUtilisateurConnecte(null); // Définir l'utilisateur connecté sur null après la suppression
            } catch (SQLException e) {
                System.out.println("Erreur lors de la suppression du profil : " + e.getMessage());
                e.printStackTrace();
                // Gérer l'exception SQL de manière appropriée
            } catch (Exception e) {
                System.out.println("Erreur inattendue lors de la suppression du profil : " + e.getMessage());
                e.printStackTrace();
                // Gérer d'autres exceptions de manière appropriée
            }
        } else {
            System.out.println("L'utilisateur connecté est null. Impossible de supprimer le profil.");
        }
    }

}

