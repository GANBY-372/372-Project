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

    private final int id;
    private boolean isVehicleAcquisitionEnabled;

    public Dealer(int id) {
        this.id = id;
        this.isVehicleAcquisitionEnabled = false;
    }

    public int getId() { return id; }

    public boolean isVehicleAcquisitionEnabled () { return isVehicleAcquisitionEnabled; }

    public void setVehicleAcquisitionEnabled (boolean isbuyingVehicles) { this.isVehicleAcquisitionEnabled = isVehicleAcquisitionEnabled; }

    public Set<Vehicle> getVehicles () {
        return VehicleCatalog.getInstance().getVehicles().filterByDealer(this);
    }

    public Vehicle findVehicleById (String vehicleId) {
        return VehicleCatalog.getInstance().getVehicles().findDealerVehicleById(this, vehicleId);
    }

    @Override
    public boolean equals (Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Dealer dealer) {
            return id == dealer.getId();
        }
        return false;
    }

    @Override
    public int hashCode () {
        return Objects.hashCode(id);
    }

    @Override
    public String toString () {
        String buyinEnabledString = isVehicleAcquisitionEnabled ? " is buying vehicles." : " is not buying vehicles.";
        return getClass().getSimpleName() + " id:" + id + buyinEnabledString;
    }
}