/**
 * JSONIOTester.java
 * @author B, G
 * This class is for testing JSON file import and export.
 * Used for testing.
 */
package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.catalog.VehicleCatalog;
import edu.metrostate.ics372.ganby.json.JSONFileExporter;
import edu.metrostate.ics372.ganby.json.JSONFileImporter;

import java.io.FileNotFoundException;

public class JSONIOTester {

    public static void main(String[] args) throws FileNotFoundException, IllegalAccessException {

        JSONFileImporter jsonFileImporter = new JSONFileImporter();
//
        System.out.println("Importing vehicles...");
        jsonFileImporter.printDealerKeys();

        jsonFileImporter.processJSON();
//        System.out.println(VehicleCatalog.getInstance().getVehicles().size() + " vehicles imported into catalog");
//        System.out.println(DealerCatalog.getInstance().getDealers().size() + " dealers imported into catalog");
//
//        System.out.println(jsonFileImporter.getVehicles().size() + " processed vehicles from JSON");
        System.out.println(jsonFileImporter.getDealers().toString());
//        System.out.println(jsonFileImporter.getDealers().size() + " processed dealers from JSON");
//
        JSONFileExporter jsonFileExporter = new JSONFileExporter();
        jsonFileExporter.exportToFile();
    }
}