package edu.metrostate.ics372.motordealership;

import edu.metrostate.ics372.motordealership.dealer.Dealer;
import edu.metrostate.ics372.motordealership.dealer.DealerCatalog;
import edu.metrostate.ics372.motordealership.json.JSONFileImporter;
import edu.metrostate.ics372.motordealership.vehicle.VehicleCatalog;

import java.io.FileNotFoundException;

import java.util.Set;

public class Main {
    public static void main (String[] args) throws FileNotFoundException, IllegalAccessException {
        JSONFileImporter jsonFileImporter = new JSONFileImporter();
//
//        System.out.println("Importing vehicles...");
//        jsonFileImporter.printDealerKeys();

        jsonFileImporter.processJSON();;
        System.out.println(VehicleCatalog.getInstance().size() + " vehicles imported into catalog");
        System.out.println(DealerCatalog.getInstance().size() + " dealers imported into catalog");

        System.out.println(jsonFileImporter.getVehicles().size() + " processed vehicles from JSON");
        System.out.println(jsonFileImporter.getDealers().size() + " processed dealers from JSON");
    }
}