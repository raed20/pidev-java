package services;

import entities.Product;
import interfaces.IService;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductService implements IService<Product> {
    private MyConnection connection;
    private static final Logger LOGGER = Logger.getLogger(MyConnection.class.getName());

    public ProductService(MyConnection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Product product) {
        String req = "INSERT INTO product(name, price, description, category_id, image, discount) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getCategory());
            ps.setString(5, product.getImage());
            ps.setDouble(6, product.getDiscount()); // Set discount
            ps.executeUpdate();
            System.out.println("Product added successfully !");
        } catch (SQLException e) {
            throw new RuntimeException("Error adding product: " + e.getMessage());
        }
    }

    @Override
    public void update(Product product) {
        String query = "UPDATE product SET name = ?, price = ?, description = ?, category_id = ?, image = ?, discount = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getCategory());
            ps.setString(5, product.getImage());
            ps.setDouble(6, product.getDiscount()); // Set discount
            ps.setInt(7, product.getId());
            ps.executeUpdate();
            System.out.println("Product Updated !");
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String req = "DELETE FROM product WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Product Deleted !");
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product: " + e.getMessage());
        }
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String req = "SELECT * FROM product";
        try (Statement st = connection.createStatement(); ResultSet res = st.executeQuery(req)) {
            while (res.next()) {
                Product product = new Product();
                product.setId(res.getInt("id"));
                product.setName(res.getString("name"));
                product.setPrice(res.getDouble("price"));
                product.setDescription(res.getString("description"));
                product.setCategory(res.getInt("category_id"));
                product.setImage(res.getString("image"));
                product.setDiscount(res.getDouble("discount")); // Set discount
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all products: " + e.getMessage());
        }
        return products;
    }

    @Override
    public Product getOne(int id) {
        String query = "SELECT * FROM product WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setDescription(resultSet.getString("description"));
                    product.setCategory(resultSet.getInt("category_id"));
                    product.setImage(resultSet.getString("image"));
                    product.setDiscount(resultSet.getDouble("discount")); // Set discount
                    return product;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting product: " + e.getMessage());
        }
        throw new IllegalArgumentException("Product with id " + id + " not found");
    }

    public List<Product> searchByName(String searchText) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE name LIKE ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + searchText + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setDescription(rs.getString("description"));
                    product.setImage(rs.getString("image"));
                    product.setDiscount(rs.getDouble("discount"));
                    product.setCategory(rs.getInt("category_id"));
                    products.add(product);
                }
            }
        }
        return products;
    }
}
