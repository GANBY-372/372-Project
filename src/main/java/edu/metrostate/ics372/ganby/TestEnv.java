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
        DealerCatalog.getInstance();
        System.out.println("DealerCatalog instance created.");

        LocalDateTime now = LocalDateTime.now();

        // create  Vehicle children objects
        Vehicle v001 = new Sedan("V001", "Camry", "Toyota", 15000.00, "001", LocalDateTime.now(), false);
        Vehicle v002 = new Pickup("V002", "F-150", "Ford", 25000.00, "001", LocalDateTime.now(), false);
        Vehicle v003 = new SportsCar("V003", "Corvette", "Chevrolet", 50000.00, "001", LocalDateTime.now(), false);
        Vehicle v004 = new Sedan("V004", "Accord", "Honda", 20000.00, "002", LocalDateTime.now(), false);
        System.out.println("Vehicles created:");
        System.out.println(v001);
        System.out.println(v002);
        System.out.println(v003);
        System.out.println(v004);

        // *** Testing Vehicle Methods ***
        // Get Vehicle ID
        System.out.println("Vehicle ID: " + v001.getVehicleId());
        // Get Vehicle Model
        System.out.println("Vehicle Model: " + v001.getModel());
        // Get Vehicle Manufacturer
        System.out.println("Vehicle Manufacturer: " + v001.getManufacturer());
        // Get Vehicle Price
        System.out.println("Vehicle Price: " + v001.getPrice());
        // Get Vehicle Dealer ID
        System.out.println("Vehicle Dealer ID: " + v001.getDealerId());
        // Get Vehicle Acquisition Date
        System.out.println("Vehicle Acquisition Date: " + v001.getAcquisitionDate());
        // Get Vehicle Rent Status
        System.out.println("Vehicle Rent Status: " + v001.getIsRentedOut());

        // create Dealer objects
        Dealer d001 = new Dealer("001", "Toyota of St. Paul");
        Dealer d002 = new Dealer("002", "Honda of St. Paul");
        System.out.println("Dealers created:");
        System.out.println(d001);
        System.out.println(d002);

        // *** Testing Dealer Methods ***
        // Get Dealer ID
        System.out.println("Dealer ID: " + d001.getDealerId());
        // Get Dealer Name
        System.out.println("Dealer Name: " + d001.getDealerName());
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
        d001.addVehicle(v001);
        d001.addVehicle(v002);
        d001.addVehicle(v003);
        System.out.println("Vehicles added to Dealer 001:");

        // *** Testing DealerCatalog Methods ***
        // Add Dealer to DealerCatalog
        System.out.println("Adding Dealers to DealerCatalog...");
        // only vehicles are added to the catalog, not dealers
        DealerCatalog.getInstance().addVehicle(v001);
        DealerCatalog.getInstance().addVehicle(v002);
        DealerCatalog.getInstance().addVehicle(v003);
        DealerCatalog.getInstance().addVehicle(v004);

        // Get the DealerCatalog HashMap, and print it

        // Get Dealer by ID

        // TODO: This method not working
        // modify vehicle price
        System.out.println("Vehicle price modified: " + v001.getPrice());
        DealerCatalog.getInstance().modifyVehiclePrice("v001", 20000.00);
        System.out.println("Vehicle price modified: " + v001.getPrice());

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
