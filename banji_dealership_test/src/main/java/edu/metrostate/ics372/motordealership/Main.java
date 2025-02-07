/*
 * Main.java
 * Version: 1.0
 * Project: Motor Dealership
 * This is the main class for the car dealership application.
 */

package edu.metrostate.ics372.motordealership;
import edu.metrostate.ics372.motordealership.dealer.DealerCatalog;
import edu.metrostate.ics372.motordealership.json.JSONFileExporter;
import edu.metrostate.ics372.motordealership.json.JSONFileImporter;
import edu.metrostate.ics372.motordealership.vehicle.VehicleCatalog;
import java.io.FileNotFoundException;

public class Main {
    public static void main (String[] args) throws FileNotFoundException, IllegalAccessException {

        JSONFileImporter jsonFileImporter = new JSONFileImporter();
        JSONFileExporter jsonFileExporter = new JSONFileExporter();

        System.out.println("-------- Dealership Inventory Import/Export --------");
        System.out.println("Reading JSON file...");
        System.out.println("Importing dealerships and vehicles...");

        jsonFileImporter.printDealerKeys();
        jsonFileImporter.processJSON();
        System.out.println(VehicleCatalog.getInstance().getVehicles().size() + " vehicles imported into catalog");
        System.out.println(DealerCatalog.getInstance().getDealers().size() + " dealers imported into catalog");
        System.out.println(jsonFileImporter.getVehicles().size() + " processed vehicles from JSON");
        System.out.println(jsonFileImporter.getDealers().toString());
        System.out.println(jsonFileImporter.getDealers().size() + " processed dealers from JSON");

        jsonFileExporter.exportToFile();
    }
}