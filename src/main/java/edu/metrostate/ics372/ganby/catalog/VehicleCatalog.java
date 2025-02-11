/**
 * VehicleCatalog.java
 * @author B, G
 * This is a singleton class. Maintains a collection of Vehicle objects.
 * Used to manage the collection of vehicles in the system during runtime.
 * Singleton pattern ensures only one instance of the class is created.
 */
package edu.metrostate.ics372.ganby.catalog;

import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import edu.metrostate.ics372.ganby.vehicle.Vehicles;

public class VehicleCatalog {

    // Singleton instance
    private static VehicleCatalog instance;

    // Collection of vehicles
    private final Vehicles vehicles;

    // Private constructor for singleton
    private VehicleCatalog () { this.vehicles = new Vehicles(); }

    // Public static method to access instance
    public static VehicleCatalog getInstance() {
        if (instance == null) {
            instance = new VehicleCatalog();
        }
        return instance;
    }

    //Method to print out list of all Vehicles
    public void printAllVehicles(){

        System.out.println("Vehicle Catalog:");
        System.out.println("------------------------------------------------------------------\n");
        for (Vehicle vehicle : this.getVehicles().getVehicles()) { // It works, don't worry bout it...
            System.out.printf("| %-10s | %-15s | %-12s | $%-10.2f | %-8d | %-20s |%n",
                    vehicle.getClass().getSimpleName(),  // Type
                    vehicle.getModel(),                  // Model
                    vehicle.getManufacturer(),           // Manufacturer
                    vehicle.getPrice(),                  // Price
                    vehicle.getDealer().getId(),         // Dealer ID
                    vehicle.getAcquisitionDate());       // Acquisition Date
        }

        System.out.println("------------------------------------------------------------------");
    }

    // Public method to get vehicles
    public Vehicles getVehicles() { return vehicles; }

}