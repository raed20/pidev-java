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
        String query = "SELECT * FROM Panier WHERE product_id = ?";
        String insertQuery = "INSERT INTO Panier (product_id, quantity) VALUES (?, ?)";
        String updateQuery = "UPDATE Panier SET quantity = ? WHERE product_id = ?";

        try (PreparedStatement selectStatement = connection.prepareStatement(query);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            for (Map.Entry<Product, Integer> entry : panier.getProducts().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                // Check if the product already exists in the cart
                selectStatement.setInt(1, product.getId());
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    // Product already exists in the cart, update its quantity
                    int currentQuantity = resultSet.getInt("quantity");
                    int newQuantity = currentQuantity + quantity;
                    updateStatement.setInt(1, newQuantity);
                    updateStatement.setInt(2, product.getId());
                    updateStatement.executeUpdate();
                } else {
                    // Product doesn't exist in the cart, insert it as a new entry
                    insertStatement.setInt(1, product.getId());
                    insertStatement.setInt(2, quantity);
                    insertStatement.executeUpdate();
                }
            }

            System.out.println("Product(s) added to Cart!");
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
        String query = "SELECT p.id AS panier_id, p.product_id, p.quantity, pr.* " +
                "FROM Panier p " +
                "LEFT JOIN Product pr ON p.product_id = pr.id";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int panierId = resultSet.getInt("panier_id");
                int productId = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                // Check if the Panier already exists in the list
                Panier panier = null;
                for (Panier existingPanier : paniers) {
                    if (existingPanier.getId() == panierId) {
                        panier = existingPanier;
                        break;
                    }
                }
                // If the Panier doesn't exist, create a new one
                if (panier == null) {
                    panier = new Panier();
                    panier.setId(panierId);
                    paniers.add(panier);
                }

                // Create a Product object and add it to the Panier
                Product product = new Product();
                product.setId(productId);
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getDouble("price"));
                product.setDescription(resultSet.getString("description"));
                product.setImage(resultSet.getString("image"));
                product.setDiscount(resultSet.getDouble("discount"));

                // Add the Product to the Panier with its quantity
                panier.getProducts().put(product, quantity);
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
    public void DeleteFromCart(int productId) {
        String query = "DELETE FROM Panier WHERE product_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Item deleted from Cart successfully!");
            } else {
                System.out.println("No item found in the Cart with product ID " + productId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting item from Cart: " + e.getMessage());
        }
    }

    public void clearPanierTable() {
        String query = "DELETE FROM Panier";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Panier table cleared successfully!");
            } else {
                System.out.println("Panier table is already empty.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing Panier table: " + e.getMessage());
        }
    }

}
