package services;

import entities.Bank;
import tools.MyConnection;
import interfaces.IBankService;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankService implements IBankService<Bank> {
    private Connection cnx;

    public BankService() {
        MyConnection myConnection = new MyConnection();
        cnx = myConnection.getConnection();
    }

    @Override
    public Bank save(Bank b) {
        String sql = "INSERT INTO bank (nom, adresse, code_swift,logo, phone_num) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getNom());
            ps.setString(2, b.getAdresse());
            ps.setString(3, b.getCodeSwift());
            ps.setString(4, b.getLogo());
            ps.setString(5, b.getPhoneNum());
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        b.setId(generatedKeys.getInt(1));
                        return b;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Bank> findById(int id) {
        String sql = "SELECT * FROM bank WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapToBank(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public  List<Bank> findAll() {
        List<Bank> banks = new ArrayList<>();
        String sql = "SELECT * FROM bank";
        try (Statement s = cnx.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                banks.add(mapToBank(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banks;
    }

    @Override
    public Bank update(Bank b) {
        String sql = "UPDATE bank SET nom = ?, adresse = ?, code_swift = ?, logo = ?, phone_num = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, b.getNom());
            ps.setString(2, b.getAdresse());
            ps.setString(3, b.getCodeSwift());
            ps.setString(4, b.getLogo());  // Include image
            ps.setString(5, b.getPhoneNum());

            ps.setInt(6, b.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                return b;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM bank WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Bank mapToBank(ResultSet rs) throws SQLException {
        return new Bank(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("adresse"),
                rs.getString("code_swift"),
                rs.getString("logo"), // Retrieve image from ResultSet
                rs.getString("phone_num")
        );
    }

    public boolean isBankExists(Bank b) {
        try {
            String query = "SELECT * FROM bank WHERE nom = ? AND adresse = ? AND code_swift = ? AND phone_num = ?";
            try (PreparedStatement ps = cnx.prepareStatement(query)) {
                ps.setString(1, b.getNom());
                ps.setString(2, b.getAdresse());
                ps.setString(3, b.getCodeSwift());
                ps.setString(4, b.getPhoneNum());
                ResultSet resultSet = ps.executeQuery();
                return resultSet.next(); // If a row is returned, the bank already exists
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


        @Override
    public String toString() {
        return "BankService{" +
                "cnx=" + cnx +
                '}';
    }
}
