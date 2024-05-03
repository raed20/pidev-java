package services;
import entities.Opportunite;
import interfaces.IOpportunite;
import tools.MyConnection;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class OpportuniteService implements interfaces.IOpportunite {
    private static final Logger logger = Logger.getLogger(OpportuniteService.class.getName());
    private MyConnection connection;

    public OpportuniteService(MyConnection connection) {
        this.connection = connection;
    }

    @Override
    public void addOpportunite(Opportunite opportunite) {
        String query = "INSERT INTO opportunite (description, prix, name, lastprice, yesterdaychange, marketcap) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, opportunite.getDescription());
            statement.setFloat(2, opportunite.getPrix());
            statement.setString(3, opportunite.getName());
            statement.setFloat(4, opportunite.getLastprice());
            statement.setFloat(5, opportunite.getYesterdaychange());
            statement.setFloat(6, opportunite.getMarketcap());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred: ", e);
        }
    }

    @Override
    public void deleteOpportunite(int id) {
        String query = "DELETE FROM opportunite WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred: ", e);
        }
    }

    @Override
    public void updateOpportunite(Opportunite opportunite) {
        String query = "UPDATE opportunite SET description = ?, prix = ?, name = ?, lastprice = ?, yesterdaychange = ?, marketcap = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, opportunite.getDescription());
            statement.setFloat(2, opportunite.getPrix());
            statement.setString(3, opportunite.getName());
            statement.setFloat(4, opportunite.getLastprice());
            statement.setFloat(5, opportunite.getYesterdaychange());
            statement.setFloat(6, opportunite.getMarketcap());
            statement.setInt(7, opportunite.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred: ", e);
        }
    }

    @Override
    public List<Opportunite> getAllOpportunites() {
        List<Opportunite> opportunites = new ArrayList<>();
        String query = "SELECT * FROM opportunite";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Opportunite opportunite = new Opportunite();
                opportunite.setId(resultSet.getInt("id"));
                opportunite.setDescription(resultSet.getString("description"));
                opportunite.setPrix(resultSet.getFloat("prix"));
                opportunite.setName(resultSet.getString("name"));
                opportunite.setLastprice(resultSet.getFloat("lastprice"));
                opportunite.setYesterdaychange(resultSet.getFloat("yesterdaychange"));
                opportunite.setMarketcap(resultSet.getFloat("marketcap"));
                opportunites.add(opportunite);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred: ", e);
        }
        return opportunites;
    }
    public Opportunite getOpportuniteById(int opportuniteId) {
        String query = "SELECT * FROM opportunite WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, opportuniteId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Opportunite opportunite = new Opportunite();
                    opportunite.setId(resultSet.getInt("id"));
                    opportunite.setDescription(resultSet.getString("description"));
                    // Set other properties of Opportunite as needed
                    return opportunite;
                } else {
                    // Handle case where no Opportunite with the given ID exists
                    // You can return an empty Opportunite object or throw an exception
                    // For example:
                    // throw new IllegalArgumentException("No Opportunite found with ID: " + opportuniteId);
                    return new Opportunite(); // Returning an empty Opportunite object for demonstration
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
        return null;
    }

}