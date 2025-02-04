package edu.metrostate.ics372.motordealership.vehicle;


import java.util.HashSet;
import java.util.Set;

public class VehicleCatalog {

    private static VehicleCatalog instance;
    private final Vehicles vehicles;

    private VehicleCatalog () { this.vehicles = new Vehicles(); }

    public static VehicleCatalog getInstance() {
        if (instance == null) {
            instance = new VehicleCatalog();
        }
        return instance;
    }

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