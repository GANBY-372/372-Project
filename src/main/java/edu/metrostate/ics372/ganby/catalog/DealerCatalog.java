/**
 * DealerCatalog.java
 * This is a singleton class. Maintains a collection of Dealer objects.
 * Used to manage the collection of dealers in the system during runtime.
 * Singleton pattern ensures only one instance of the class is created.
 */

package edu.metrostate.ics372.ganby.catalog;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class DealerCatalog {

    private static DealerCatalog instance;

    private HashMap<String, Dealer> dealerCatalog;

    /**
     * Private constructor to prevent instantiation
     */
    private DealerCatalog() {
        dealerCatalog = new HashMap<>();
    }

    /**
     * Method to get the instance of the DealerCatalog
     * @return instance of DealerCatalog
     */
    public static DealerCatalog getInstance() {
        if (instance == null) {
            instance = new DealerCatalog();
        }
        return instance;
    }

    /**
     * Method to get the dealer collection / collection
     * @return HashMap of dealers
     */
    public HashMap<String, Dealer> getDealerCatalog() {
        return dealerCatalog;
    }

    /**
     * Returns a dealer object using the dealer id
     * @param id String dealer id
     * @return Dealer object
     */
    public Dealer getDealerWithId(String id) {
        return dealerCatalog.get(id);
    }


    /**
     * Add a vehicle to the dealer collection
     * @param vehicle Vehicle object
     */
    public void addVehicle(Vehicle vehicle) {

        if (vehicle == null) {    //Doesn't add vehicle if it's null
            return;

            //Checks if dealer exists. This first if-statement handles the case when dealer does not exist
        } else if (!dealerCatalog.containsKey(vehicle.getDealerId())) {
            //Makes a new dealer if does not exist and adds it to hashmap
            dealerCatalog.put(vehicle.getDealerId(), new Dealer(vehicle.getDealerId()));
            System.out.println("Dealer does not exist. Dealer has been added.");
            //Adds the vehicle to that dealer's vehicle collection
            dealerCatalog.get(vehicle.getDealerId()).addVehicle(vehicle);
            System.out.println("Vehicle id #" + vehicle.getId() + " added");

            //If dealer does exist then check if vehicle already exists in dealer's vehicle collection and check
            //if dealer's vehicle acquisition is enabled. This if statement handles the case when both conditions are
            //false.
        } else if (dealerCatalog.get(vehicle.getDealerId()).getVehicleCatalog().containsKey(vehicle.getId()) &&
                !dealerCatalog.get(vehicle.getDealerId()).getIsBuying()) {
            System.out.println("Acquisition Disabled for dealer id #" + vehicle.getDealerId() +
                    " and vehicle id #" + vehicle.getId() + ", already exists in the collection.");

            //Checks acquisition status, if false then vehicle will not be added. This if-statement handles the case
            //when the vehicle already exists in the dealer's vehicle collection
        } else if (dealerCatalog.get(vehicle.getDealerId()).getVehicleCatalog().containsKey(vehicle.getId())) {
            System.out.println("Vehicle id #" + vehicle.getId() + " already exists in dealer's the collection.");
        } else if (!dealerCatalog.get(vehicle.getDealerId()).getIsBuying()) {
            System.out.println("Acquisition Disabled for dealer id #" + vehicle.getDealerId());
            //All conditions are met and the vehicle is added
        } else {
            //adds vehicle to corresponding dealer
            dealerCatalog.get(vehicle.getDealerId()).addVehicle(vehicle);
            System.out.println("Vehicle id #" + vehicle.getId() + " added");
        }

    }

    /**
     * Requirement #17: Method to modify a given vehicle's price
     * @param VehicleId id of vehicle whose price will be modified
     * @param  price new price that is specified
     */
    public void modifyVehiclePrice(String VehicleId, double price) {
        for (Dealer dealer : dealerCatalog.values()) {
            if (dealer.getVehicleCatalog().containsKey(VehicleId)) {
                dealer.getVehicleCatalog().get(VehicleId).setPrice(price);
            }
        }
    }


    /**
     * Requirement #18: Method to print all vehicle sorted by their prices
     */
    public void printAllVehiclesByPrice() {
        System.out.println("Vehicles by Price:");

        for (Dealer dealer : this.getDealerCatalog().values()) {
            System.out.printf("%nDealer ID #%d:%n", Integer.parseInt(dealer.getId())); // Print Dealer ID above the table

            System.out.println("-------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-15s | %-12s | %-10s | %-20s |%n",
                    "Type", "Model", "Manufacturer", "Price", "Acquisition Date");
            System.out.println("-------------------------------------------------------------------------------");

            // Convert the vehicle collection to a list for sorting
            List<Vehicle> sortedVehicles = new ArrayList<>(dealer.getVehicleCatalog().values());

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

    /**
     * Requirement #19: Method to print all vehicles that are with a certain price range specified
     * @param minPrice no. of min price specified
     * @param  maxPrice no. of max price specified
     */
    public void printVehiclesByPriceRange(double minPrice, double maxPrice) {
        System.out.printf("Vehicles within price range: $%.2f - $%.2f%n", minPrice, maxPrice);
        boolean found = false;

        for (Dealer dealer : this.getDealerCatalog().values()) {
            List<Vehicle> filteredVehicles = dealer.getVehicleCatalog().values().stream()
                    .filter(vehicle -> vehicle.getPrice() >= minPrice && vehicle.getPrice() <= maxPrice)
                    .sorted(Comparator.comparingDouble(Vehicle::getPrice))
                    .toList();

            if (!filteredVehicles.isEmpty()) {
                found = true;
                System.out.printf("%nDealer ID #%d:%n", Integer.parseInt(dealer.getId())); // Print Dealer ID above the table
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


    /**
     * Requirement #20: Method to print all dealers that have a certain range of vehicles.
     * @param minVehicles no. of min vehicles specified
     * @param  maxVehicles no. of max vehicles specified
     */
    public void printDealersByInventoryRange(int minVehicles, int maxVehicles) {
        System.out.printf("Dealers with inventory size between %d and %d:%n", minVehicles, maxVehicles);
        boolean found = false;

        System.out.println("------------------------------------------------");
        System.out.printf("| %-10s | %-20s | %-15s |%n", "Dealer ID", "Dealer Name", "Vehicle Count");
        System.out.println("------------------------------------------------");

        for (Dealer dealer : this.getDealerCatalog().values()) {
            int vehicleCount = dealer.getVehicleCatalog().size();
            if (vehicleCount >= minVehicles && vehicleCount <= maxVehicles) {
                found = true;
                System.out.printf("| %-10s | %-20s | %-15d |%n",
                        dealer.getId(),
                        dealer.getName(),
                        vehicleCount);
            }
        }

        System.out.println("------------------------------------------------");

        if (!found) {
            System.out.println("No dealers found within the specified inventory range.");
        }
    }



    /**
     * Method to print all dealers
     */
    public void printAllDealers() {
        System.out.println("-------------------------------------------");
        System.out.printf("| %-10s | %-20s |%n", "Dealer ID", "Acquisition Enabled");
        System.out.println("-------------------------------------------");
        for (Dealer dealer : this.dealerCatalog.values()) {
            System.out.println(dealer);
        }
        System.out.println("-------------------------------------------");
    }

    /**
     * Method to print all vehicles sorted by their dealers
     */
    public void printAllVehiclesByDealerId() {
        System.out.println("Vehicles by Dealer:");

        for (Dealer dealer : this.getDealerCatalog().values()) {
            System.out.printf("%nDealer ID #%d:%n", Integer.parseInt(dealer.getId())); // Print Dealer ID above the table

            System.out.println("-------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-15s | %-12s | %-10s | %-20s |%n",
                    "Type", "Model", "Manufacturer", "Price", "Acquisition Date");
            System.out.println("-------------------------------------------------------------------------------");

            for (Vehicle vehicle : dealer.getVehicleCatalog().values()) {
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

    /**
     * Method to print all vehicles of a dealer
     * @param id dealer id
     */
    public void printAllVehiclesOfDealer(String id) {
        System.out.println("-------------------------------------------------------------");
        System.out.printf("| %-10s | %-15s | %-10s | %-10s | %-15s |\n",
                "Type", "Model", "Manufacturer", "Price", "Acquisition Date");
        System.out.println("-------------------------------------------------------------");
        for (Vehicle v : this.getDealerCatalog().get(id).getVehicleCatalog().values()) {
            System.out.printf("| %-10s | %-15s | %-10s | $%-9.2f | %-15s |\n",
                    v.getClass().getSimpleName(), // Sedan, SUV, etc.
                    v.getModel(),
                    v.getManufacturer(),
                    v.getPrice(),
                    v.getAcquisitionDate());
        }
        System.out.println("-------------------------------------------------------------");
    }

    /**
     * Method to get the total number of dealers
     * @return int
     */
    public int amountOfAllDealers() {
        return this.dealerCatalog.size();
    }

    /**
     * Method to get the total number of vehicles
     * @return int
     */
    public int amountOfAllVehicles() {
        int totalAmount = 0;
        for (Dealer dealer : this.getDealerCatalog().values()) {
            totalAmount += dealer.getVehicleCatalog().size();
        }
        return totalAmount;
    }

    /**
     * Enable Dealer Acquisition with the dealer id
     * @param id dealer id
     */
    public void enableDealerAcquisition(String id) {
        if (this.getDealerCatalog().get(id) == null) {
            System.out.println("Dealer does not exist.");
        } else {
            this.getDealerCatalog().get(id).enableVehicleAcquisition(id);
        }
    }

    /**
     * Disable Dealer Acquisition with the dealer id
     * @param id String
     */
    public void disableDealerAcquisition(String id) {
        if (this.getDealerCatalog().get(id) == null) {
            System.out.println("Dealer does not exist.");
        } else {
            this.getDealerCatalog().get(id).disableVehicleAcquisition(id);
        }
    }

    public ObservableList<Dealer> getDealerWhoAreBuying() {
        Set<Dealer> matches = new HashSet<>();

        for (Dealer dealer : dealerCatalog.values()) {
            if (dealer.getIsBuying()) {
                System.out.println(dealer.getName() + "  is buying");
                matches.add(dealer);
            }
        }
        return FXCollections.observableArrayList(matches);
    }

    public Dealer getDealerByName (String name) {
        if (name == null) return null;
        for (Dealer dealer : dealerCatalog.values()) {
            if (dealer.getName().equalsIgnoreCase(name)) return dealer;
        }
        return null;
    }
}