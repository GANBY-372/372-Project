package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Pickup;
import edu.metrostate.ics372.ganby.vehicle.Sedan;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TestDriver {
    public static void main(String[] args) {
        // This is a placeholder for the main method.
        // You can add code here to test your classes or run your application.
        System.out.println("Test Driver is running.");

        // Create dealer catalog instance
        DealerCatalog dealerCatalog = new DealerCatalog();

        // Add a dealer to the catalog
        Dealer dealer = new Dealer("D001", "Metro Auto");
        dealerCatalog.getDealers().add(dealer);

        // Print out the dealer information
        System.out.println("Dealer ID: " + dealer.getId());
        System.out.println("Dealer Name: " + dealer.getName());

        // Create a couple vehicles
        Sedan vehicle1 = new Sedan("V001", "Mazdaspeed6", "Mazda", 25000, dealer.getId(), LocalDateTime.now(), true);
        Pickup vehicle2 = new Pickup("V002", "Ford F-150", "Ford", 30000, dealer.getId(), LocalDateTime.now(), false);

        // Show vehicle data
        System.out.println("Vehicle ID: " + vehicle1.getVehicleId());
        System.out.println("Vehicle Model: " + vehicle1.getModel());
        System.out.println("Vehicle Manufacturer: " + vehicle1.getManufacturer());
        System.out.println("Vehicle Rented Status: " + vehicle1.getIsRentedOut());

        // Switch Dealer acquisition status to false
        dealer.setIsBuying(false);

        // Print out the dealer's buying status
        System.out.println("Dealer Buying Status: " + dealer.getIsBuying());

        // Add the vehicles to the dealer catalog
        DealerCatalog.getInstance().addVehicle(vehicle1);
        DealerCatalog.getInstance().addVehicle(vehicle2);

        // Print out the dealer catalog
        HashMap<String, Dealer> catalog = DealerCatalog.getInstance().getDealerCatalog();

        for (Map.Entry<String, Dealer> entry : catalog.entrySet()) {
            System.out.println("Dealer ID: " + entry.getKey() + ", Dealer Name: " + entry.getValue().getName());
        }

        // Print out the vehicles in the dealer catalog
        for (Map.Entry<String, Vehicle> entry : dealer.getVehicleCatalog().entrySet()) {
            System.out.println("Vehicle ID: " + entry.getKey() + ", Vehicle Model: " + entry.getValue().getModel());
        }
    }
}