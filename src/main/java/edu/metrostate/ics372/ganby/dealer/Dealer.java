/**
 * Dealer.java
 * @author B, G
 * This class represents a dealer in the motor dealership system.
 * ID is a unique identifier for the dealer and immutable.
 * vehicleAcquisitionEnabled is a boolean flag that indicates state.
 */

package edu.metrostate.ics372.ganby.dealer;

import edu.metrostate.ics372.ganby.catalog.VehicleCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import lombok.Getter;

import java.util.Objects;
import java.util.Set;

public class Dealer {

    // Getter for dealer id
    // Instance variables
    @Getter
    private final int id;
    private boolean vehicleAcquisitionEnabled;

    // Constructor, initializes dealer with id
    // Vehicle acquisition is disabled by default
    public Dealer(int id) {
        this.id = id;
        this.vehicleAcquisitionEnabled = true;
    }

    // Getter for vehicle acquisition state
    public boolean vehicleAcquisitionEnabled () {
        return vehicleAcquisitionEnabled;
    }

    // set vehicle acquisition state to true
    public void enableVehicleAcquisition(int id) {
        if (vehicleAcquisitionEnabled) {
            System.out.println("Vehicle acquisition already enabled for dealer id #" + id + ".");
        } else{
            this.vehicleAcquisitionEnabled = true;
            System.out.println("Successfully enabled vehicle acquisition for dealer id #" + id + ".");
        }
    }

    // set vehicle acquisition state to false
    public void disableVehicleAcquisition() {
        if (!vehicleAcquisitionEnabled) {
            System.out.println("Vehicle acquisition already disabled for dealer id #" + id + ".");
        } else{
            this.vehicleAcquisitionEnabled = true;
            System.out.println("Successfully disabled vehicle acquisition for dealer id #" + id + ".");
        }
    }

    // Getter for vehicles associated with dealer, Returns a set of vehicle objects
    public Set<Vehicle> getVehicles () {
        return VehicleCatalog.getInstance().getVehicles().filterByDealer(this);
    }

    // Find vehicle by id, returns a vehicle object
    public Vehicle findVehicleById (String vehicleId) {
        return VehicleCatalog.getInstance().getVehicles().findDealerVehicleById(this, vehicleId);
    }

    // Equals method
    @Override
    public boolean equals (Object object) {
        if (object == this) return true;        // If object is compared with itself return true
        if (object == null) return false;       // If object is null return false
        if (object instanceof Dealer dealer) {  // If neither are true, check if object is an instance of Dealer
            return id == dealer.getId();        // Is object a dealer with same id?
        }
        return false;                           // If none of the above, return false
    }

    // Hashcode is used to determine the location of an object in a hash table
    // returns the dealers index in a hash table
    @Override
    public int hashCode () {
        return Objects.hashCode(id);
    }

    // toString method
    //Asked CHATGPT 4.0 to format the toString to make a clean output
    @Override
    public String toString() {
        String acquisitionStatus = vehicleAcquisitionEnabled ? "Yes" : "No";
        return String.format("| %-10d | %-20s |", id, acquisitionStatus);
    }
}