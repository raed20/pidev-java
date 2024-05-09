package services;

import entities.Commentaire;
import interfaces.ICommentaire;
import tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommentaireService implements ICommentaire {
    private static final Logger logger = Logger.getLogger(CommentaireService.class.getName());
    private MyConnection connection;

    public CommentaireService(MyConnection connection) {
        this.connection = connection;
    }

    @Override
    public void addCommentaire(Commentaire commentaire) {
        String query = "INSERT INTO Commentaire (contenue, idblog_id,iduser) VALUES (?, ? , ?)";

        int userid=1;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, commentaire.getContent());
            statement.setInt(2, commentaire.getBlog_id());
            statement.setInt(3, userid);
            //statement.setInt(3, commentaire.getUserid());

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
        String query = "UPDATE Commentaire SET contenue = ?, blog_id = ? , userid= ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, commentaire.getContent());
            statement.setInt(2, commentaire.getBlog_id());
            statement.setInt(3, commentaire.getUserid());
            statement.setInt(4, commentaire.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred: ", e);
        }
    }

    @Override
    public List<Commentaire> getAllCommentaire() {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM Commentaire";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setId(resultSet.getInt("id"));
                commentaire.setContent(resultSet.getString("contenue"));
                commentaire.setBlog_id(resultSet.getInt("idblog_id"));
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred: ", e);
        }
        return commentaires;
    }

    public Commentaire getCommentaireById(int CommentaireId) {
        String query = "SELECT * FROM Commentaire WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, CommentaireId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Commentaire commentaire = new Commentaire();
                    commentaire.setId(resultSet.getInt("id"));
                    commentaire.setContent(resultSet.getString("contenue"));
                    commentaire.setBlog_id(resultSet.getInt("idblog_id"));
                    return commentaire;
                } else {
                    return new Commentaire();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL Exception occurred: ", e);
        }
        return null;
    }
}
