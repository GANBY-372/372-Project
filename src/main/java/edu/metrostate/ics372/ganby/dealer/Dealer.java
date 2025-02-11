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
import java.util.Objects;
import java.util.Set;

public class Dealer {

    // Instance variables
    private final int id;
    private boolean vehicleAcquisitionEnabled;

    // Constructor, initializes dealer with id
    // Vehicle acquisition is disabled by default
    public Dealer(int id) {
        this.id = id;
        this.vehicleAcquisitionEnabled = true;
    }

    // Getter for dealer id
    public int getId() { return id; }

    // Getter for vehicle acquisition state
    public boolean vehicleAcquisitionEnabled () {
        return vehicleAcquisitionEnabled;
    }

    // Setter for vehicle acquisition state
    public void setVehicleAcquisitionEnabled (boolean isbuyingVehicles) {
        this.vehicleAcquisitionEnabled = vehicleAcquisitionEnabled;
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
    @Override
    public String toString () {
        String buyinEnabledString = vehicleAcquisitionEnabled ? " is buying vehicles." : " is not buying vehicles.";
        return getClass().getSimpleName() + " id:" + id + buyinEnabledString;
    }
}