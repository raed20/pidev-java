package controllers;

import entities.Utilisateurs;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import java.io.IOException;

import services.UtilisateurService;

public class cellfactorydashsmall implements Callback<ListView<Utilisateurs>, ListCell<Utilisateurs>> {

    private final UtilisateurService utilisateurService;
    private final AnchorPane anchorPane; // Reference to the AnchorPane

    public cellfactorydashsmall(UtilisateurService utilisateurService, AnchorPane anchorPane) {
        this.utilisateurService = utilisateurService;
        this.anchorPane = anchorPane; // Initialize the AnchorPane reference
    }

    @Override
    public ListCell<Utilisateurs> call(ListView<Utilisateurs> param) {
        return new ListCell<>() {
            private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/BackOffice/investissement/recentcustomerslistviewcollum.fxml"));

            {
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @FXML
            Label nameLabel;
            @FXML
            private AnchorPane listviewCollum;

            @Override
            protected void updateItem(Utilisateurs item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    nameLabel.setText(item.getLastname());
                    setGraphic(listviewCollum);                }
            }
        };
    }
}
