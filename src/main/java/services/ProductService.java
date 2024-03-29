package services;

import entities.Product;
import entities.Category;
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
        String req = "INSERT INTO product(name,price,description,category_id) VALUES (?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getCategory());
            ps.executeUpdate();
            System.out.println("Product added successfully !");
        } catch (SQLException e) {
            throw new RuntimeException("Error adding product: " + e.getMessage());
        }
    }

    @Override
    public void update(Product product) {
        String query = "UPDATE product SET name = ?, price = ?, description = ?, category_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getCategory());
            ps.setInt(5, product.getId());
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
                    return product;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting product: " + e.getMessage());
        }
        throw new IllegalArgumentException("Product with id " + id + " not found");
    }
}