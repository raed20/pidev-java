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
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

public class LoanService implements ILoanService<Pret> {

    private Classifier classifier;
    Instances trainData;
    Instances test_data;
    public static Instances getInstances (String filename)
    {

        ConverterUtils.DataSource source;
        Instances dataset = null;
        try {
            source = new ConverterUtils.DataSource(filename);
            dataset = source.getDataSet();
            dataset.setClassIndex(dataset.numAttributes()-1);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        return dataset;
    }
    public static String getEmailById(int idUser) throws SQLException {
        String email = null;
        MyConnection myConnection = new MyConnection();

        try (Connection connection = myConnection.getConnection()) {
            String query = "SELECT email FROM user WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idUser);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        email = resultSet.getString("email");
                    }
                }
            }
        }
        return email;
    }

    public static String getBankNameById(int idBank) throws SQLException {
        String bankName = null;
        MyConnection myConnection = new MyConnection();

        try (Connection connection = myConnection.getConnection()) {
            String query = "SELECT nom FROM bank WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idBank);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        bankName = resultSet.getString("nom");

                    }
                }
            }
        }
        return bankName;
    }


    public LoanService() {
        try {
            this.trainData = getInstances("src/main/java/services/Train.arff");
            this.test_data = getInstances("src/main/java/services/Test.arff");

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
    public String addLoan(Pret pret) throws Exception {
        String toemail=null;
        MyConnection myConnection = new MyConnection();
        try (Connection connection = myConnection.getConnection()) {
            String query = "INSERT INTO pret (gender, married, dependents, education, self_employed, applicant_income, coapplicant_income, loan_amount, loan_amount_term, credit_history, property_area, loan_status, idBank, idUser) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                Scanner scanner = new Scanner(System.in);
                System.out.println(this.classifier);

                int idUser = 1;
                int bankId=1;
                toemail=getEmailById(idUser);
                /*System.out.println("user number : " + idUser);
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
                String propertyArea = scanner.nextLine();*/

                // Prepare an instance for prediction
                Instance instance = new DenseInstance(10);
                instance.setDataset(this.trainData);
                instance.setValue(0, pret.getGender());
                instance.setValue(1, pret.getMarried());
                // Skip setting dependents
                instance.setValue(2, pret.getEducation());
                instance.setValue(3, pret.getSelfEmployed());
                instance.setValue(4, pret.getApplicantIncome());
                instance.setValue(5, pret.getCoapplicantIncome());
                instance.setValue(6, pret.getLoanAmount());
                instance.setValue(7, pret.getLoanAmountTerm());
                instance.setValue(8, pret.getCreditHistory());
                instance.setValue(9, pret.getPropertyArea());

                // Apply any necessary preprocessing here
                System.out.println(instance);
                double predictedValue = this.classifier.classifyInstance(instance);
                String loanStatus = predictedValue == 1.0 ? "yes" : "no";
                System.out.println("PredictedValue: " + predictedValue);

                // Insert loan data into the database
                statement.setString(1, pret.getGender());
                statement.setString(2, pret.getMarried());
                statement.setInt(3, 0); // Set dependents to 0 as it was not read
                statement.setString(4, pret.getEducation());
                statement.setString(5, pret.getSelfEmployed());
                statement.setInt(6, pret.getApplicantIncome());
                statement.setInt(7, pret.getCoapplicantIncome());
                statement.setInt(8, pret.getLoanAmount());
                statement.setInt(9, pret.getLoanAmountTerm());
                statement.setInt(10, pret.getCreditHistory());
                statement.setString(11, pret.getPropertyArea());
                statement.setString(12, loanStatus);
                statement.setInt(13, bankId);
                statement.setInt(14, idUser);

                statement.executeUpdate();
                System.out.println("Pret ajouté");
                System.out.println("Loan Status Predicted: " + loanStatus);
                new GMailer().sendMail("Loan Application ", "Congratulations ! your loan application has been submitted successfully with status ."+loanStatus, toemail);
                return loanStatus;
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            myConnection.closeConnection();
        }



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
    public String updateLoan(int id, Pret pret) throws Exception {
        String toemail=null;
        MyConnection myConnection = new MyConnection();
        try (Connection connection = myConnection.getConnection()) {
            String query = "UPDATE pret SET gender = ?, married = ?, education = ?, self_employed = ?, applicant_income = ?, coapplicant_income = ?, loan_amount = ?, loan_amount_term = ?, credit_history = ?, property_area = ? , idBank= ? , loan_status= ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                int idUser = 1; // Assuming you're updating a specific loan for a specific user
                int bankId = 1; // Assuming you're updating a specific loan for a specific bank

                toemail=getEmailById(idUser);
                // Prepare an instance for prediction
                Instance instance = new DenseInstance(10);
                instance.setDataset(this.trainData);
                instance.setValue(0, pret.getGender());
                instance.setValue(1, pret.getMarried());
                // Skip setting dependents
                instance.setValue(2, pret.getEducation());
                instance.setValue(3, pret.getSelfEmployed());
                instance.setValue(4, pret.getApplicantIncome());
                instance.setValue(5, pret.getCoapplicantIncome());
                instance.setValue(6, pret.getLoanAmount());
                instance.setValue(7, pret.getLoanAmountTerm());
                instance.setValue(8, pret.getCreditHistory());
                instance.setValue(9, pret.getPropertyArea());

                // Apply any necessary preprocessing here
                System.out.println(instance);
                double predictedValue = this.classifier.classifyInstance(instance);
                String loanStatus = predictedValue == 1.0 ? "yes" : "no";
                System.out.println("PredictedValue: " + predictedValue);
                // Set values for the update statement
                statement.setString(1, pret.getGender());
                statement.setString(2, pret.getMarried());
                statement.setString(3, pret.getEducation());
                statement.setString(4, pret.getSelfEmployed());
                statement.setInt(5, pret.getApplicantIncome());
                statement.setInt(6, pret.getCoapplicantIncome());
                statement.setInt(7, pret.getLoanAmount());
                statement.setInt(8, pret.getLoanAmountTerm());
                statement.setInt(9, pret.getCreditHistory());
                statement.setString(10, pret.getPropertyArea());
                statement.setString(11, loanStatus);
                statement.setInt(12, bankId);
                statement.setInt(13, id); // Set the ID of the loan to update

                // Execute the update statement
                statement.executeUpdate();
                System.out.println("Pret modifié");
                new GMailer().sendMail("Loan Application ", "Your loan application has been updated successfully", toemail);
                return loanStatus;
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
            String query = "SELECT * FROM pret WHERE idUser=? ";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, 1);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Pret pret = new Pret();
                    pret.setId(resultSet.getInt("id"));
                    //pret.setGender(resultSet.getString("gender"));
                    //pret.setMarried(resultSet.getString("married"));
                    //pret.setDependents(resultSet.getInt("dependents"));
                    //pret.setEducation(resultSet.getString("education"));
                    //pret.setSelfEmployed(resultSet.getString("self_employed"));
                    pret.setApplicantIncome(resultSet.getInt("applicant_income"));
                    pret.setCoapplicantIncome(resultSet.getInt("coapplicant_income"));
                    pret.setLoanAmount(resultSet.getInt("loan_amount"));
                    pret.setLoanAmountTerm(resultSet.getInt("loan_amount_term"));
                    //pret.setCreditHistory(resultSet.getInt("credit_history"));
                    //pret.setPropertyArea(resultSet.getString("property_area"));
                    pret.setLoanStatus(resultSet.getString("loan_status"));
                    int bankId = resultSet.getInt("idBank");
                    String bankName = getBankNameById(bankId);
                    pret.setBankName(bankName);
                    pretList.add(pret);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pretList;
    }

}


