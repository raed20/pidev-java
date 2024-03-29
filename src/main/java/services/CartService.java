package services;

import entities.Cart;
import entities.Product;
import tools.MyConnection;
import interfaces.IService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class CartService implements IService<Cart> {

    private MyConnection connection;
    private static final Logger LOGGER = Logger.getLogger(MyConnection.class.getName());

    public CartService(MyConnection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Cart cart) {
        String query = "INSERT INTO Cart (product_id, quantity) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Map.Entry<Product, Integer> entry : cart.getProducts().entrySet()) {
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
    public void update(Cart cart) {
        String updateQuery = "UPDATE Cart SET product_id = ?, quantity = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            for (Map.Entry<Product, Integer> entry : cart.getProducts().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                statement.setInt(1, product.getId());
                statement.setInt(2, quantity);
                statement.setInt(3, cart.getId());
                statement.executeUpdate();
            }
            System.out.println("Cart updated successfully!");
        } catch (SQLException e) {
            throw new RuntimeException("Error updating cart: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Cart WHERE id = ?";
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
    public List<Cart> getAll() {
        List<Cart> carts = new ArrayList<>();
        String query = "SELECT * FROM Cart";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int cartId = resultSet.getInt("id");
                Cart cart = new Cart();
                cart.setId(cartId);
                // You may add other attributes if needed
                carts.add(cart);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving carts: " + e.getMessage());
        }
        return carts;
    }

    @Override
    public Cart getOne(int id) {
        String query = "SELECT * FROM Cart WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Cart cart = new Cart();
                cart.setId(id);
                // You may add other attributes if needed
                return cart;
            } else {
                System.out.println("No cart found with ID " + id);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving cart: " + e.getMessage());
        }
    }
}