package edu.metrostate.ics372.motordealership.dealer;


import edu.metrostate.ics372.motordealership.vehicle.Vehicle;
import edu.metrostate.ics372.motordealership.vehicle.VehicleCatalog;

import java.util.Objects;
import java.util.Set;

public class Dealer {

    private final int id;
    private boolean vehicleAcquisitionEnabled;

    public Dealer(int id) {
        this.id = id;
        this.vehicleAcquisitionEnabled = false;
    }

    public int getId() { return id; }

    public boolean vehicleAcquisitionEnabled () { return vehicleAcquisitionEnabled; }

    public void setVehicleAcquisitionEnabled (boolean isbuyingVehicles) { this.vehicleAcquisitionEnabled = vehicleAcquisitionEnabled; }

    public Set<Vehicle> getVehicles () {
        return VehicleCatalog.getInstance().filterByDealer(this);
    }

    public Vehicle findVehicleById (String vehicleId) {
        return VehicleCatalog.getInstance().findDealerVehicleById(this, vehicleId);
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
        String buyinEnabledString = vehicleAcquisitionEnabled ? " is buying vehicles." : " is not buying vehicles.";
        return getClass().getSimpleName() + " id:" + id + buyinEnabledString;
    }
}