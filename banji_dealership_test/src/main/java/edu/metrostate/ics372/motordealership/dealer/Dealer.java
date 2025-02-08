/*
    * Dealer.java
    * branched from Banji Dealership Test
    *
    * This class represents a dealer in the motor dealership system.
    * ID is a unique identifier for the dealer and immutable.
    * isVehicleAcquisitionEnabled is a boolean flag that indicates state.
    *
    * Working on commenting and some edits to the code.
    *
*/

package edu.metrostate.ics372.motordealership.dealer;

import edu.metrostate.ics372.motordealership.vehicle.Vehicle;
import edu.metrostate.ics372.motordealership.vehicle.VehicleCatalog;

import java.util.Objects;
import java.util.Set;

public class Dealer {

    // Instance Variables
    private final int id;
    private boolean isVehicleAcquisitionEnabled;

    // Constructor
    public Dealer(int id) {
        this.id = id;
        this.isVehicleAcquisitionEnabled = false;
    }

    // Return the dealer ID number
    public int getId() { return id; }

    // Return the state of vehicle acquisition
    public boolean isVehicleAcquisitionEnabled () { return isVehicleAcquisitionEnabled; }

    // Get the set of vehicle objects
    public Set<Vehicle> getVehicles () {
        return VehicleCatalog.getInstance().getVehicles().filterByDealer(this);
    }

    // Find a vehicle by ID
    public Vehicle findVehicleById (String vehicleId) {
        return VehicleCatalog.getInstance().getVehicles().findDealerVehicleById(this, vehicleId);
    }

    // Equals method to compare Dealer objects
    @Override
    public boolean equals (Object object) {
        if (object == this) return true;        // if object is the same as this object, return true
        if (object == null) return false;       // if object is null, return false
        if (object instanceof Dealer dealer) {  // if object is an instance of Dealer
            return id == dealer.getId();
        }
        return false;
    }

    // Hashcode method to generate a hashcode for the Dealer object
    @Override
    public int hashCode () {
        return Objects.hashCode(id);
    }

    // To string method to return a string representation of the Dealer object
    @Override
    public String toString () {
        String buyinEnabledString = isVehicleAcquisitionEnabled ? " is buying vehicles." : " is not buying vehicles.";
        return getClass().getSimpleName() + " id:" + id + buyinEnabledString;
    }
}