package controllers;

import entities.Utilisateurs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import services.UtilisateurService;
import tools.MyConnection;


import java.io.IOException;
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
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button delbtn;
    @FXML
    private ImageView selectedImageLabel;
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
            FullNameTextField.setText(utilisateurConnecte.getLastname());
            PhoneNumberTextField.setText(String.valueOf(utilisateurConnecte.getNumtel()));
            EmailAddressTextField.setText(utilisateurConnecte.getEmail());
            AddressTextField.setText(utilisateurConnecte.getAdresse());
            RoleBox.setValue(utilisateurConnecte.getRoles());

            String imagePath = utilisateurConnecte.getImage(); // Assurez-vous que getImagePath() renvoie le chemin d'accès correct
            if (imagePath != null) {
                Image image = new Image(imagePath);
                selectedImageLabel.setImage(image);
            } else {
                System.out.println("Le chemin d'accès à l'image est null.");
            }
            // Update other labels with additional user profile information
        } else {
            // Handle the case where utilisateurConnecte is null
        }
    }







    public void editerProfil(ActionEvent event) {
        Utilisateurs utilisateurConnecte = UtilisateurService.getUtilisateurConnecte();
        if (utilisateurConnecte != null) {
            // Populate input fields with current user information

            // Implement logic to allow user to modify information
            // For example, enable text fields for editing

            // Update user's information in the database when changes are confirmed
            try {
                utilisateurConnecte.setLastname(FullNameTextField.getText());
                utilisateurConnecte.setNumtel(Integer.parseInt(PhoneNumberTextField.getText()));
                utilisateurConnecte.setEmail(EmailAddressTextField.getText());
                utilisateurConnecte.setAdresse(AddressTextField.getText());
                utilisateurConnecte.setRoles( RoleBox.getValue());
                System.out.println(utilisateurConnecte.getEmail());

                UtilisateurService service = new UtilisateurService();
                service.updateUtilisateur(utilisateurConnecte);

            } catch (SQLException e) {
                System.out.println("Erreur lors de la mise à jour du profil : " + e.getMessage());
                e.printStackTrace();
                // Handle SQL exception
            } catch (NumberFormatException e) {
                System.out.println("Le numéro de téléphone doit être un entier.");
                // Handle number format exception for phone number
            }
        } else {
            System.out.println("L'utilisateur connecté est null. Impossible de mettre à jour le profil.");
        }
    }


    public void supprimerProfil(ActionEvent event) {
        Utilisateurs utilisateurConnecte = UtilisateurService.getUtilisateurConnecte();

        if (utilisateurConnecte != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer le profil");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer votre profil ?");

            // Customize the buttons in the alert
            ButtonType buttonTypeYes = new ButtonType("Oui");
            ButtonType buttonTypeNo = new ButtonType("Non");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // Show the alert and wait for the user's response
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == buttonTypeYes) {
                    // User clicked "Yes", delete the profile
                    try {
                        System.out.println(userId);
                        this.utilisateurService.deleteUtilisateur(utilisateurConnecte.getEmail());
                        UtilisateurService.setUtilisateurConnecte(null);
                        System.out.println("Profil supprimé avec succès.");
                        logout();

                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la suppression du profil : " + e.getMessage());
                        e.printStackTrace();
                        // Handle SQL exception
                    } catch (Exception e) {
                        System.out.println("Erreur inattendue lors de la suppression du profil : " + e.getMessage());
                        e.printStackTrace();
                        // Handle other exceptions
                    }
                } else {
                    // User clicked "No" or closed the alert
                    System.out.println("Suppression du profil annulée.");
                }
            });
        } else {
            System.out.println("L'utilisateur connecté est null. Impossible de supprimer le profil.");
        }
    }

    private void logout() {
        try {
            // Load the login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/FrontOffice/Login/login.fxml"));
            Parent root = loader.load();

            Stage homepageStage = new Stage();
            homepageStage.setTitle("Homepage");
            homepageStage.setScene(new Scene(root));
            homepageStage.show();

            // Get the current stage
            Stage stage = (Stage) delbtn.getScene().getWindow();

            stage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    }



