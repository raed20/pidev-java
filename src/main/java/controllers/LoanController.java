package controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class LoanController implements Initializable {


    LoanService loanService=new LoanService();

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

    public void generatePDF(int loanId) throws Exception {
        // Get the selected loan details
        Pret selectedLoan = loanService.getPretById(loanId);

        // Create a PDF document
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Loan_details.pdf"));
        document.open();

        // Add selected loan details to the PDF document
        document.add(new Paragraph("Selected Loan Details:"));
        document.add(new Paragraph("Loan ID: " + selectedLoan.getId()));
        document.add(new Paragraph("Gender: " + selectedLoan.getGender()));
        document.add(new Paragraph("Married: " + selectedLoan.getMarried()));
        document.add(new Paragraph("Education: " + selectedLoan.getEducation()));
        document.add(new Paragraph("Self Employed: " + selectedLoan.getSelfEmployed()));
        document.add(new Paragraph("Applicant Income: " + selectedLoan.getApplicantIncome()));
        document.add(new Paragraph("Coapplicant Income: " + selectedLoan.getCoapplicantIncome()));
        document.add(new Paragraph("Loan Amount: " + selectedLoan.getLoanAmount()));
        document.add(new Paragraph("Loan Amount Term: " + selectedLoan.getLoanAmountTerm()));
        document.add(new Paragraph("Loan Status: " + selectedLoan.getLoanStatus()));
        // Add more loan details as needed

        document.close();
        File file = new File("Loan_details.pdf");
        Desktop.getDesktop().open(file);
    }




}


