package controllers;

import entities.Utilisateurs;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import services.UtilisateurService;
import tools.MyConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewAllUsersController implements Initializable {

    @FXML
    private TableView<Utilisateurs> usersTableView;

    @FXML
    private TableColumn<Utilisateurs, String> lastnameColumn;

    @FXML
    private TableColumn<Utilisateurs, String> emailColumn;

    @FXML
    private TableColumn<Utilisateurs, String> rolesColumn;

    @FXML
    private TableColumn<Utilisateurs, Integer> numtelColumn;

    @FXML
    private TableColumn<Utilisateurs, String> addressColumn;

    @FXML
    private TableColumn<Utilisateurs, Utilisateurs> otherColumn;

    private UtilisateurService utilisateurService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MyConnection myConnection = new MyConnection();
        Connection connection = myConnection.getConnection();
        utilisateurService = new UtilisateurService(connection);
        try {
            List<Utilisateurs> usersList = utilisateurService.getData();
            usersTableView.getItems().addAll(usersList);

            // Configuration des cellules des colonnes pour extraire les valeurs des propriétés
            lastnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastname()));
            emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
            rolesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoles()));
            numtelColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumtel()).asObject());
            addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
            otherColumn.setCellValueFactory(new PropertyValueFactory<>(""));
            otherColumn.setCellFactory(new Callback<>() {
                @Override
                public TableCell<Utilisateurs, Utilisateurs> call(TableColumn<Utilisateurs, Utilisateurs> param) {
                    return new TableCell<>() {
                        final Button deleteButton = new Button("Supprimer");
                        final Button editButton = new Button("Modifier");

                        {
                            // Gestionnaire d'événements pour le bouton Supprimer
                            deleteButton.setOnAction(event -> {
                                Utilisateurs user = getTableView().getItems().get(getIndex());
                                // Appelez votre méthode de suppression ici en utilisant l'objet Utilisateurs "user"
                                // Par exemple : utilisateurService.deleteUtilisateur(user.getId());
                                // Réactualisez la table après la suppression
                                usersTableView.getItems().remove(user);
                            });

                            // Gestionnaire d'événements pour le bouton Modifier
                            editButton.setOnAction(event -> {
                                Utilisateurs user = getTableView().getItems().get(getIndex());
                                // Appelez votre méthode de modification ici en utilisant l'objet Utilisateurs "user"
                                // Par exemple : showEditDialog(user);
                            });
                        }

                        @Override
                        protected void updateItem(Utilisateurs item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setGraphic(null);
                            } else {
                                // Affectez les boutons à la cellule
                                setGraphic(deleteButton);
                                setGraphic(editButton);
                            }
                        }
                    };
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
