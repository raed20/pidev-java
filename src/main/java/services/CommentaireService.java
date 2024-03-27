package services;
import entities.Commentaire;
import interfaces.ICommentaire;
import tools.MyConnection;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CommentaireService implements interfaces.ICommentaire {
    private static final Logger logger = Logger.getLogger(CommentaireService.class.getName());
    private MyConnection connection;

    public CommentaireService(MyConnection connection) {
        this.connection = connection;
    }

    @Override
    public void addCommentaire(Commentaire Commentaire) {
        String query = "INSERT INTO Commentaire (Content) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, Commentaire.getContent());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred: ", e);
        }
    }

    @Override
    public void deleteCommentaire(int id) {
        String query = "DELETE FROM Commentaire WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred: ", e);
        }
    }

    @Override
    public void updateCommentaire(Commentaire commentaire) {
        String query = "UPDATE Commentaire SET Content = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, commentaire.getContent());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred: ", e);
        }
    }

    @Override
    public List<Commentaire> getAllCommentaire() {
        List<Commentaire> Commentaires = new ArrayList<>();
        String query = "SELECT * FROM Commentaire";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setId(resultSet.getInt("id"));
                commentaire.setContent(resultSet.getString("content"));
                Commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred: ", e);
        }
        return Commentaires;
    }
    public Commentaire getCommentaireById(int CommentaireId) {
        String query = "SELECT * FROM Commentaire WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, CommentaireId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Commentaire commentaire = new Commentaire();
                    commentaire.setId(resultSet.getInt("id"));
                    commentaire.setContent(resultSet.getString("content"));
                    // Set other properties of Commentaire as needed
                    return commentaire;
                } else {
                    // Handle case where no Commentaire with the given ID exists
                    // You can return an empty Commentaire object or throw an exception
                    // For example:

                    return new Commentaire(); // Returning an empty Commentaire object for demonstration
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
        return null;
    }

}