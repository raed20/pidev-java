package controllers;

import entities.Pret;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import services.LoanService;
import tests.MainFX;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

    @FXML
    private VBox pnItems;

    @FXML
    private Button edit;

    @FXML
    private Button delete;
    @FXML
    private Button print;

    @FXML
    private Label bankname;
    @FXML
    private Label amount;
    @FXML
    private Label term;
    @FXML
    private Label status;

    private LoanService loanService = new LoanService();
    BankInterestRate bankRates = new BankInterestRate();


    private Pret selectedLoan;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            displayLoanItems();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }

    private void displayLoanItems() throws Exception {
        pnItems.getChildren().clear();

        for (Pret loanData : loanService.getDataLoan()) {
            HBox item = createLoanItem(loanData);
            item.setOnMouseClicked(event -> {
                selectedLoan = loanData;
                styleSelectedItem(item);
                // Assuming 'item' is the selected item in the VBox

            });
            pnItems.getChildren().add(item);
        }

        edit.setOnAction(event -> {
            LoanController l=new LoanController();
            if (this.selectedLoan != null) {
                try {
                    l.goToEdit(loanService.getPretById(selectedLoan.getId()), selectedLoan.getIdBank());
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

        delete.setOnAction(event -> {
            LoanController l=new LoanController();
            if (this.selectedLoan != null) {
                l.deleteSelectedLoan(selectedLoan.getId());
                try {
                    displayLoanItems();
                } catch (Exception e) {
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
        print.setOnAction(event -> {
            System.out.println("test");
            LoanController l = new LoanController();
            if (this.selectedLoan != null) {
                try {
                    // Generate PDF for selected loan details
                    l.generatePDF(selectedLoan.getId());
                } catch (Exception e) {
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

    private HBox createLoanItem(Pret loanData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/FrontOffice/LoanFront/LoanItem.fxml"));
            HBox item = loader.load();
            ItemController controller = loader.getController();
            controller.setLoanData(loanData);
            return item;
        } catch (IOException e) {
            e.printStackTrace();
            return new HBox(); // Return an empty HBox in case of error
        }
    }
    private void styleSelectedItem(HBox selectedItem) {
        for (Node node : pnItems.getChildren()) {
            if (node instanceof HBox) {
                HBox item = (HBox) node;
                if (item == selectedItem) {
                    item.setStyle("-fx-background-color: rgb(56, 116, 255);");
                    for (Node labelNode : item.getChildren()) {
                        if (labelNode instanceof Label) {
                            Label label = (Label) labelNode;
                            label.setTextFill(Color.WHITE);
                        }
                    }
                } else {
                    item.setStyle("-fx-background-color: transparent;");
                    for (Node labelNode : item.getChildren()) {
                        if (labelNode instanceof Label) {
                            Label label = (Label) labelNode;
                            label.setTextFill(Color.BLACK); // Set to the default color
                        }
                    }
                }
            }
        }
    }


    public void setLoanData(Pret LoanData) {

        bankname.setText(LoanData.getBankName());
        amount.setText(String.valueOf(LoanData.getLoanAmount()));
        term.setText(String.valueOf(LoanData.getLoanAmountTerm()));
        status.setText(LoanData.getLoanStatus());
    }


}
