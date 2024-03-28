package services;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Opportunite;
import entities.Investissement;
import interfaces.IInvestissement;
import tools.MyConnection;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvestissementService implements IInvestissement {
    private MyConnection connection;
    private static final Logger LOGGER = Logger.getLogger(MyConnection.class.getName());


    public InvestissementService(MyConnection connection) {
        this.connection = connection;
    }

    @Override
    public void addInvestissement(Investissement investissement) {
        String query = "INSERT INTO investissement (montant, date_invest, total_value, stock_name, changerate, price, opport_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, investissement.getMontant());
            statement.setDate(2, new java.sql.Date(investissement.getDateInvest().getTime()));
            statement.setFloat(3, investissement.getTotalValue());
            statement.setString(4, investissement.getStockName());
            statement.setFloat(5, investissement.getChangerate());
            statement.setFloat(6, investissement.getPrice());
            statement.setInt(7, investissement.getOpport());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
    }

    @Override
    public void deleteInvestissement(int id) {
        String query = "DELETE FROM investissement WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
    }

    @Override
    public void updateInvestissement(Investissement investissement) {
        String query = "UPDATE investissement SET montant = ?, date_invest = ?, total_value = ?, stock_name = ?, changerate = ?, price = ?, opport_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, investissement.getMontant());
            statement.setDate(2, new java.sql.Date(investissement.getDateInvest().getTime()));
            statement.setFloat(3, investissement.getTotalValue());
            statement.setString(4, investissement.getStockName());
            statement.setFloat(5, investissement.getChangerate());
            statement.setFloat(6, investissement.getPrice());
            statement.setInt(7, investissement.getOpport());
            statement.setInt(8, investissement.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
    }

    @Override
    public List<Investissement> getAllInvestissements() {
        List<Investissement> investissements = new ArrayList<>();
        String query = "SELECT * FROM investissement";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Investissement investissement = new Investissement();
                investissement.setId(resultSet.getInt("id"));
                investissement.setMontant(resultSet.getLong("montant"));
                investissement.setDateInvest(resultSet.getDate("date_invest"));
                investissement.setTotalValue(resultSet.getFloat("total_value"));
                investissement.setStockName(resultSet.getString("stock_name"));
                investissement.setChangerate(resultSet.getFloat("changerate"));
                investissement.setPrice(resultSet.getFloat("price"));
                // Fetch Opportunite entity for this Investissement
                OpportuniteService opportuniteService = new OpportuniteService(connection);
                investissement.setOpport(opportuniteService.getOpportuniteById(resultSet.getInt("opport_id")).getId());
                investissements.add(investissement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
        return investissements;
    }
    @Override
    public List<Investissement> getOpportuniteByInvestissementId(int opportuniteId) {
        String query = "SELECT * FROM opportunite WHERE id = (SELECT opport_id FROM investissement WHERE id = ?)";
        List<Investissement> investissements = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, opportuniteId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Opportunite opportunite = new Opportunite();
                    opportunite.setId(resultSet.getInt("id"));
                    opportunite.setDescription(resultSet.getString("description"));
                    Investissement investissement = new Investissement();
                    investissement.setOpport(opportunite.getId());
                    investissements.add(investissement);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
        return investissements;
    }
}
