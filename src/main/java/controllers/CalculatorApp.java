package controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalculatorApp extends Application {

    @FXML
    private TextField loanAmountInput, interestRateInput, loanTenureInput;
    @FXML
    private Label loanEMIValue, totalInterestValue, totalAmountValue;
    @FXML
    private PieChart pieChart;


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Loan EMI Calculator");

        loanAmountInput = new TextField("10000");
        interestRateInput = new TextField("7.5");
        loanTenureInput = new TextField("12");

        loanEMIValue = new Label();
        totalInterestValue = new Label();
        totalAmountValue = new Label();

        Button calculateBtn = new Button("Calculate");
        calculateBtn.setOnAction(e -> calculateEMI());

        pieChart = new PieChart();
        VBox vbox = new VBox(loanAmountInput, interestRateInput, loanTenureInput, calculateBtn,
                loanEMIValue, totalInterestValue, totalAmountValue, pieChart);

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        calculateEMI();
    }

    @FXML
    private void calculateEMI() {

        if (!validateInput()) {
        return;
        }
        double loanAmount = Double.parseDouble(loanAmountInput.getText());
        double interestRate = Double.parseDouble(interestRateInput.getText());
        int loanTenure = Integer.parseInt(loanTenureInput.getText());
        double interest = interestRate / 12 / 100;

        double emi = loanAmount * interest * (Math.pow(1 + interest, loanTenure) / (Math.pow(1 + interest, loanTenure) - 1));
        loanEMIValue.setText("Loan EMI: " + String.format("%.2f", emi));

        double totalAmount = loanTenure * emi;
        totalAmountValue.setText("Total Amount Payable: " + String.format("%.2f", totalAmount));

        double totalInterestPayable = totalAmount - loanAmount;
        totalInterestValue.setText("Total Interest Payable: " + String.format("%.2f", totalInterestPayable));

        pieChart.getData().clear();
        pieChart.getData().add(new PieChart.Data("Total Interest", totalInterestPayable));
        pieChart.getData().add(new PieChart.Data("Principal Loan Amount", loanAmount));
    }

    private boolean validateInput() {
        try {
            double loanAmount = Double.parseDouble(loanAmountInput.getText());
            if (loanAmount <= 0) {
                showAlert("Loan amount must be greater than zero");
                return false;
            }

            double interestRate = Double.parseDouble(interestRateInput.getText());
            if (interestRate <= 0) {
                showAlert("Interest rate must be greater than zero");
                return false;
            }

            int loanTenure = Integer.parseInt(loanTenureInput.getText());
            if (loanTenure <= 0) {
                showAlert("Loan tenure must be greater than zero");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            showAlert("Invalid input: Please enter numeric values");
            return false;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
