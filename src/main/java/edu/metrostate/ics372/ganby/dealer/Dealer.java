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

    // TODO: Add Name attribute to Dealer
    // Instance variables
    private final String                id;
    private boolean                     isVehicleAcquisitionEnabled;
    private HashMap<String, Vehicle>    vehicleCollection;

    /**
     * Constructor for Dealer
     * @param id String
     */
    public Dealer(String id) {
        this.id = id;
        this.isVehicleAcquisitionEnabled = true;
        vehicleCollection = new HashMap<>();
    }

    /**
     * Get the dealer id
     * @return String dealer id
     */
    public String getDealerId(){
        return id;
    }

    // TODO: Change method to "getIsVehicleAcquisitionEnabled" for consistency with the attribute it gets
    /**
     * Get the vehicle acquisition status
     * @return boolean
     */
    public boolean getIsVehicleAcquisitionStatus() {
        return isVehicleAcquisitionEnabled;
    }

    // TODO: Drop this lombok functionality, we are not using it and IDE is throwing a notice
    /**
     * Get the vehicle collection
     * @return HashMap<String, Vehicle> vehicleCollection
     */
    public HashMap<String, Vehicle> getVehicleCollection() {
        return vehicleCollection;
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
        return getVehicleCollection().get(vehicleId);
    }

    /**
     * Add a vehicle to the dealer's vehicle collection
     * @param vehicle Vehicle
     */
    public void addVehicle (Vehicle vehicle) {
            vehicleCollection.put(vehicle.getVehicleId(), vehicle);
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