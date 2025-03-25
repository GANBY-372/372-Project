package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.DataProcessing.JSONFileExporter;
import edu.metrostate.ics372.ganby.DataProcessing.XMLFileImporter;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.DataProcessing.JSONFileImporter;
import edu.metrostate.ics372.ganby.vehicle.Pickup;
import edu.metrostate.ics372.ganby.vehicle.Sedan;
import edu.metrostate.ics372.ganby.vehicle.SportsCar;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import org.w3c.dom.Document;

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
        Vehicle v001 = new Sedan("V001", "Camry", "Toyota", 15000.00, "D001", LocalDateTime.now(), false);
        Vehicle v002 = new Pickup("V002", "F-150", "Ford", 25000.00, "D001", LocalDateTime.now(), false);
        Vehicle v003 = new SportsCar("V003", "Corvette", "Chevrolet", 50000.00, "D001", LocalDateTime.now(), false);
        Vehicle v004 = new Sedan("V004", "Accord", "Honda", 20000.00, "D002", LocalDateTime.now(), false);

        // *** Testing DealerCatalog Methods ***

        // add Vehicle Object

        // Get the DealerCatalog HashMap, and print it

        // Get Dealer by ID

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
