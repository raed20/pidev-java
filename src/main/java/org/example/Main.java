package org.example;
import interfaces.IService;


import entities.Personnes;
import services.PersonneService;
import tools.MyConnection;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Main {
    public static void main(String[] args) {
        try {
            // Establish database connection
            MyConnection connection = new MyConnection();

            // Create IService instance
            IService<Personnes> personneService = new PersonneService(connection.getConnection());

            // Test Update operation
            System.out.println("Updating a Personne...");
            // Suppose you have an existing Personne with ID 1 in the database
            Personnes existingPersonne = new Personnes(1, "taher", "ezzine");
            existingPersonne.setNom("Abdou");
            existingPersonne.setPrenom("Bouafif");
            personneService.updatePerson(existingPersonne);
            System.out.println("Personne updated successfully: " + existingPersonne);

            // Test Read operation
            System.out.println("Retrieving all Personnes...");
            List<Personnes> personnesList = personneService.getData();
            for (Personnes personne : personnesList) {
                System.out.println(personne);
            }

            // Test Delete operation
            System.out.println("Deleting a Personne...");
            // Suppose you want to delete the same Personne with ID 1
            personneService.deletePerson(2);
            System.out.println("Personne deleted successfully");

            // Close database connection
            connection.closeConnection();
        } catch (SQLException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "SQL Exception occurred: " + e.getMessage());
        }
    }
    }
