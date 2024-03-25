package org.example;
// import interfaces.IService;
import interfaces.UService;


// import entities.Personnes;
// import services.PersonneService;

import entities.Utilisateurs;
import services.UtilisateurService;

import tools.MyConnection;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.sql.SQLException;
public class Main {
    public static void main(String[] args) {
        try {
            // Establish database connection
            MyConnection connection = new MyConnection();

            // Create IService instance
            // IService<Personnes> personneService = new PersonneService(connection.getConnection());


            UtilisateurService utilisateurService = new UtilisateurService(connection.getConnection());

            //Update user
           /* System.out.println("Updating a User...");
            Utilisateurs UpUser = new Utilisateurs();
            UpUser.setId(1);
            UpUser.setLastname("hama");
            UpUser.setAdresse("tadha");
            UpUser.setEmail("nvkays@gmail.com");
            UpUser.setNumtel(0000000);
            UpUser.setRoles("Client");
            UpUser.setPassword("112233445566");

            utilisateurService.updateUtilisateur(UpUser);

            System.out.println("Utilisateur mis à jour avec succès.");  */







            //Liste des users
          /* System.out.println("Liste des utilisateurs :");
            List<Utilisateurs> utilisateursList = utilisateurService.getData();
            for (Utilisateurs user : utilisateursList) {
                System.out.println("ID : " + user.getId());
                System.out.println("Nom : " + user.getLastname());
                System.out.println("Email : " + user.getEmail());
                System.out.println("Numéro de téléphone : " + user.getNumtel());
                System.out.println("Adresse : " + user.getAdresse());
                System.out.println("Roles : " + user.getRoles());
                System.out.println("Mot de passe : " + user.getPassword());
                System.out.println();
            } */






            //AddUser
         /*    Utilisateurs nvUser = new Utilisateurs();
            nvUser.setLastname("h");
            nvUser.setAdresse("sdgbdg");
            nvUser.setEmail("hssssssssss@gmail.com");
            nvUser.setNumtel(4526526);
            nvUser.setRoles("admin");
            nvUser.setPassword("123456789");
            utilisateurService.addUtilisateur(nvUser);
            System.out.println("user added succes");   */






            // Delete user
             System.out.println("Deleting a User...");
            utilisateurService.deleteUtilisateur(3);
            System.out.println("User deleted successfully");





            // Suppose you have an existing Personne with ID 1 in the database
            //Personnes existingPersonne = new Personnes(1, "taher", "ezzine");
            //existingPersonne.setNom("Abdou");
            //existingPersonne.setPrenom("Bouafif");
            // personneService.updatePerson(existingPersonne);
            // System.out.println("Personne updated successfully: " + existingPersonne);

            // Test Read operation
            //  System.out.println("Retrieving all Personnes...");
            // List<Personnes> personnesList = personneService.getData();
            // for (Personnes personne : personnesList) {
            //   System.out.println(personne);
            //  }

            // Test Delete operation
            // System.out.println("Deleting a Personne...");


            //System.out.println("Deleting a User...");


            // Suppose you want to delete the same Personne with ID 1
            //  personneService.deletePerson(2);
            //  System.out.println("Personne deleted successfully");




            // Close database connection
            connection.closeConnection();


        } catch (SQLException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "SQL Exception occurred: " + e.getMessage());
        }
    }
}
