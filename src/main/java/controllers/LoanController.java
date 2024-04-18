package controllers;


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
import javafx.stage.Stage;
import services.LoanService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class LoanController {


    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private ChoiceBox<String> marriedChoiceBox;

    @FXML
    private ChoiceBox<String> educationChoiceBox;

    @FXML
    private ChoiceBox<String> selfEmployedChoiceBox;

    @FXML
    private TextField applicantIncomeTextField;

    @FXML
    private TextField coapplicantIncomeTextField;

    @FXML
    private TextField loanAmountTextField;

    @FXML
    private TextField loanAmountTermTextField;

    @FXML
    private CheckBox creditHistoryCheckBox1;

    @FXML
    private ChoiceBox<String> propertyAreaChoiceBox;

    @FXML
    private Label resultLabel;

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
    public void initialize() {
        // Initialize choice boxes with options
        genderChoiceBox.getItems().addAll("Male", "Female");
        marriedChoiceBox.getItems().addAll("Yes", "No");
        educationChoiceBox.getItems().addAll("Graduate", "Not Graduate");
        selfEmployedChoiceBox.getItems().addAll("Yes", "No");
        propertyAreaChoiceBox.getItems().addAll("Urban", "Rural", "Semiurban");


        LoanService l= new LoanService();

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // or SelectionMode.MULTIPLE if you want to allow multiple selection

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        incomeColumn.setCellValueFactory(new PropertyValueFactory<>("applicantIncome"));
        coIncomeColumn.setCellValueFactory(new PropertyValueFactory<>("coapplicantIncome"));
        loanAmountColumn.setCellValueFactory(new PropertyValueFactory<>("loanAmount"));
        loanTermColumn.setCellValueFactory(new PropertyValueFactory<>("loanAmountTerm"));
        loanStatusColumn.setCellValueFactory(new PropertyValueFactory<>("loanStatus"));

        // Configurez la colonne pour afficher le nom de la banque
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

        // Set default values or handle initializations
    }

    @FXML
    private void handleLoanAdd() {
        // Retrieve values from form components
        String gender = genderChoiceBox.getValue();
        String married = marriedChoiceBox.getValue();
        String education = educationChoiceBox.getValue();
        String selfEmployed = selfEmployedChoiceBox.getValue();
        int applicantIncome = Integer.parseInt(applicantIncomeTextField.getText());
        int coapplicantIncome = Integer.parseInt(coapplicantIncomeTextField.getText());
        int loanAmount = Integer.parseInt(loanAmountTextField.getText());
        int loanAmountTerm = Integer.parseInt(loanAmountTermTextField.getText());
        boolean creditHistory = creditHistoryCheckBox1.isSelected();
        String propertyArea = propertyAreaChoiceBox.getValue();

        // Handle form submission
        Pret pret = new Pret();
        pret.setGender(gender);
        pret.setMarried(married);
        pret.setEducation(education);
        pret.setSelfEmployed(selfEmployed);
        pret.setApplicantIncome(applicantIncome);
        pret.setCoapplicantIncome(coapplicantIncome);
        pret.setLoanAmount(loanAmount);
        pret.setLoanAmountTerm(loanAmountTerm);
        pret.setCreditHistory(creditHistory ? 1 : 0); // Convert boolean to int
        pret.setPropertyArea(propertyArea);

        try {
            LoanService l = new LoanService();
            String loanStatus = l.addLoan(pret);
            resultLabel.setText("Loan Status Predicted: " + loanStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Now you can use these values for further processing or saving to a database
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

    @FXML
    private void handleLoanUpdate() {

        genderChoiceBox.getItems().addAll("Male", "Female");
        marriedChoiceBox.getItems().addAll("Yes", "No");
        educationChoiceBox.getItems().addAll("Graduate", "Not Graduate");
        selfEmployedChoiceBox.getItems().addAll("Yes", "No");
        propertyAreaChoiceBox.getItems().addAll("Urban", "Rural", "Semiurban");
        // Retrieve values from form components
        String gender = genderChoiceBox.getValue();
        String married = marriedChoiceBox.getValue();
        String education = educationChoiceBox.getValue();
        String selfEmployed = selfEmployedChoiceBox.getValue();
        int applicantIncome = Integer.parseInt(applicantIncomeTextField.getText());
        int coapplicantIncome = Integer.parseInt(coapplicantIncomeTextField.getText());
        int loanAmount = Integer.parseInt(loanAmountTextField.getText());
        int loanAmountTerm = Integer.parseInt(loanAmountTermTextField.getText());
        boolean creditHistory = creditHistoryCheckBox1.isSelected();
        String propertyArea = propertyAreaChoiceBox.getValue();

        // Handle form submission
        Pret pret = new Pret();
        pret.setGender(gender);
        pret.setMarried(married);
        pret.setEducation(education);
        pret.setSelfEmployed(selfEmployed);
        pret.setApplicantIncome(applicantIncome);
        pret.setCoapplicantIncome(coapplicantIncome);
        pret.setLoanAmount(loanAmount);
        pret.setLoanAmountTerm(loanAmountTerm);
        pret.setCreditHistory(creditHistory ? 1 : 0); // Convert boolean to int
        pret.setPropertyArea(propertyArea);

        try {
            LoanService l = new LoanService();
            String loanStatus = l.updateLoan(pret.getId(), pret);
            resultLabel.setText("Loan Status Predicted: " + loanStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Now you can use these values for further processing or saving to a database
    }

    @FXML
    private void goToEdit() {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/LoanFront/LoanEdit.fxml")));


            // Create a new stage for the edit form
            Stage stage = new Stage();
            stage.setTitle("Edit Loan");
            stage.setScene(new Scene(root));

            // Show the stage
            stage.show();
            // Close the current stage (optional)
            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
