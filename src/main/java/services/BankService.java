package services;

import entities.Bank;
import interfaces.IBankService;
import tools.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankService implements IBankService {

    @Override
    public void addBank(Object o) throws Exception {
            MyConnection myConnection = new MyConnection();
            try (Connection connection = myConnection.getConnection()) {
                String query = "INSERT INTO bank (nom, adresse, code_swift, logo, phone_num) " +
                        "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    Scanner scanner = new Scanner(System.in);

                    System.out.println("Enter bank name:");
                    String nom = scanner.nextLine();

                    System.out.println("Enter bank address:");
                    String adresse = scanner.nextLine();

                    System.out.println("Enter bank SWIFT code:");
                    String codeSwift = scanner.nextLine();

                    System.out.println("Enter bank logo:");
                    String logo = scanner.nextLine();

                    System.out.println("Enter bank phone number:");
                    String phoneNum = scanner.nextLine();

                    statement.setString(1, nom);
                    statement.setString(2, adresse);
                    statement.setString(3, codeSwift);
                    statement.setString(4, logo);
                    statement.setString(5, phoneNum);

                    statement.executeUpdate();
                    System.out.println("Bank added successfully");
                }
            } catch (SQLException e) {
                throw new Exception(e.getMessage());
            } finally {
                myConnection.closeConnection();
            }
    }

    @Override
    public void deleteBank(int id) throws Exception {
        MyConnection myConnection = new MyConnection();
        try (Connection connection = myConnection.getConnection()) {
            String query = "DELETE FROM bank WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, id);
                statement.executeUpdate();
                System.out.println("Bank supprimé");
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            myConnection.closeConnection();
        }
    }

    @Override
    public void updateBank(int id) throws Exception {
        MyConnection myConnection = new MyConnection();
        try (Connection connection = myConnection.getConnection()) {
            String query = "UPDATE bank SET Nom=?, adresse=?, code_swift=?, logo=?, phone_num=? WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                Scanner scanner = new Scanner(System.in);

                System.out.println("Enter bank name:");
                String nom = scanner.nextLine();

                System.out.println("Enter bank address:");
                String adresse = scanner.nextLine();

                System.out.println("Enter bank SWIFT code:");
                String codeSwift = scanner.nextLine();

                System.out.println("Enter bank logo:");
                String logo = scanner.nextLine();

                System.out.println("Enter bank phone number:");
                String phoneNum = scanner.nextLine();

                scanner.nextLine(); // Consume the newline character

                statement.setString(1, nom);
                statement.setString(2, adresse);
                statement.setString(3, codeSwift);
                statement.setString(4, logo);
                statement.setString(5, phoneNum);
                statement.setInt(6, id);

                statement.executeUpdate();
                System.out.println("Bank information updated");
            }
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } finally {
            myConnection.closeConnection();
        }

    }

    @Override
    public List getDataBank() throws Exception {
        List<Bank> bankList = new ArrayList<>();
        MyConnection myConnection = new MyConnection();
        try (Connection connection = myConnection.getConnection()) {
            String query = "SELECT * FROM bank";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Bank bank = new Bank();
                    bank.setId(resultSet.getInt("id"));
                    bank.setNom(resultSet.getString("Nom"));
                    bank.setAdresse(resultSet.getString("adresse"));
                    bank.setCodeSwift(resultSet.getString("code_swift"));
                    bank.setLogo(resultSet.getString("logo"));
                    bank.setPhoneNum(resultSet.getString("phone_num"));

                    bankList.add(bank);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bankList;

    }
}
