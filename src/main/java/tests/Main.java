package tests;
import java.util.Scanner;

import entities.Pret;
import services.LoanService;

public class Main {
    public static void main(String[] args) {
        // Test Add operation
        LoanService loanService = new LoanService();

        /*System.out.println("Adding a new pret...");
        //Pret newPret = new Pret("homme", "yes",3,"graduated","yes",3000,500,5000,12,1,"urban","no");
        LoanService loanService = new LoanService();
        Pret pret = new Pret();*/

        System.out.println("deleting ...");

            // Set Pret object properties here or use user input

        /*try {
            loanService.addLoan(pret);
            System.out.println("New Pret added successfully: " + pret);
        } catch (Exception e) {
            System.err.println("Error adding new Pret: " + e.getMessage());
        }*/

        /*try {
            // Demandez à l'utilisateur d'entrer l'identifiant de l'enregistrement à supprimer
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the ID of the loan to delete:");
            int idToDelete = scanner.nextInt();

            // Appelez la méthode deleteLoan avec l'identifiant spécifié
            loanService.deleteLoan(idToDelete);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        System.out.println("Editing ...");

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the ID of the loan to delete:");
            int idToUpdate = scanner.nextInt();
            loanService.updateLoan(idToUpdate);
            System.out.println(" Loan edited successfully: ");
        } catch (Exception e) {
            System.err.println("Error editing  the Loan: " + e.getMessage());
        }

    }
}
