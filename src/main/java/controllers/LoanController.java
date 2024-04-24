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


    @FXML
    private TableView<Pret> tableView;

    @FXML
    private TableColumn<Pret, Integer> idColumn;

    @FXML
    private TableColumn<Pret, Integer> loanAmountColumn;

    @FXML
    private TableColumn<Pret, Integer> loanTermColumn;

    @FXML
    private TableColumn<Pret, Integer> incomeColumn;

    @FXML
    private TableColumn<Pret, Integer> coIncomeColumn;

    @FXML
    private TableColumn<Pret, String> loanStatusColumn;

    @FXML
    private TableColumn<Pret, String> bankNameColumn;

    @FXML
    private Button edit;



    @Override
    public void initialize(URL url, ResourceBundle resource) {

        loanShow();

    }


    @FXML
    private void deleteSelectedLoan() {
        Pret selectedPret = tableView.getSelectionModel().getSelectedItem();
        if (selectedPret != null) {
            try {
                LoanService loanService = new LoanService();
                loanService.deleteLoan(selectedPret.getId());
                tableView.getItems().remove(selectedPret);
                System.out.println("Row deleted successfully");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No row selected");
        }
    }

    void loanShow() {
        tableView.getSelectionModel().clearSelection();
        LoanService l = new LoanService();

        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        incomeColumn.setCellValueFactory(new PropertyValueFactory<>("applicantIncome"));
        coIncomeColumn.setCellValueFactory(new PropertyValueFactory<>("coapplicantIncome"));
        loanAmountColumn.setCellValueFactory(new PropertyValueFactory<>("loanAmount"));
        loanTermColumn.setCellValueFactory(new PropertyValueFactory<>("loanAmountTerm"));
        loanStatusColumn.setCellValueFactory(new PropertyValueFactory<>("loanStatus"));

        // Configure the bankNameColumn
        bankNameColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(cellData.getValue().getBankName());
            } catch (Exception e) {
                e.printStackTrace();
                return new SimpleStringProperty("test");
            }
        });

        try {
            List<Pret> pretList = l.getDataLoan();
            ObservableList<Pret> data = FXCollections.observableArrayList(pretList);
            tableView.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Allow editing only if an item is selected
        edit.setOnAction(event -> {
            Pret selectedPret = tableView.getSelectionModel().getSelectedItem();
            if (selectedPret != null) {
                try {
                    goToEdit(l.getPretById(selectedPret.getId()), selectedPret.getIdBank());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Attention !");
                alert.setHeaderText(null);
                alert.setContentText("You need to select a loan first!");

                // Show the alert and wait for a button to be clicked
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        System.out.println("OK button clicked");
                    }
                });
            }
        });
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

}


