package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.Pickup;
import edu.metrostate.ics372.ganby.vehicle.Sedan;
import edu.metrostate.ics372.ganby.vehicle.SportsCar;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.awt.FileDialog;
import java.awt.Frame;

public class TestEnv {
    public static void main(String[] args) {
        System.out.println("Hello, GANBY!");

        // Create a DealerCatalog instance
        System.out.println("Creating a DealerCatalog instance...");
        DealerCatalog dealerCatalog = DealerCatalog.getInstance();
        System.out.println("DealerCatalog instance created.");

        LocalDateTime now = LocalDateTime.now();

        // Create Vehicle objects
        Vehicle v001 = new Sedan("V001", "Camry", "Toyota", 15000.00, "001", now, false);
        Vehicle v002 = new Pickup("V002", "F-150", "Ford", 25000.00, "001", now, false);
        Vehicle v003 = new SportsCar("V003", "Corvette", "Chevrolet", 50000.00, "001", now, false);
        Vehicle v004 = new Sedan("V004", "Accord", "Honda", 20000.00, "002", now, false);

        System.out.println("Vehicles created:");
        System.out.println(v001);
        System.out.println(v002);
        System.out.println(v003);
        System.out.println(v004);

        // Create Dealer objects
        Dealer d001 = new Dealer("001", "Toyota of St. Paul");
        Dealer d002 = new Dealer("002", "Honda of St. Paul");
        System.out.println("Dealers created:");
        System.out.println(d001);
        System.out.println(d002);

        // *** Testing Dealer Methods ***
        // Get Dealer ID
        System.out.println("Dealer ID: " + d001.getId());
        // Get Dealer Name
        System.out.println("Dealer Name: " + d001.getName());
        // Get Vehicle Acquisition Status
        System.out.println("Vehicle Acquisition Status: " + d001.getIsVehicleAcquisitionEnabled());
        // Get Vehicle Catalog
        System.out.println("Vehicle Catalog: " + d001.getVehicleCatalog());
        //set Vehicle Acquisition Status to false
        System.out.print("Disabling Vehicle Acquisition for Dealer 001...");
        d001.disableVehicleAcquisition("001");
        System.out.println("Vehicle Acquisition Status: " + d001.getIsVehicleAcquisitionEnabled());
        System.out.print("Enabling Vehicle Acquisition for Dealer 001...");
        d001.enableVehicleAcquisition("001");
        System.out.println("Vehicle Acquisition Status: " + d001.getIsVehicleAcquisitionEnabled());
        // Add Vehicle to Dealer
        System.out.println("Adding Vehicle to Dealer 001...");
        // Add Vehicles to Dealers
        d001.addVehicle(v001);
        d001.addVehicle(v002);
        d001.addVehicle(v003);
        d002.addVehicle(v004);

        // Add Dealers to DealerCatalog
        dealerCatalog.getDealerCatalog().put(d001.getId(), d001);
        dealerCatalog.getDealerCatalog().put(d002.getId(), d002);

        // Print DealerCatalog HashMap
        System.out.println("Current DealerCatalog:");
        System.out.println(dealerCatalog.getDealerCatalog());

        // Modify Vehicle Price
        System.out.println("Modifying vehicle price for V001...");
        dealerCatalog.modifyVehiclePrice("V001", 20000.00);
        System.out.println("Updated Vehicle Price: " + v001.getPrice());

        /*
        // Print all vehicles sorted by price
        dealerCatalog.printAllVehiclesByPrice();

        // Print all vehicles in a price range
        dealerCatalog.printVehiclesByPriceRange(18000.00, 30000.00);

        // Print dealers by inventory range
        dealerCatalog.printDealersByInventoryRange(1, 3);

        // Print all dealers
        dealerCatalog.printAllDealers();

        // Print all vehicles by dealer ID
        dealerCatalog.printAllVehiclesByDealerId();

        // Print all vehicles of a dealer
        dealerCatalog.printAllVehiclesOfDealer("001");
    }
    */

    }
}






