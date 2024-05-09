package services;
import entities.Utilisateurs;
import interfaces.UService;
import tools.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService implements UService<Utilisateurs> {
    private Connection connection;
    public static Utilisateurs utilisateurConnecte = new Utilisateurs();

    public UtilisateurService(Connection connection) {
        MyConnection myConnection=new MyConnection();
        connection = myConnection.getConnection();

    }
    public UtilisateurService(){
        MyConnection myConnection = new MyConnection();
        connection = myConnection.getConnection();
    }

    public static void setUtilisateurConnecte(Utilisateurs utilisateur) {
        utilisateurConnecte = utilisateur;
    }

    public static Utilisateurs getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    @Override
    public void addUtilisateur(Utilisateurs utilisateur) throws SQLException {
        String query = "INSERT INTO user (lastname, email, roles, numtel, adresse, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, utilisateur.getLastname());
            statement.setString(2, utilisateur.getEmail());
            statement.setString(3, utilisateur.getRoles());
            statement.setInt(4, utilisateur.getNumtel());
            statement.setString(5, utilisateur.getAdresse());
            statement.setString(6, utilisateur.getPassword());
            statement.executeUpdate();
        }
    }


    @Override
    public void deleteUtilisateur(String email) throws SQLException {
        // Récupérer la connexion à la base de données
        MyConnection myConnection = new MyConnection();
        Connection connection = myConnection.getConnection();

        // Vérifier si la connexion existe
        if (connection != null) {
            String query = "DELETE FROM user WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email); // Utiliser l'ID passé en paramètre de la méthode
                statement.executeUpdate();
                System.out.println("L'utilisateur avec l'Email " + email + " a été supprimé avec succès.");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
                e.printStackTrace();
                // Gérer l'exception SQL de manière appropriée
            } finally {
                // Fermer la connexion après utilisation
                connection.close();
            }
        } else {
            System.out.println("La connexion à la base de données n'a pas pu être établie.");
        }
    }




    @Override
    public void updateUtilisateur(Utilisateurs utilisateur) throws SQLException {
        // Initialize the connection
        MyConnection myConnection = new MyConnection();
        Connection connection = myConnection.getConnection();

        // Check if the connection is not null
        if (connection != null) {
            String query = "UPDATE user SET lastname = ?, email = ?, roles = ?, numtel = ?, adresse = ? WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, utilisateur.getLastname());
                statement.setString(2, utilisateur.getEmail());
                statement.setString(3, utilisateur.getRoles());
                statement.setInt(4, utilisateur.getNumtel());
                statement.setString(5, utilisateur.getAdresse());
                statement.setString(6, utilisateur.getEmail());
                statement.executeUpdate();
                System.out.println("Profil mis à jour avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la mise à jour du profil : " + e.getMessage());
                e.printStackTrace();
                // Handle SQL exception
            } finally {
                // Close the connection after use
                connection.close();
            }
        } else {
            System.out.println("La connexion à la base de données n'a pas pu être établie.");
        }
    }

    @Override
    public List<Utilisateurs> getData() throws SQLException {
        List<Utilisateurs> utilisateursList = new ArrayList<>();
        String query = "SELECT * FROM user";
        MyConnection myConnection = new MyConnection();
        Connection connection = myConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Utilisateurs utilisateur = new Utilisateurs(
                        resultSet.getInt("id"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getInt("numtel"),
                        resultSet.getString("adresse"),
                        resultSet.getString("roles"),
                        resultSet.getString("password"));
                utilisateursList.add(utilisateur);
            }
        }
        System.out.println("ViewAllUsers : ");
        for (Utilisateurs utilisateur : utilisateursList) {
            System.out.println(utilisateur);
        }
        return utilisateursList;
    }


}