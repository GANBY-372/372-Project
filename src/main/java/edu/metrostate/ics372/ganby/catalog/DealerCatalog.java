/**
 * DealerCatalog.java
 *
 * @author B, G
 * This is a singleton class. Maintains a collection of Dealer objects.
 * Used to manage the collection of dealers in the system during runtime.
 * Singleton pattern ensures only one instance of the class is created.
 */

package edu.metrostate.ics372.ganby.catalog;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class DealerCatalog {

    private static DealerCatalog instance;
    private HashMap<String, Dealer> dealerCollection;

    private DealerCatalog() {
        dealerCollection = new HashMap<>();
    }

    //Singleton implementation
    public static DealerCatalog getInstance() {
        if (instance == null) {
            instance = new DealerCatalog();
        }
        return instance;
    }

    // Public method to get dealers
    public HashMap<String, Dealer> getDealerCollection() {
        return dealerCollection;
    }

    //returns a dealer object given an id.
    public Dealer findDealerById(String id) {
        return dealerCollection.get(id);
    }

    public void addVehicle(Vehicle vehicle) {

        if (vehicle == null) {    //Doesn't add vehicle if it's null
            return;

            //Checks if dealer exists. This first if-statement handles the case when dealer does not exist
        } else if (!dealerCollection.containsKey(vehicle.getDealerId())) {
            //Makes a new dealer if does not exist and adds it to hashmap
            dealerCollection.put(vehicle.getDealerId(), new Dealer(vehicle.getDealerId()));
            System.out.println("Dealer does not exist. Dealer has been added.");
            //Adds the vehicle to that dealer's vehicle collection
            dealerCollection.get(vehicle.getDealerId()).addVehicle(vehicle);
            System.out.println("Vehicle id #" + vehicle.getVehicleId() + " added");

            //If dealer does exist then check if vehicle already exists in dealer's vehicle collection and check
            //if dealer's vehicle acquisition is enabled. This if statement handles the case when both conditions are
            //false.
        } else if (dealerCollection.get(vehicle.getDealerId()).getVehicleCollection().containsKey(vehicle.getVehicleId()) &&
                !dealerCollection.get(vehicle.getDealerId()).getIsVehicleAcquisitionStatus()) {
            System.out.println("Acquisition Disabled for dealer id #" + vehicle.getDealerId() +
                    " and vehicle id #" + vehicle.getVehicleId() + ", already exists in the collection.");

            //Checks acquisition status, if false then vehicle will not be added. This if-statement handles the case
            //when the vehicle already exists in the dealer's vehicle collection
        } else if (dealerCollection.get(vehicle.getDealerId()).getVehicleCollection().containsKey(vehicle.getVehicleId())) {
            System.out.println("Vehicle id #" + vehicle.getVehicleId() + " already exists in dealer's the collection.");
        } else if (!dealerCollection.get(vehicle.getDealerId()).getIsVehicleAcquisitionStatus()) {
            System.out.println("Acquisition Disabled for dealer id #" + vehicle.getDealerId());
            //All conditions are met and the vehicle is added
        } else {
            //adds vehicle to corresponding dealer
            dealerCollection.get(vehicle.getDealerId()).addVehicle(vehicle);
            System.out.println("Vehicle id #" + vehicle.getVehicleId() + " added");
        }

    }

    //Requirement #17: Allow user to modify given vehicle’s price
    public void modifyVehiclePrice(String VehicleId, double price) {
        for (Dealer dealer : dealerCollection.values()) {
            if (dealer.getVehicleCollection().containsKey(VehicleId)) {
                dealer.getVehicleCollection().get(VehicleId).setPrice(price);
            }
        }
    }

    // Requirement #18: Allow user to view all vehicles sorted by price
    public void printAllVehiclesByPrice() {
        System.out.println("Vehicles by Price:");

        for (Dealer dealer : this.getDealerCollection().values()) {
            System.out.printf("%nDealer ID #%d:%n", Integer.parseInt(dealer.getDealerId())); // Print Dealer ID above the table

            System.out.println("-------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-15s | %-12s | %-10s | %-20s |%n",
                    "Type", "Model", "Manufacturer", "Price", "Acquisition Date");
            System.out.println("-------------------------------------------------------------------------------");

            // Convert the vehicle collection to a list for sorting
            List<Vehicle> sortedVehicles = new ArrayList<>(dealer.getVehicleCollection().values());

            // Sort the list by price in ascending order
            sortedVehicles.sort(Comparator.comparingDouble(Vehicle::getPrice));

            // Print the sorted list
            for (Vehicle vehicle : sortedVehicles) {
                System.out.printf("| %-10s | %-15s | %-12s | $%-9.2f | %-20s |%n",
                        vehicle.getClass().getSimpleName(),  // Type
                        vehicle.getModel(),                  // Model
                        vehicle.getManufacturer(),           // Manufacturer
                        vehicle.getPrice(),                  // Price (Floating-Point)
                        vehicle.getAcquisitionDate());       // Acquisition Date
            }

            System.out.println("-------------------------------------------------------------------------------");
        }
    }

    // Requirement #19   Allow user to view all vehicles within a certain price range
    public void printVehiclesByPriceRange(double minPrice, double maxPrice) {
        System.out.printf("Vehicles within price range: $%.2f - $%.2f%n", minPrice, maxPrice);
        boolean found = false;

        for (Dealer dealer : this.getDealerCollection().values()) {
            List<Vehicle> filteredVehicles = dealer.getVehicleCollection().values().stream()
                    .filter(vehicle -> vehicle.getPrice() >= minPrice && vehicle.getPrice() <= maxPrice)
                    .sorted(Comparator.comparingDouble(Vehicle::getPrice))
                    .toList();

            if (!filteredVehicles.isEmpty()) {
                found = true;
                System.out.printf("%nDealer ID #%d:%n", Integer.parseInt(dealer.getDealerId())); // Print Dealer ID above the table
                System.out.println("-------------------------------------------------------------------------------");
                System.out.printf("| %-10s | %-15s | %-12s | %-10s | %-20s |%n",
                        "Type", "Model", "Manufacturer", "Price", "Acquisition Date");
                System.out.println("-------------------------------------------------------------------------------");

                for (Vehicle vehicle : filteredVehicles) {
                    System.out.printf("| %-10s | %-15s | %-12s | $%-9.2f | %-20s |%n",
                            vehicle.getClass().getSimpleName(),  // Type
                            vehicle.getModel(),                  // Model
                            vehicle.getManufacturer(),           // Manufacturer
                            vehicle.getPrice(),                  // Price (Floating-Point)
                            vehicle.getAcquisitionDate());       // Acquisition Date
                }
                System.out.println("-------------------------------------------------------------------------------");
            }
        }
        if (!found) {
            System.out.println("No vehicles found within the specified price range.");
        }
    }

    //Requirement #20: Allow user to view all dealers that have certain range of vehicles in inventory
    public void printDealersByInventoryRange(int minVehicles, int maxVehicles) {
        System.out.printf("Dealers with inventory size between %d and %d:%n", minVehicles, maxVehicles);
        boolean found = false;

        System.out.println("------------------------------------------------");
        System.out.printf("| %-10s | %-20s | %-15s |%n", "Dealer ID", "Dealer Name", "Vehicle Count");
        System.out.println("------------------------------------------------");

        for (Dealer dealer : this.getDealerCollection().values()) {
            int vehicleCount = dealer.getVehicleCollection().size();
            if (vehicleCount >= minVehicles && vehicleCount <= maxVehicles) {
                found = true;
                System.out.printf("| %-10s | %-20s | %-15d |%n",
                        dealer.getDealerId(),
                        dealer.getDealerName(),
                        vehicleCount);
            }
        }

        System.out.println("------------------------------------------------");

        if (!found) {
            System.out.println("No dealers found within the specified inventory range.");
        }
    }


    //Method to print out list of all Dealers
    public void printAllDealers() {
        System.out.println("-------------------------------------------");
        System.out.printf("| %-10s | %-20s |%n", "Dealer ID", "Acquisition Enabled");
        System.out.println("-------------------------------------------");
        for (Dealer dealer : this.dealerCollection.values()) {
            System.out.println(dealer);
        }
        System.out.println("-------------------------------------------");
    }

    public void enableDealerAcquisition(String id) {
        if (this.getDealerCollection().get(id) == null) {
            System.out.println("Dealer does not exist.");
        } else {
            this.getDealerCollection().get(id).enableVehicleAcquisition(id);
        }
    }

    public void disableDealerAcquisition(String id) {
        if (this.getDealerCollection().get(id) == null) {
            System.out.println("Dealer does not exist.");
        } else {
            this.getDealerCollection().get(id).disableVehicleAcquisition(id);
        }
    }

    //Asked CHATGPT 4.0 to write this method for me. It formatted it and figured out the logic.
//Prints all Vehicles in the Catalog sorted by Dealer
    public void printAllVehiclesById() {
        System.out.println("Vehicles by Dealer:");

        for (Dealer dealer : this.getDealerCollection().values()) {
            System.out.printf("%nDealer ID #%d:%n", Integer.parseInt(dealer.getDealerId())); // Print Dealer ID above the table

            System.out.println("-------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-15s | %-12s | %-10s | %-20s |%n",
                    "Type", "Model", "Manufacturer", "Price", "Acquisition Date");
            System.out.println("-------------------------------------------------------------------------------");

            for (Vehicle vehicle : dealer.getVehicleCollection().values()) {
                System.out.printf("| %-10s | %-15s | %-12s | $%-9.2f | %-20s |%n",
                        vehicle.getClass().getSimpleName(),  // Type
                        vehicle.getModel(),                  // Model
                        vehicle.getManufacturer(),           // Manufacturer
                        vehicle.getPrice(),                  // Price (Floating-Point)
                        vehicle.getAcquisitionDate());       // Acquisition Date
            }

            System.out.println("-------------------------------------------------------------------------------");
        }
    }

    //Asked ChatGPT 4.0 to make a clean and formatted output for vehicles
//This method get all vehicles of a given dealer
    public void getAllVehiclesOfDealer(String id) {
        System.out.println("-------------------------------------------------------------");
        System.out.printf("| %-10s | %-15s | %-10s | %-10s | %-15s |\n",
                "Type", "Model", "Manufacturer", "Price", "Acquisition Date");
        System.out.println("-------------------------------------------------------------");
        for (Vehicle v : this.getDealerCollection().get(id).getVehicleCollection().values()) {
            System.out.printf("| %-10s | %-15s | %-10s | $%-9.2f | %-15s |\n",
                    v.getClass().getSimpleName(), // Sedan, SUV, etc.
                    v.getModel(),
                    v.getManufacturer(),
                    v.getPrice(),
                    v.getAcquisitionDate());
        }
        System.out.println("-------------------------------------------------------------");
    }

    public int amountOfAllDealers() {
        return this.dealerCollection.size();
    }

    public int amountOfAllVehicles() {
        int totalAmount = 0;
        for (Dealer dealer : this.getDealerCollection().values()) {
            totalAmount += dealer.getVehicleCollection().size();
        }
        return totalAmount;
    }
}
