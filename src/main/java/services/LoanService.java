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

public class LoanService implements ILoanService<Pret> {
    @Override
    public void addLoan(Pret pret) throws Exception {
        MyConnection myConnection = new MyConnection();
        try (Connection connection = myConnection.getConnection()) {
            String query = "INSERT INTO pret (gender, married, dependents, education, self_employed, applicant_income, coapplicant_income, loan_amount, loan_amount_term, credit_history, property_area, loan_status , idBank , idUser) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                Scanner scanner = new Scanner(System.in);

                int idUser=1;
                System.out.println("user number : "+idUser);
                System.out.println("Enter id of bank:");
                int bankId = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

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
                statement.setInt(13, bankId);
                statement.setInt(14, idUser);


                statement.executeUpdate();
                System.out.println("Pret ajouté");
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            myConnection.closeConnection();
        }
        new GMailer().sendMail("Loan Application ","Congratulations ! your loan application has been submitted successfully .");

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


