package services;
import entities.Personnes;
import interfaces.IService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonneService implements IService<Personnes> {
    private Connection connection;

    public PersonneService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addPerson(Personnes personne) throws SQLException {
        String query = "INSERT INTO personnes (nom, prenom) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, personne.getNom());
            statement.setString(2, personne.getPrenom());
            statement.executeUpdate();
        }
    }


    @Override
    public void deletePerson(int id) throws SQLException { // Change parameter type to int
        String query = "DELETE FROM personnes WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void updatePerson(Personnes personne) throws SQLException {
        String query = "UPDATE personnes SET nom = ?, prenom = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, personne.getNom());
            statement.setString(2, personne.getPrenom());
            statement.setInt(3, personne.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public List<Personnes> getData() throws SQLException {
        List<Personnes> personnesList = new ArrayList<>();
        String query = "SELECT * FROM personnes";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Personnes personne = new Personnes(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"));
                personnesList.add(personne);
            }
        }
        return personnesList;
    }
}