package services;

import entities.Panier;
import entities.Product;
import tools.MyConnection;
import interfaces.IService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PanierService implements IService<Panier> {

    private MyConnection connection;
    private static final Logger LOGGER = Logger.getLogger(MyConnection.class.getName());

    public PanierService(MyConnection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Panier panier) {
        String query = "INSERT INTO Panier (product_id, quantity) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Map.Entry<Product, Integer> entry : panier.getProducts().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                statement.setInt(1, product.getId());
                statement.setInt(2, quantity);
                statement.executeUpdate();
            }
            System.out.println("Product added to Cart!");
        } catch (SQLException e) {
            throw new RuntimeException("Error adding cart: " + e.getMessage());
        }
    }

    @Override
    public void update(Panier panier) {
        String updateQuery = "UPDATE Panier SET product_id = ?, quantity = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            for (Map.Entry<Product, Integer> entry : panier.getProducts().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                statement.setInt(1, product.getId());
                statement.setInt(2, quantity);
                statement.setInt(3, panier.getId());
                statement.executeUpdate();
            }
            System.out.println("Cart updated successfully!");
        } catch (SQLException e) {
            throw new RuntimeException("Error updating cart: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Panier WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cart deleted successfully!");
            } else {
                System.out.println("No cart found with id " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting cart: " + e.getMessage());
        }
    }

    @Override
    public List<Panier> getAll() {
        List<Panier> paniers = new ArrayList<>();
        String query = "SELECT * FROM Panier";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int panierId = resultSet.getInt("id");
                Panier panier = new Panier();
                panier.setId(panierId);
                paniers.add(panier);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving carts: " + e.getMessage());
        }
        return paniers;
    }

    @Override
    public Panier getOne(int id) {
        String query = "SELECT * FROM Panier WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Panier panier = new Panier();
                panier.setId(id);
                // You may add other attributes if needed
                return panier;
            } else {
                System.out.println("No cart found with ID " + id);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving cart: " + e.getMessage());
        }
    }
}
