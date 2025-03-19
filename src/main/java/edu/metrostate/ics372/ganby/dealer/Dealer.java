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
    private HashMap<String, Vehicle> vehicleCollection;

    // Constructor, initializes dealer with id
    // Vehicle acquisition is enabled by default
    public Dealer(String id) {
        this.id = id;
        this.isVehicleAcquisitionEnabled = true;
        vehicleCollection = new HashMap<>();
        name = null;
    }

    // Constructor, initializes dealer with id
    // Vehicle acquisition is enabled by default
    public Dealer(String id, String name) {
        this.id = id;
        this.isVehicleAcquisitionEnabled = true;
        vehicleCollection = new HashMap<>();
        this.name = name;
    }


    //Getter for dealerId
    public String getDealerId(){
        return id;
    }

    //Getter for dealerId
    public String getDealerName(){
        return name;
    }

    // Getter for vehicle acquisition state
    public boolean getIsVehicleAcquisitionStatus() {
        return isVehicleAcquisitionEnabled;
    }

    public HashMap<String, Vehicle> getVehicleCollection() {
        return vehicleCollection;
    }

    // set vehicle acquisition state to true
    public void enableVehicleAcquisition(String dealerId) {
        if (isVehicleAcquisitionEnabled) {
            System.out.println("Vehicle acquisition already enabled for dealer id #" + dealerId + ".");
        } else{
            this.isVehicleAcquisitionEnabled = true;
            System.out.println("Successfully enabled vehicle acquisition for dealer id #" + dealerId + ".");
        }
    }

    // set vehicle acquisition state to false
    public void disableVehicleAcquisition(String dealerId) {
        if (!isVehicleAcquisitionEnabled) {
            System.out.println("Vehicle acquisition already disabled for dealer id #" + dealerId + ".");
        } else{
            this.isVehicleAcquisitionEnabled = false;
            System.out.println("Successfully disabled vehicle acquisition for dealer id #" + dealerId + ".");
        }
    }

    // Find vehicle by id, returns a vehicle object
    public Vehicle findVehicleById (String vehicleId) {
        return getVehicleCollection().get(vehicleId);
    }


    //Add the vehicle to vehicle collection
    public void addVehicle (Vehicle vehicle) {
            vehicleCollection.put(vehicle.getVehicleId(), vehicle);
    }

    // Equals method
    @Override
    public boolean equals (Object object) {
        if (object == this) return true;        // If object is compared with itself return true
        if (object == null) return false;       // If object is null return false
        if (object instanceof Dealer dealer) {  // If neither are true, check if object is an instance of Dealer
            return id.equals(dealer.getDealerId()) ;        // Is object a dealer with same id?
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
        String acquisitionStatus = isVehicleAcquisitionEnabled ? "Yes" : "No";
        return String.format("| %-10d | %-20s |", Integer.parseInt(id), acquisitionStatus);
    }
}