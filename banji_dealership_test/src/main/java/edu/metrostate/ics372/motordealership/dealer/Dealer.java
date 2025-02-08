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

    // Class Methods
    /**
     * Return the dealer's ID number
     */
    public int getId() { return id; }

    /**
     * Check if acquiring vehicles is enabled
     */
    public boolean isVehicleAcquisitionEnabled () { return isVehicleAcquisitionEnabled; }

    /**
     * Return set of vehicles at this dealer
     */
    public Set<Vehicle> getVehicles () {
        return VehicleCatalog.getInstance().getVehicles().filterByDealer(this);
    }

    /**
     * find a vehicle by ID
     */
    public Vehicle findVehicleById (String vehicleId) {
        return VehicleCatalog.getInstance().getVehicles().findDealerVehicleById(this, vehicleId);
    }

    /**
     * Overriding equals method
     */
    @Override
    public boolean equals (Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Dealer dealer) {
            return id == dealer.getId();
        }
        return false;
    }

    /**
     * Overriding hashCode method
     */
    @Override
    public int hashCode () {
        return Objects.hashCode(id);
    }

    /**
     * Overriding toString method.
     */
    @Override
    public String toString () {
        String buyinEnabledString = isVehicleAcquisitionEnabled ? " is buying vehicles." : " is not buying vehicles.";
        return getClass().getSimpleName() + " id:" + id + buyinEnabledString;
    }
}