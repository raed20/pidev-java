package tests;

import entities.Pret;
import services.LoanService;
import weka.classifiers.Classifier;

public class Main {
    public static void main(String[] args) throws Exception {
        // Test Add operation
        LoanService loanService = new LoanService();
        //BankService bankService = new BankService();
        System.out.println("Adding a new pret...");
       // Pret newPret = new Pret("homme", "yes",3,"graduated","yes",3000,500,5000,12,1,"urban","no");
        Pret pret = new Pret();
        //System.out.println("deleting ...");

            // Set Pret object properties here or use user input

        try {
            loanService.addLoan(pret);
            System.out.println("New Pret added successfully: " + pret);
        } catch (Exception e) {
            System.err.println("Error adding new Pret: " + e.getMessage());
        }

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


        /*System.out.println("Editing ...");

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the ID of the loan to delete:");
            int idToUpdate = scanner.nextInt();
            loanService.updateLoan(idToUpdate);
            System.out.println(" Loan edited successfully: ");
        } catch (Exception e) {
            System.err.println("Error editing  the Loan: " + e.getMessage());
        }*/

       /* try {
            List<Pret> pretList = loanService.getDataLoan();
            for (Pret pret : pretList) {
                System.out.println("ID: " + pret.getId());
                System.out.println("Gender: " + pret.getGender());
                System.out.println("Married: " + pret.getMarried());
                System.out.println("Dependents: " + pret.getDependents());
                System.out.println("Education: " + pret.getEducation());
                System.out.println("Self Employed: " + pret.getSelfEmployed());
                System.out.println("Applicant Income: " + pret.getApplicantIncome());
                System.out.println("Coapplicant Income: " + pret.getCoapplicantIncome());
                System.out.println("Loan Amount: " + pret.getLoanAmount());
                System.out.println("Loan Amount Term: " + pret.getLoanAmountTerm());
                System.out.println("Credit History: " + pret.getCreditHistory());
                System.out.println("Property Area: " + pret.getPropertyArea());
                System.out.println("Loan Status: " + pret.getLoanStatus());
                System.out.println("-------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*System.out.println("Adding a new bank...");
        Bank newBank = new Bank("UIB","gabes","UIBHTEJA","test","+21675456548");
        try {
            bankService.addBank(newBank);
            System.out.println("New Pret added successfully: " + newBank);
        } catch (Exception e) {
            System.err.println("Error adding new Pret: " + e.getMessage());
        }*/

        /*try {
            // Demandez à l'utilisateur d'entrer l'identifiant de l'enregistrement à supprimer
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the ID of the bank to delete:");
            int idToDelete = scanner.nextInt();

            // Appelez la méthode deleteLoan avec l'identifiant spécifié
            bankService.deleteBank(idToDelete);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*System.out.println("Editing ...");

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the ID of the bank to edit:");
            int idToUpdate = scanner.nextInt();
            bankService.updateBank(idToUpdate);
            System.out.println(" bank edited successfully: ");
        } catch (Exception e) {
            System.err.println("Error editing  the bank: " + e.getMessage());
        }*/

        /*try {
            List<Bank> banks = bankService.getDataBank();
            for (Bank bank : banks) {
                System.out.println("Bank ID: " + bank.getId());
                System.out.println("Bank Name: " + bank.getNom());
                System.out.println("Bank Address: " + bank.getAdresse());
                System.out.println("Bank Swift Code: " + bank.getCodeSwift());
                System.out.println("Bank Logo: " + bank.getLogo());
                System.out.println("Bank Phone Number: " + bank.getPhoneNum());
                System.out.println("-------------------------");
            }
        } catch (Exception e) {
            System.err.println("Error fetching banks: " + e.getMessage());
        }*/

        //new GMailer().sendMail("test","test");


    }
}

