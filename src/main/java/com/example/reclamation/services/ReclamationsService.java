package com.example.reclamation.services;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.reclamation.config.ImageUploadService;
import com.example.reclamation.models.Reclamations;

public class ReclamationsService{
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mariadb://localhost:3307/reclamations";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // JDBC variables for opening, closing and managing connection
    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    private ImageUploadService imageUploadService;

    public ReclamationsService() {
        this.imageUploadService = new ImageUploadService();
    }

    public void addReclamation(Reclamations reclamation, File imageFile) {
        // Upload the image
        String imagePath = imageUploadService.uploadImage(imageFile);

        // Set the image path in the reclamation object
        reclamation.setImagePath(imagePath);

        // Add reclamation to the database
        String query = "INSERT INTO reclamations (objet, description, date_reclamation, etat , imagePath) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, reclamation.getObjet());
            preparedStatement.setString(2, reclamation.getDescription());
            preparedStatement.setTimestamp(3, new Timestamp(reclamation.getDate_reclamation().getTime()));
            preparedStatement.setBoolean(4, reclamation.isEtat());
            preparedStatement.setString(5, reclamation.getImagePath()); // Set the image path
            preparedStatement.executeUpdate();

            System.out.println("Reclamation added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read operation
    public List<Reclamations> getAllReclamations() {
        List<Reclamations> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamations";
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String objet = resultSet.getString("objet");
                String description = resultSet.getString("description");
                Timestamp dateReclamation = resultSet.getTimestamp("date_reclamation");
                boolean etat = resultSet.getBoolean("etat");
                Reclamations reclamation = new Reclamations(id, objet, description, dateReclamation, etat);
                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return reclamations;
    }

    // Update operation
    public void updateReclamation(Reclamations reclamation) {
        String query = "UPDATE reclamations SET objet=?, description=?, date_reclamation=?, etat=? WHERE id=?";
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reclamation.getObjet());
            preparedStatement.setString(2, reclamation.getDescription());
            preparedStatement.setTimestamp(3, new Timestamp(reclamation.getDate_reclamation().getTime()));
            preparedStatement.setBoolean(4, reclamation.isEtat());
            preparedStatement.setInt(5, reclamation.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // Delete operation
    public void deleteReclamation(int id) {
        String query = "DELETE FROM reclamations WHERE id=?";
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public void updateReclamationEtat(int id, boolean newEtat) {
        String query = "UPDATE reclamations SET etat = ? WHERE id = ?";
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, newEtat);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Utility method to close JDBC resources
    private static void closeResources() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveResponseAndUpdateEtat(Reclamations reclamation, String responseMessage) {
        // Save the response to database and update etat attribute of reclamation
        String insertResponseQuery = "INSERT INTO responses (message, date, reclamation_id) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement insertResponseStatement = connection.prepareStatement(insertResponseQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Set parameters for response
            insertResponseStatement.setString(1, responseMessage);
            insertResponseStatement.setTimestamp(2, new Timestamp(new Date().getTime()));
            insertResponseStatement.setInt(3, reclamation.getId());

            // Execute insert statement
            insertResponseStatement.executeUpdate();

            // Get the generated response ID
            int responseId;
            try (ResultSet generatedKeys = insertResponseStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    responseId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve response ID from the database.");
                }
            }

            // Update etat attribute of reclamation
            String updateReclamationQuery = "UPDATE reclamations SET etat = ? WHERE id = ?";
            try (PreparedStatement updateReclamationStatement = connection.prepareStatement(updateReclamationQuery)) {
                updateReclamationStatement.setBoolean(1, true);
                updateReclamationStatement.setInt(2, reclamation.getId());
                updateReclamationStatement.executeUpdate();
            }

            System.out.println("Response saved and etat attribute updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
