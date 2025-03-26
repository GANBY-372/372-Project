/**
 * Dealer.java
 * @author B, G
 * This class represents a dealer in the motor dealership system.
 * ID is a unique identifier for the dealer and immutable.
 * vehicleAcquisitionEnabled is a boolean flag that indicates state.
 */

package edu.metrostate.ics372.ganby.dealer;

import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.beans.property.*;

import java.util.*;

public class Dealer {

    private final StringProperty id; // Unique identifier for the dealer
    private final StringProperty name; // Dealer name (optional)
    private final BooleanProperty isBuying; // Whether the dealer is buying vehicles
    private final HashMap<String, Vehicle> vehicleCatalog; // Dealer's vehicle inventory

    /**
     * Constructor for Dealer if name is not specified
     * @param id String
     */
    public Dealer(String id) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty("Dealer id#" +id);
        this.isBuying = new SimpleBooleanProperty(true);
        this.vehicleCatalog = new HashMap<>();
    }

    /**
     * Constructor for Dealer if name is specified
     * @param id String
     * @param name Name
     */
    public Dealer(String id, String name) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.isBuying = new SimpleBooleanProperty(true);
        this.vehicleCatalog = new HashMap<>();
    }

    /**
     * Get the dealer id
     * @return String dealer id
     */
    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    /**
     * Get the dealer name
     * @return String dealer name
     */
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Get the vehicle acquisition status
     * @return boolean
     */
    public boolean getIsVehicleAcquisitionEnabled() {
        return isBuying.get();
    }

    public BooleanProperty isAcquisitionEnabledProperty() {
        return isBuying;
    }

    /**
     * Get the vehicle collection
     * @return HashMap<String, Vehicle> vehicleCatalog
     */
    public HashMap<String, Vehicle> getVehicleCatalog() {
        return vehicleCatalog;
    }

    /**
     * Set the dealer acquisition status to true / enabled
     * @param dealerId String
     */
    public void enableVehicleAcquisition(String dealerId) {
        if (isBuying.get()) {
            System.out.println("Vehicle acquisition already enabled for dealer id #" + dealerId + ".");
        } else {
            this.isBuying.set(true);
            System.out.println("Successfully enabled vehicle acquisition for dealer id #" + dealerId + ".");
        }
    }

    /**
     * Set the dealer acquisition status to false / disabled
     * @param dealerId String
     */
    public void disableVehicleAcquisition(String dealerId) {
        if (!isBuying.get()) {
            System.out.println("Vehicle acquisition already disabled for dealer id #" + dealerId + ".");
        } else {
            this.isBuying.set(false);
            System.out.println("Successfully disabled vehicle acquisition for dealer id #" + dealerId + ".");
        }
    }

    /**
     * Find a vehicle by its id
     * @param vehicleId String
     * @return Vehicle
     */
    public Vehicle findVehicleById(String vehicleId) {
        return vehicleCatalog.get(vehicleId);
    }

    /**
     * Add a vehicle to the dealer's vehicle collection
     * @param vehicle Vehicle
     */
    public void addVehicle(Vehicle vehicle) {
        vehicleCatalog.put(vehicle.getVehicleId(), vehicle);
    }

    /**
     * Adds mutliple vehicles at once. Will be used for transferring vehicles from one dealer to another.
     * @param vehicles arraylist containting vehicles
     */
    public void addVehicles(ArrayList<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            addVehicle(vehicle);
        }
    }

    /**
     * Transfer a vehicle from this dealer to another
     */
    public boolean transferVehicle(Vehicle vehicle, Dealer dealer) {
        if (vehicle == null || dealer == null) return false;
        if (!vehicleCatalog.containsKey(vehicle.getVehicleId())) return false;

        vehicleCatalog.remove(vehicle.getVehicleId());
        vehicle.setDealer(dealer);
        dealer.getVehicleCatalog().put(vehicle.getVehicleId(), vehicle);

        System.out.println("Transferred vehicle " + vehicle.getVehicleId() + " to dealer " + dealer.getName());
        return true;
    }

    /**
     * Equals method to compare two dealers
     * @param object Object
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null || !(object instanceof Dealer dealer)) return false;
        return getId().equals(dealer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s |", getId(), getIsVehicleAcquisitionEnabled() ? "Yes" : "No");
    }

    public ArrayList<Vehicle> getRentedOutVehicles() {
        ArrayList<Vehicle> rentedOutVehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicleCatalog.values()) {
            if (vehicle.getIsRentedOut()) {
                rentedOutVehicles.add(vehicle);
            }
        }
        return rentedOutVehicles;
    }

    public HashMap<String, Vehicle> getVehiclesByType(String type) {
        HashMap<String, Vehicle> result = new HashMap<>();
        for (Vehicle vehicle : vehicleCatalog.values()) {
            if (vehicle.getType().equalsIgnoreCase(type)) {
                result.put(vehicle.getVehicleId(), vehicle);
            }
        }
        return result;
    }

    public boolean transferVehicleSet(Set<Vehicle> vehicles, Dealer dealer) {
        if (vehicles == null || dealer == null) return false;
        for (Vehicle vehicle : vehicles) {
            if (!transferVehicle(vehicle, dealer)) return false;
        }
        return true;
    }
}