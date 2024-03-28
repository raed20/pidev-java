package org.example;
import entities.Investissement;
import entities.Opportunite;
import services.InvestissementService;
import services.OpportuniteService;
import tools.MyConnection;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {

        MyConnection connection = new MyConnection();
        InvestissementService investissementService = new InvestissementService(connection);
        OpportuniteService opportuniteService = new OpportuniteService(connection);
        try {
            Random random = new Random();

            // Generate random data for Opportunite
            Opportunite opportunite = new Opportunite();
            opportunite.setDescription("Description_" + random.nextInt(1000));
            opportunite.setPrix((float) (random.nextDouble() * 100));
            opportunite.setName("Opportunite_" + random.nextInt(1000));
            opportunite.setLastprice((float) (random.nextDouble() * 100));
            opportunite.setYesterdaychange((float) (random.nextDouble() * 100));
            opportunite.setMarketcap((float) (random.nextDouble() * 1000));
            opportuniteService.addOpportunite(opportunite);
            LOGGER.log(Level.INFO, "Opportunite added successfully.");


            // Add Opportunite

            // Generate random data for Investissement
            Investissement investissement = new Investissement();
            investissement.setMontant((long) (random.nextDouble() * 1000));
            investissement.setDateInvest(new Date());
            investissement.setTotalValue((float) (random.nextDouble() * 1000));
            investissement.setStockName("Stock_" + random.nextInt(1000));
            investissement.setChangerate((float) random.nextDouble());
            investissement.setPrice((float) (random.nextDouble() * 100));

            // Set the Opportunite for Investissement
            if (opportunite.getId() != null) {
            investissement.setOpport(opportunite.getId());}


            // Add Investissement

            investissementService.addInvestissement(investissement);
            LOGGER.log(Level.INFO, "Investissement added successfully.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An Exception occurred:", e);
        }
    }
}