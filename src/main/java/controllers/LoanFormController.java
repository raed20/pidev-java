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
    private Button boutonEnregistrer;

    private LoanService serviceLoan = new LoanService();

    private Pret pretActuel;

    private int bankId; // Add a field to store the bank ID




    public void initialize() {
        //chargerCampagnes();
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
            labelTitre.setText("Ajouter un Nouveau Don");
        } else {
            labelTitre.setText("Modifier le Don");
        }
    }

    /*private Bank findCampagneById(Integer campagneId) {
        return campagneId == null ? null : BankService.findById(campagneId).orElse(null);
    }*/

    /*private void chargerCampagnes() {
        List<Bank> banks = BankService.findAll();
        comboCampagne.setItems(FXCollections.observableArrayList(campagnes));
        comboCampagne.setConverter(new StringConverter<Campagne>() {
            @Override
            public String toString(Campagne campagne) {
                return campagne == null ? null : campagne.getTitre();
            }
            @Override
            public Campagne fromString(String string) {
                return null;
            }
        });
    }*/

    void gererEnregistrementPret() throws Exception {
       /* if (!validerSaisie()) {
            afficherAlerte(Alert.AlertType.WARNING, "Validation échouée", "Veuillez corriger les champs invalides.");
            return;
        }*/
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
            // Campagne selectedCampagne = comboCampagne.getSelectionModel().getSelectedItem();


            String loanStatus = serviceLoan.addLoan(pretActuel,this.bankId);

            resultLabel.setText("Loan Status Predicted: " + loanStatus);

            if (Objects.equals(loanStatus, "yes") || Objects.equals(loanStatus, "no")) {
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Don ajouté avec succès.");
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'ajout du don a échoué.");
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

            String pretMisAJour = serviceLoan.updateLoan(pretActuel);
            resultLabel.setText("Loan Status Predicted: " + pretMisAJour);

            if (Objects.equals(pretMisAJour, "yes") || Objects.equals(pretMisAJour, "no")) {
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Don mis à jour avec succès.");
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La mise à jour du don a échoué.");
            }
        }
        clearForm();

    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }


    /*private boolean validerSaisie() {
        String errorMessage = "";
        if (champType.getText().isEmpty()) {
            errorMessage += "Le type ne peut pas être vide.\n";
        }
        if (selecteurDateRemise.getValue() == null) {
            errorMessage += "La date de remise est requise.\n";
        }
        if (!errorMessage.isEmpty()) {
            afficherAlerte(Alert.AlertType.WARNING, "Saisie invalide", errorMessage);
            return false;
        }
        return true;
    }*/

    /*private Integer essayerParserInt(String texte) {
        try {
            return Integer.parseInt(texte);
        } catch (NumberFormatException e) {
            return null;
        }
    }*/

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
}
