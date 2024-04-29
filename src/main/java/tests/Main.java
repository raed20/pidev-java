package tests;

import entities.Investissement;
import entities.Opportunite;
import services.InvestissementService;
import services.OpportuniteService;
import tools.MyConnection;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        MyConnection connection = new MyConnection(); // Assuming you have a MyConnection class

        InvestissementService investissementService = new InvestissementService(connection);
        OpportuniteService opportuniteService = new OpportuniteService(connection);
        // Test Add operation for Investissement
        System.out.println("Adding a new investment...");
        Investissement newInvestissement = new Investissement();
        // Set properties for the new investment
        newInvestissement.setMontant(10000L); // Example value, replace with actual value
        newInvestissement.setDateInvest(new Date()); // Example value, replace with actual value
        newInvestissement.setTotalValue(5000F); // Example value, replace with actual value
        newInvestissement.setStockName("ABC"); // Example value, replace with actual value
        newInvestissement.setChangerate(0.05F); // Example value, replace with actual value
        newInvestissement.setPrice(50F); // Example value, replace with actual value
        newInvestissement.setOpport(1); // Example value, replace with actual value

        try {
            investissementService.addInvestissement(newInvestissement);
            System.out.println("New investment added successfully: " + newInvestissement);
        } catch (Exception e) {
            System.err.println("Error adding new investment: " + e.getMessage());
        }

        // Test Add operation for Opportunite
        System.out.println("Adding a new opportunity...");
        Opportunite newOpportunite = new Opportunite();
        // Set properties for the new opportunity
        newOpportunite.setDescription("New Opportunity Description"); // Example value, replace with actual value
        newOpportunite.setPrix(1000F); // Example value, replace with actual value
        newOpportunite.setName("New Opportunity Name"); // Example value, replace with actual value
        newOpportunite.setLastprice(200F); // Example value, replace with actual value
        newOpportunite.setYesterdaychange(0.02F); // Example value, replace with actual value
        newOpportunite.setMarketcap(500000F); // Example value, replace with actual value
        investissementService.deleteInvestissement(1);


        try {
            opportuniteService.addOpportunite(newOpportunite);
            System.out.println("New opportunity added successfully: " + newOpportunite);
        } catch (Exception e) {
            System.err.println("Error adding new opportunity: " + e.getMessage());
        }

    }
}
