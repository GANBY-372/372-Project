package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.dealer.DealerCatalog;

public class TestEnv {
    public static void main(String[] args) {
        System.out.println("Hello, GANBY!");

        // Create a DealerCatalog instance
        System.out.println("Creating a DealerCatalog instance...");
        DealerCatalog.getInstance();
        System.out.println("DealerCatalog instance created.");

        // Load JSON file

        // Load XML file

        // Load CSV file

        // *** Testing DealerCatalog Methods ***

        // Get the DealerCatalog HashMap, and print it

        // Get Dealer by ID

        // add Vehicle Object

        // modify vehicle price

        // Print all vehicles by price

        // Print all vehicles in price range

        // Print dealers by total inventory range

        // Print all dealers

        // Print all vehicles of a dealer with dealerID

        // Amount of vehicles in inventory

        // Enable dealer acquisitions

        // Disable dealer acquisitions

    }
}
