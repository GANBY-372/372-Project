/**
 * Dealer.java
 * @author B, G
 * This class represents a dealer in the motor dealership system.
 * ID is a unique identifier for the dealer and immutable.
 * vehicleAcquisitionEnabled is a boolean flag that indicates state.
 */

package edu.metrostate.ics372.ganby.dealer;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;

import java.util.HashMap;
import java.util.Objects;

public class Dealer {

    // Getter for dealer id
    // Instance variables
    private String name;
    private final String id;    //id is string because some IDs may contain letters
    private boolean isVehicleAcquisitionEnabled;
    // Getter for vehicles associated with dealer, Returns a set of vehicle objects
    private HashMap<String, Vehicle> vehicleCatalog;

    // TODO: Implement automatic naming if dealer name is not specified.
    /**
     * Constructor for Dealer if name is not specified
     * @param id String
     */
    public Dealer(String id) {
        this.id = id;
        this.isVehicleAcquisitionEnabled = true;
        vehicleCatalog = new HashMap<>();
        name = null;
    }

    /**
     * Constructor for Dealer if name is specified
     * @param id String
     * @param name Name
     */
    public Dealer(String id, String name) {
        this.id = id;
        this.isVehicleAcquisitionEnabled = true;
        vehicleCatalog = new HashMap<>();
        this.name = name;
    }


    /**
     * Get the dealer id
     * @return String dealer id
     */
    public String getDealerId(){
        return id;
    }

    /**
     * Get the dealer name
     * @return String dealer name
     */     public String getDealerName(){
        return name;
    }


    /**
     * Get the vehicle acquisition status
     * @return boolean
     */
    public boolean getIsVehicleAcquisitionEnabled() {
        return isVehicleAcquisitionEnabled;
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
        if (isVehicleAcquisitionEnabled) {
            System.out.println("Vehicle acquisition already enabled for dealer id #" + dealerId + ".");
        } else{
            this.isVehicleAcquisitionEnabled = true;
            System.out.println("Successfully enabled vehicle acquisition for dealer id #" + dealerId + ".");
        }
    }

    /**
     * Set the dealer acquisition status to false / disabled
     * @param dealerId String
     */
    public void disableVehicleAcquisition(String dealerId) {
        if (!isVehicleAcquisitionEnabled) {
            System.out.println("Vehicle acquisition already disabled for dealer id #" + dealerId + ".");
        } else{
            this.isVehicleAcquisitionEnabled = false;
            System.out.println("Successfully disabled vehicle acquisition for dealer id #" + dealerId + ".");
        }
    }

    /**
     * Find a vehicle by its id
     * @param vehicleId String
     * @return Vehicle
     */
    public Vehicle findVehicleById (String vehicleId) {
        return getVehicleCatalog().get(vehicleId);
    }



    /**
     * Add a vehicle to the dealer's vehicle collection
     * @param vehicle Vehicle
     */
    public void addVehicle (Vehicle vehicle) {
        vehicleCatalog.put(vehicle.getVehicleId(), vehicle);
    }



    /**
     * Equals method to compare two dealers
     * @param object Object
     * @return boolean
     */
    @Override
    public boolean equals (Object object) {
        if (object == this) return true;        // If object is compared with itself return true
        if (object == null) return false;       // If object is null return false
        if (object instanceof Dealer dealer) {  // If neither are true, check if object is an instance of Dealer
            return id.equals(dealer.getDealerId()) ;        // Is object a dealer with same id?
        }
        return false;                           // If none of the above, return false
    }


    /**
     * Hashcode is used to determine the location of an object in a hash table
     * @return int
     */
    @Override
    public int hashCode () {
        return Objects.hashCode(id);
    }


    /**
     * String representation of a dealer
     * @return String
     */
    @Override
    public String toString() {
        String acquisitionStatus = isVehicleAcquisitionEnabled ? "Yes" : "No";
        return String.format("| %-10d | %-20s |", Integer.parseInt(id), acquisitionStatus);
    }
}