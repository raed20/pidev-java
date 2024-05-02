package controllers;

import controllers.LoanFormController;
import entities.Bank;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class CardController implements Initializable {

    @FXML
    private AnchorPane card_form;

    @FXML
    private Label bank_name;

    @FXML
    private Label bank_rate;

    @FXML
    private ImageView bank_imageView;

    @FXML
    private Spinner<Integer> bank_spinner;

    @FXML
    private Button bank_addBtn;

    private Bank bankData;
    private Image image;

    private Integer bankID;
    private String bank_image;

    private SpinnerValueFactory<Integer> spin;
    LoanFormController l = new LoanFormController();

    BankInterestRate bankRates = new BankInterestRate();


    public void setData(Bank bankData) {
        this.bankData = bankData;
        bank_image = bankData.getLogo();
        bankID = bankData.getId();
        bank_name.setText(bankData.getNom());
        String path = "File:" + bankData.getLogo();
        image = new Image(path, 190, 94, false, true);
        bank_imageView.setImage(image);
        double interestRate = bankRates.getInterestRate(bankData.getNom());
        bank_rate.setText(String.format("%.2f%%", interestRate));
    }


    public void selectBtn() {


        LoanController l = new LoanController();
        bank_addBtn.setOnAction(event -> {
            // Handle the selection event here
            if (bankData != null) {
                try {
                    l.goToEdit(null,bankData.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Handle case when no bank is selected
                System.out.println("No bank selected.");
            }
        });
    }

    public Bank getData() {
        return bankData;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}