package edu.metrostate.ics372.GANBY.Dealer;

import edu.metrostate.ics372.GANBY.Vehicle.Vehicle;

import java.util.HashMap;
import java.util.Objects;

public class Dealer {

    private final int id;
    private boolean vehicleAcquisitionEnabled;
    private  HashMap<Integer, Vehicle> vehicleCollection;

    public Dealer(int id) {
        this.id = id;
        this.vehicleAcquisitionEnabled = true;
        vehicleCollection = new HashMap<>();
    }

    public int getId() { return id; }

    public boolean getVehicleAcquisitionEnabled() { return vehicleAcquisitionEnabled; }

    public void enableVehicleAcquisition() {
        this.vehicleAcquisitionEnabled = true;
    }

    public void disableVehicleAcquisition() {
        this.vehicleAcquisitionEnabled = false;
    }

    //Get all vehicles of a given dealer
    public HashMap<Integer,Vehicle> getVehicles () {
        return vehicleCollection;
    }

    //Add the vehicle to vehicle collection
    public boolean addVehicle (Vehicle vehicle) {
        //Adds vehicle to collection if it doesn't already exist in it
        if(vehicleCollection.containsKey(vehicle.getVehicleId())) {
            System.out.println("Vehicle already exists");
            return false;
        } else{
            vehicleCollection.put(vehicle.getVehicleId(), vehicle);
            return true;
        }

    }

    public Vehicle findVehicleById(int vehicleId) {
        //Find vehicle by looping through dealer collection of dealers and checking for vehicleId
        return vehicleCollection.get(vehicleId);
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
        return "Id: " + id + "\nVehicle Acquisition Status: " + vehicleAcquisitionEnabled + "\n";
    }
}
