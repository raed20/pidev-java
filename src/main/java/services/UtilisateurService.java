package services;
import entities.Utilisateurs;
import interfaces.UService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService implements UService<Utilisateurs> {
    private Connection connection;
    private static Utilisateurs utilisateurConnecte;

    public UtilisateurService(Connection connection) {
        this.connection = connection;

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
    public void deleteUtilisateur(int id) throws SQLException { // Change parameter type to int
        String query = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, utilisateurConnecte.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void updateUtilisateur(Utilisateurs utilisateur) throws SQLException {
        String query = "UPDATE user SET lastname = ?, email = ?, roles = ?, numtel = ?, adresse = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, utilisateur.getLastname());
            statement.setString(2, utilisateur.getEmail());
            statement.setString(3, utilisateur.getRoles());
            statement.setInt(4, utilisateur.getNumtel());
            statement.setString(5, utilisateur.getAdresse());
            statement.setInt(6, utilisateur.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public List<Utilisateurs> getData() throws SQLException {
        List<Utilisateurs> utilisateursList = new ArrayList<>();
        String query = "SELECT * FROM user";
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