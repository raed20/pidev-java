package controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;



import entities.Pret;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.LoanService;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class LoanController implements Initializable {




    @Override
    public void initialize(URL url, ResourceBundle resource) {


    }


    @FXML
    void deleteSelectedLoan(int idPret) {
        if (idPret != 0) {
            try {
                LoanService loanService = new LoanService();
                loanService.deleteLoan(idPret);
                showAlert("Success","Row deleted successfully");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Error","No row selected");
        }
    }


    @FXML
    void goToEdit(Pret pret , int bankid) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/FrontOffice/LoanFront/LoanForm.fxml"));
            Parent form = loader.load();
            LoanFormController controller = loader.getController();
            controller.setBankId(bankid);

            if (pret != null) {
                controller.setPret(pret); // If updating an existing loan
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(form));
            stage.setTitle(pret == null ? "Add a New loan": "Modify the loan");
            stage.showAndWait(); // Use showAndWait to refresh list after adding or updating

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}


