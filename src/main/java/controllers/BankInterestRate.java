package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BankInterestRate {
    private Map<String, Double> interestRates;

    public BankInterestRate() {
        interestRates = new HashMap<>();
        // Add default interest rates for existing banks
        interestRates.put("ATB", 5.2);
        interestRates.put("BIAT", 4.8);
        interestRates.put("AMEN", 5.5);
        interestRates.put("ZITOUNA", 4.9);
        interestRates.put("ATTIJARI", 5.0);
        interestRates.put("BNA", 5.3);
    }

    public double getInterestRate(String bankName) {
        return interestRates.getOrDefault(bankName, generateRandomRate());
    }

    private double generateRandomRate() {
        // Generate a random interest rate between 2.0 and 6.0
        return 2.0 + new Random().nextDouble() * 4.0;
    }

    public static void main(String[] args) {
        BankInterestRate bankRates = new BankInterestRate();
    }
}