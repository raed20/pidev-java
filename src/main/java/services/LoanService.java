package services;

import entities.Pret;
import interfaces.ILoanService;
import tools.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import weka.classifiers.evaluation.Evaluation;
import weka.core.Instance;
import weka.core.DenseInstance;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

public class LoanService implements ILoanService<Pret> {

    private Classifier classifier;
    Instances trainData;
    Instances test_data;

    public LoanService() {
        try {
            this.trainData = LogR.getInstances("src/main/java/services/Train3.arff");
            this.test_data = LogR.getInstances("src/main/java/services/Test.arff");

            this.classifier = new weka.classifiers.functions.Logistic();
            this.classifier.buildClassifier(trainData);
            Evaluation eval = new Evaluation(trainData);
            eval.evaluateModel(this.classifier, test_data);
            trainData.setClassIndex(
                    trainData.numAttributes() - 1);

            // Initialize the ReplaceMissingValues filter
            Filter replaceMissingValues = new ReplaceMissingValues();
            replaceMissingValues.setInputFormat(trainData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addLoan(Pret pret) throws Exception {
        MyConnection myConnection = new MyConnection();
        try (Connection connection = myConnection.getConnection()) {
            String query = "INSERT INTO pret (gender, married, dependents, education, self_employed, applicant_income, coapplicant_income, loan_amount, loan_amount_term, credit_history, property_area, loan_status, idBank, idUser) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                Scanner scanner = new Scanner(System.in);
                System.out.println(this.classifier);

                int idUser = 1;
                System.out.println("user number : " + idUser);
                System.out.println("Enter id of bank:");
                int bankId = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter gender:");
                String gender = scanner.nextLine();

                System.out.println("Enter married (yes/no):");
                String married = scanner.nextLine();

                // Skip reading dependents as it's not used

                System.out.println("Enter education:");
                String education = scanner.nextLine();

                System.out.println("Enter self employed (yes/no):");
                String selfEmployed = scanner.nextLine();

                System.out.println("Enter applicant income:");
                int applicantIncome = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter coapplicant income:");
                int coapplicantIncome = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter loan amount:");
                int loanAmount = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter loan amount term:");
                int loanAmountTerm = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter credit history (0/1):");
                int creditHistory = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter property area:");
                String propertyArea = scanner.nextLine();

                // Prepare an instance for prediction
                Instance instance = new DenseInstance(10);
                instance.setDataset(this.trainData);
                instance.setValue(0, gender);
                instance.setValue(1, married);
                // Skip setting dependents
                instance.setValue(2, education);
                instance.setValue(3, selfEmployed);
                instance.setValue(4, applicantIncome);
                instance.setValue(5, coapplicantIncome);
                instance.setValue(6, loanAmount);
                instance.setValue(7, loanAmountTerm);
                instance.setValue(8, creditHistory);
                instance.setValue(9, propertyArea);

                // Apply any necessary preprocessing here
                System.out.println(instance);
                double predictedValue = this.classifier.classifyInstance(instance);
                String loanStatus = predictedValue == 1.0 ? "yes" : "no";
                System.out.println("PredictedValue: " + predictedValue);

                // Insert loan data into the database
                statement.setString(1, gender);
                statement.setString(2, married);
                statement.setInt(3, 0); // Set dependents to 0 as it was not read
                statement.setString(4, education);
                statement.setString(5, selfEmployed);
                statement.setInt(6, applicantIncome);
                statement.setInt(7, coapplicantIncome);
                statement.setInt(8, loanAmount);
                statement.setInt(9, loanAmountTerm);
                statement.setInt(10, creditHistory);
                statement.setString(11, propertyArea);
                statement.setString(12, loanStatus);
                statement.setInt(13, bankId);
                statement.setInt(14, idUser);

                statement.executeUpdate();
                System.out.println("Pret ajouté");
                System.out.println("Loan Status Predicted: " + loanStatus);
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            myConnection.closeConnection();
        }



        new GMailer().sendMail("Loan Application ", "Congratulations ! your loan application has been submitted successfully .");
    }


    @Override
    public void deleteLoan(int id) throws Exception {
        MyConnection myConnection = new MyConnection();
        try (Connection connection = myConnection.getConnection()) {
            String query = "DELETE FROM pret WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, id);
                statement.executeUpdate();
                System.out.println("Pret supprimé");
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            myConnection.closeConnection();
        }
    }

    @Override
    public void updateLoan(int id) throws Exception {
        MyConnection myConnection = new MyConnection();
        try (Connection connection = myConnection.getConnection()) {
            String query = "UPDATE pret SET gender=?, married=?, dependents=?, education=?, self_employed=?, applicant_income=?, coapplicant_income=?, loan_amount=?, loan_amount_term=?, credit_history=?, property_area=?, loan_status=? WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                Scanner scanner = new Scanner(System.in);

                System.out.println("Enter gender:");
                String gender = scanner.nextLine();

                System.out.println("Enter married (yes/no):");
                String married = scanner.nextLine();

                System.out.println("Enter number of dependents:");
                int dependents = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter education:");
                String education = scanner.nextLine();

                System.out.println("Enter self employed (yes/no):");
                String selfEmployed = scanner.nextLine();

                System.out.println("Enter applicant income:");
                int applicantIncome = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter coapplicant income:");
                int coapplicantIncome = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter loan amount:");
                int loanAmount = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter loan amount term:");
                int loanAmountTerm = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter credit history (0/1):");
                int creditHistory = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                System.out.println("Enter property area:");
                String propertyArea = scanner.nextLine();

                System.out.println("Enter loan status (yes/no):");
                String loanStatus = scanner.nextLine();

                statement.setString(1, gender);
                statement.setString(2, married);
                statement.setInt(3, dependents);
                statement.setString(4, education);
                statement.setString(5, selfEmployed);
                statement.setInt(6, applicantIncome);
                statement.setInt(7, coapplicantIncome);
                statement.setInt(8, loanAmount);
                statement.setInt(9, loanAmountTerm);
                statement.setInt(10, creditHistory);
                statement.setString(11, propertyArea);
                statement.setString(12, loanStatus);
                statement.setInt(13, id);

                statement.executeUpdate();
                System.out.println("Pret modifié");
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            myConnection.closeConnection();
        }
    }

    @Override
    public List<Pret> getDataLoan() throws Exception {
        List<Pret> pretList = new ArrayList<>();
        MyConnection myConnection = new MyConnection();
        try (Connection connection = myConnection.getConnection()) {
            String query = "SELECT * FROM pret";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Pret pret = new Pret();
                    pret.setId(resultSet.getInt("id"));
                    pret.setGender(resultSet.getString("gender"));
                    pret.setMarried(resultSet.getString("married"));
                    pret.setDependents(resultSet.getInt("dependents"));
                    pret.setEducation(resultSet.getString("education"));
                    pret.setSelfEmployed(resultSet.getString("self_employed"));
                    pret.setApplicantIncome(resultSet.getInt("applicant_income"));
                    pret.setCoapplicantIncome(resultSet.getInt("coapplicant_income"));
                    pret.setLoanAmount(resultSet.getInt("loan_amount"));
                    pret.setLoanAmountTerm(resultSet.getInt("loan_amount_term"));
                    pret.setCreditHistory(resultSet.getInt("credit_history"));
                    pret.setPropertyArea(resultSet.getString("property_area"));
                    pret.setLoanStatus(resultSet.getString("loan_status"));

                    pretList.add(pret);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pretList;
    }

}


