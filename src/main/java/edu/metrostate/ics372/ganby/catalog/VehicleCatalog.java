/**
 * VehicleCatalog.java
 * @author B, G
 * This is a singleton class. Maintains a collection of Vehicle objects.
 * Used to manage the collection of vehicles in the system during runtime.
 * Singleton pattern ensures only one instance of the class is created.
 */
package edu.metrostate.ics372.ganby.catalog;

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

    // Public method to get vehicles
    public Vehicles getVehicles() { return vehicles; }
//    private List<Vehicle> vehicles;

//    private VehicleCatalog() {
//        this.vehicles = new ArrayList<>();
//    }
//
//    public static VehicleCatalog getInstance () {
//        if (instance == null) {
//            return new VehicleCatalog();
//        }
//        return instance;
//    }
//
//    public int size() {
//        return vehicles.size();
//    }
//
//    public List<Vehicle> getVehicles () {
//        return vehicles;
//    }
//
//    public void addVehicle (Vehicle vehicle) throws IllegalAccessException {
//        if (vehicle== null)
//            throw new IllegalAccessException("Cannot add null vehicleEntity to collection");
//        System.out.println("adding " + vehicle.toString() + " to the catalog");
//        this.vehicles.add(vehicle);
//        System.out.println(this.vehicles.size());
//    }
//
//    public void removeVehicle (String id) {
//        Vehicle vehicle = findById(id);
//        if (vehicle != null) {
//            vehicles.remove(vehicle);
//        }
//    }
//
//    public Vehicle findById (String id) {
//        for (Vehicle vehicle : vehicles) {
//            if (id.equalsIgnoreCase(vehicle.getId())) return vehicle;
//        }
//        return null;
//    }
//
//    public Set<Vehicle> filterByDealer (Dealer dealer) {
//        Set<Vehicle> matches = new HashSet<>();
//        for (Vehicle vehicle : vehicles) {
//            if (vehicle.getDealer().equals(dealer)) {
//                matches.add(vehicle);
//            }
//        }
//        return matches;
//    }
//
//    public Vehicle findDealerVehicleById(Dealer dealer, String vehicleId) {
//        if (dealer == null)
//            throw new IllegalArgumentException("The dealer cannot be null");
//        for (Vehicle vehicle : filterByDealer(dealer)) {
//            if (vehicle.getId().equalsIgnoreCase(vehicleId)) return vehicle;
//        }
//        return null;
//    }
}