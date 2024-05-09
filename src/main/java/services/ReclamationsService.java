package services;

import tools.ImageUploadService;
import models.Reclamations;
import entities.Responses;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReclamationsService {
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/pidev";
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
        String query = "INSERT INTO reclamation (objet, description, date_rec, etat , image_name) VALUES (?, ?, ?, ?, ?)";
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
        String query = "SELECT * FROM reclamation";
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String objet = resultSet.getString("objet");
                String description = resultSet.getString("description");
                Timestamp dateReclamation = resultSet.getTimestamp("date_rec");
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
        String query = "UPDATE reclamation SET objet=?, description=?, date_reclamation=?, etat=? WHERE id=?";
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
        String query = "DELETE FROM reclamation WHERE id=?";
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
        String query = "UPDATE reclamation SET etat = ? WHERE id = ?";
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
    public boolean objetExists(String description) {
        String query = "SELECT COUNT(*) FROM reclamation WHERE objet = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, description);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Returns true if count is more than 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Responses> getResponsesForReclamation(int reclamationId) {
        List<Responses> responses = new ArrayList<>();
        String query = "SELECT * FROM response WHERE reclamation_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reclamationId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Reclamations reclamation = getReclamationById(reclamationId);  // Assuming this method exists
                responses.add(new Responses(
                        rs.getInt("id"),
                        rs.getString("message"),
                        rs.getTimestamp("date"),
                        reclamation
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responses;
    }

    public Reclamations getReclamationById(int id) {
        String query = "SELECT * FROM reclamation WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Reclamations(
                        rs.getInt("id"),
                        rs.getString("objet"),
                        rs.getString("description"),
                        rs.getTimestamp("date_reclamation"),
                        rs.getBoolean("etat")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // or throw an exception
    }


    public void saveResponseAndUpdateEtat(Reclamations reclamation, String responseMessage) {
        // Save the response to database and update etat attribute of reclamation
        String insertResponseQuery = "INSERT INTO reponse (description, date_rep, id_rec) VALUES  (?, ?, ?)";
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
            String updateReclamationQuery = "UPDATE reclamation SET etat = ? WHERE id = ?";
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
