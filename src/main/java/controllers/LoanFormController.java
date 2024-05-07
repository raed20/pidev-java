package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.BankService;
import services.LoanService;
import entities.Pret;
import entities.Bank;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.util.StringConverter;

public class LoanFormController {

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
    private Label labelTitre;

    @FXML
    private Label resultLabel;

    @FXML
    private Label loanEMIValue;
    @FXML
    private Label totalInterestValue;
    @FXML
    private Label totalAmountValue;

    @FXML
    private Button boutonEnregistrer;

    private LoanService serviceLoan = new LoanService();

    private Pret pretActuel ;

    private int bankId; // Add a field to store the bank ID
    BankInterestRate bankRates = new BankInterestRate();




    public void initialize() {
        pretActuel=null ;
        updateFormTitle();
        initializeChoiceBoxes();
        boutonEnregistrer.setOnAction(event -> {
            try {
                gererEnregistrementPret();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void initializeChoiceBoxes() {
        genderChoiceBox.getItems().addAll("Male", "Female");
        marriedChoiceBox.getItems().addAll("Yes", "No");
        educationChoiceBox.getItems().addAll("Graduate", "Not Graduate");
        selfEmployedChoiceBox.getItems().addAll("Yes", "No");
        propertyAreaChoiceBox.getItems().addAll("Urban", "Rural", "Semiurban");
    }
    public void setPret(Pret p) {
        pretActuel = p;
        updateFormTitle();
        if (p != null) {
            if (genderChoiceBox != null) {
                genderChoiceBox.setValue(String.valueOf(p.getGender()));
            }

            if (marriedChoiceBox != null) {
                marriedChoiceBox.setValue(String.valueOf(p.getMarried()));
            }

            if (educationChoiceBox != null) {
                educationChoiceBox.setValue(String.valueOf(p.getEducation()));
            }

            if (selfEmployedChoiceBox != null) {
                selfEmployedChoiceBox.setValue(String.valueOf(p.getSelfEmployed()));
            }
            if (applicantIncomeTextField != null) {
                applicantIncomeTextField.setText(String.valueOf(p.getApplicantIncome()));
            }
            if (coapplicantIncomeTextField != null) {
                coapplicantIncomeTextField.setText(String.valueOf(p.getCoapplicantIncome()));
            }
            if (loanAmountTextField != null) {
                loanAmountTextField.setText(String.valueOf(p.getLoanAmount()));
            }
            if (loanAmountTermTextField != null) {
                loanAmountTermTextField.setText(String.valueOf(p.getLoanAmountTerm()));
            }

            creditHistoryCheckBox1.setSelected(p.getCreditHistory() == 1);

            if (propertyAreaChoiceBox != null) {
                propertyAreaChoiceBox.setValue(String.valueOf(p.getPropertyArea()));
            }else {
                clearForm();
            }
        }
    }

    private void updateFormTitle() {
        if (pretActuel == null) {
            labelTitre.setText("Add a new Loan");
        } else {
            labelTitre.setText("Edit a Loan");
        }
    }




    void gererEnregistrementPret() throws Exception {

        //validate choose the bank
        String bankname;
        BankService bankService=new BankService();



        if (!validerSaisie()) {
            return;
        }
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
        if (pretActuel == null) {
            Optional<Bank> optionalBank = bankService.findById(bankId);
            Bank bank = optionalBank.get();
            bankname = bank.getNom();
            double interestRate = bankRates.getInterestRate(bankname);
            pretActuel=new Pret();

            // Handle form submission
            pretActuel.setGender(gender);
            pretActuel.setMarried(married);
            pretActuel.setEducation(education);
            pretActuel.setSelfEmployed(selfEmployed);
            pretActuel.setApplicantIncome(applicantIncome);
            pretActuel.setCoapplicantIncome(coapplicantIncome);
            pretActuel.setLoanAmount(loanAmount);
            pretActuel.setLoanAmountTerm(loanAmountTerm);
            pretActuel.setCreditHistory(creditHistory ? 1 : 0); // Convert boolean to int
            pretActuel.setPropertyArea(propertyArea);


            String loanStatus = serviceLoan.addLoan(pretActuel,this.bankId);

            resultLabel.setText("Loan Status Predicted: " + loanStatus);
            if (loanStatus.equals("yes")){
                calculateLoan(interestRate,loanAmount,loanAmountTerm);
            }

            if (Objects.equals(loanStatus, "yes") || Objects.equals(loanStatus, "no")) {
                afficherAlerte(Alert.AlertType.INFORMATION, "Success", "Loan added successfully.");
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Error", "Adding loan failed.");
            }
        } else {
            pretActuel.setGender(gender);
            pretActuel.setMarried(married);
            pretActuel.setEducation(education);
            pretActuel.setSelfEmployed(selfEmployed);
            pretActuel.setApplicantIncome(applicantIncome);
            pretActuel.setCoapplicantIncome(coapplicantIncome);
            pretActuel.setLoanAmount(loanAmount);
            pretActuel.setLoanAmountTerm(loanAmountTerm);
            pretActuel.setCreditHistory(creditHistory ? 1 : 0); // Convert boolean to int
            pretActuel.setPropertyArea(propertyArea);

            String pretMisAJour = serviceLoan.updateLoan(pretActuel,serviceLoan.getBankIdByPretId(pretActuel.getId()));
            resultLabel.setText("Loan Status Predicted: " + pretMisAJour);
            if (pretMisAJour.equals("yes")){
                Optional<Bank> optionalBank = bankService.findById(serviceLoan.getBankIdByPretId(pretActuel.getId()));
                Bank bank = optionalBank.get();
                bankname = bank.getNom();
                double interestRate = bankRates.getInterestRate(bankname);
                calculateLoan(interestRate,loanAmount,loanAmountTerm);
            }

            if (Objects.equals(pretMisAJour, "yes") || Objects.equals(pretMisAJour, "no")) {
                afficherAlerte(Alert.AlertType.INFORMATION, "Success", "Loan updated successfully.");
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Error", "Loan update failed.");
            }
        }
        clearForm();

    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }



    private void afficherAlerte(Alert.AlertType typeAlerte, String titre, String contenu) {
        Alert alerte = new Alert(typeAlerte);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(contenu);
        alerte.showAndWait();
    }

    private void clearForm() {
        genderChoiceBox.setValue(null);
        marriedChoiceBox.setValue(null);
        educationChoiceBox.setValue(null);
        selfEmployedChoiceBox.setValue(null);
        applicantIncomeTextField.clear();
        coapplicantIncomeTextField.clear();
        loanAmountTextField.clear();
        loanAmountTermTextField.clear();
        creditHistoryCheckBox1.setSelected(false);
        propertyAreaChoiceBox.setValue(null);
    }

    private boolean validerSaisie() {
        String gender = genderChoiceBox.getValue();
        String married = marriedChoiceBox.getValue();
        String education = educationChoiceBox.getValue();
        String selfEmployed = selfEmployedChoiceBox.getValue();
        String applicantIncomeText = applicantIncomeTextField.getText();
        String coapplicantIncomeText = coapplicantIncomeTextField.getText();
        String loanAmountText = loanAmountTextField.getText();
        String loanAmountTermText = loanAmountTermTextField.getText();
        String propertyArea = propertyAreaChoiceBox.getValue();

        if (gender == null || married == null || education == null || selfEmployed == null ||
                applicantIncomeText.isEmpty() || coapplicantIncomeText.isEmpty() ||
                loanAmountText.isEmpty() || loanAmountTermText.isEmpty() || propertyArea == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Required fields", "Please complete all required fields.");
            return false;
        }

        try {
            int applicantIncome = Integer.parseInt(applicantIncomeText);
            int coapplicantIncome = Integer.parseInt(coapplicantIncomeText);
            int loanAmount = Integer.parseInt(loanAmountText);
            int loanAmountTerm = Integer.parseInt(loanAmountTermText);

            if (applicantIncome <= 0 || coapplicantIncome < 0 || loanAmount <= 0 || loanAmountTerm <= 0) {
                afficherAlerte(Alert.AlertType.WARNING, "Invalid values", "Please enter valid values for income and loan amounts.");
                return false;
            }
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.WARNING, "Invalid format", "Please enter valid numeric values for income and loan amounts.");
            return false;
        }

        return true;
    }

    public void textfieldDesign(){

        if(genderChoiceBox.isFocused()){

            genderChoiceBox.setStyle("-fx-border-width:2px; -fx-background-color:#fff");
            marriedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            educationChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            selfEmployedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            applicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            coapplicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTermTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            propertyAreaChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");

        }else if(marriedChoiceBox.isFocused()){

            genderChoiceBox.setStyle("-fx-border-width:2px; -fx-background-color:transparent");
            marriedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:#fff");
            educationChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            selfEmployedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            applicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            coapplicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTermTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            propertyAreaChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");

        }else if(educationChoiceBox.isFocused()){

            genderChoiceBox.setStyle("-fx-border-width:2px; -fx-background-color:transparent");
            marriedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            educationChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:#fff");
            selfEmployedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            applicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            coapplicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTermTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            propertyAreaChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");

        }else if(selfEmployedChoiceBox.isFocused()){

            genderChoiceBox.setStyle("-fx-border-width:2px; -fx-background-color:transparent");
            marriedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            educationChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            selfEmployedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:#fff");
            applicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            coapplicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTermTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            propertyAreaChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");

        }else if(applicantIncomeTextField.isFocused()){

            genderChoiceBox.setStyle("-fx-border-width:2px; -fx-background-color:transparent");
            marriedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            educationChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            selfEmployedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            applicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:#fff");
            coapplicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTermTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            propertyAreaChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
        }else if(coapplicantIncomeTextField.isFocused()){

            genderChoiceBox.setStyle("-fx-border-width:2px; -fx-background-color:transparent");
            marriedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            educationChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            selfEmployedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            applicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            coapplicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:#fff");
            loanAmountTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTermTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            propertyAreaChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
        }else if(loanAmountTextField.isFocused()){

            genderChoiceBox.setStyle("-fx-border-width:2px; -fx-background-color:transparent");
            marriedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            educationChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            selfEmployedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            applicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            coapplicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTextField.setStyle("-fx-border-width:1px; -fx-background-color:#fff");
            loanAmountTermTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            propertyAreaChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
        }else if(loanAmountTermTextField.isFocused()){

            genderChoiceBox.setStyle("-fx-border-width:2px; -fx-background-color:transparent");
            marriedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            educationChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            selfEmployedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            applicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            coapplicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTermTextField.setStyle("-fx-border-width:1px; -fx-background-color:#fff");
            propertyAreaChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
        }else if(propertyAreaChoiceBox.isFocused()){

            genderChoiceBox.setStyle("-fx-border-width:2px; -fx-background-color:transparent");
            marriedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            educationChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            selfEmployedChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            applicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            coapplicantIncomeTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            loanAmountTermTextField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            propertyAreaChoiceBox.setStyle("-fx-border-width:1px; -fx-background-color:#fff");
        }

    }

    private void calculateLoan(double interestRate , int loanAmount , int term ) {

        double monthlyInterestRate = interestRate / 100 / 12;
        double monthlyPayment = loanAmount * monthlyInterestRate * (Math.pow(1 + monthlyInterestRate, term) / (Math.pow(1 + monthlyInterestRate, term) - 1));
        double totalPayment = monthlyPayment * term;
        double totalInterest = totalPayment - loanAmount;

        // Update the UI with the calculated values
        loanEMIValue.setText(String.format("Monthly EMI: %.2f", monthlyPayment));
        totalInterestValue.setText(String.format("Total Interest: %.2f", totalInterest));
        totalAmountValue.setText(String.format("Total Amount: %.2f", totalPayment));
    }

}
