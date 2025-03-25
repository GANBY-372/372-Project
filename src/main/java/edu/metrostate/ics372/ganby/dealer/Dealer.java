package edu.metrostate.ics372.ganby.dealer;

import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Dealer {

    // Getter for dealer id
    // Instance variables
    private String name;
    private final String id;    //id is string because some IDs may contain letters

//    private BooleanProperty isBuying;
    private BooleanProperty isBuying;
    // Getter for vehicles associated with dealer, Returns a set of vehicle objects
    private HashMap<String, Vehicle> vehicleCatalog;

    // TODO: Implement automatic naming if dealer name is not specified.
    /**
     * Constructor for Dealer if name is not specified
     * @param id String
     */
    public Dealer(String id) {
        this(id, "Dealer-" + id);
    }

    /**
     * Constructor for Dealer if name is specified
     * @param id String
     * @param name Name
     */
    public Dealer(String id, String name) {
        this.id = id;
        this.isBuying = new SimpleBooleanProperty(true);
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
     */
    public String getDealerName(){
        return name;
    }


    /**
     * Get the vehicle acquisition status
     * @return boolean
     */
    public boolean getIsBuying () {
        return isBuying.get();
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
        if (isBuying.get()) {
            System.out.println("Vehicle acquisition already enabled for dealer id #" + dealerId + ".");
        } else{
            this.isBuying.set(true);
            System.out.println("Successfully enabled vehicle acquisition for dealer id #" + dealerId + ".");
        }
    }

    /**
     * Set the dealer acquisition status to false / disabled
     * @param dealerId String
     */
    public void disableVehicleAcquisition(String dealerId) {
        if (!isBuying.get()) {
            System.out.println("Vehicle acquisition already disabled for dealer id #" + dealerId + ".");
        } else{
            this.isBuying.set(false);
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
        String acquisitionStatus = isBuying.get() ? "Yes" : "No";
        return String.format("| %-10d | %-20s |", Integer.parseInt(id), acquisitionStatus);
    }

    public boolean transferVehicleSet(Set<Vehicle> vehicles, Dealer dealer) {
        if (vehicles == null) {
            System.out.print("Cannot transfer a null vehicle set.");
            return false;
        }
        if (dealer == null) {
            System.out.print("Cannot transfer a vehicle set to a null dealer.");
            return false;
        }

        for (Vehicle vehicle : vehicles) {
            if (!transferVehicle(vehicle, dealer)) {
                System.out.println("Batch transfer to dealer: " + dealer.getDealerName() + " failed at vehicleid " + vehicle.getVehicleId());
                return false;
            }
        }
        System.out.println("Batch transfer to dealer: " + dealer.getDealerName() + " successful.");
        return true;
    }

    public boolean transferVehicle (Vehicle vehicle, Dealer dealer) {

        if (vehicle == null) {
            System.out.print("Cannot transfer a null vehicle.");
            return false;
        }
        if (dealer == null) {
            System.out.print("Cannot transfer a vehicle to a null dealer.");
            return false;
        }

        if (!dealer.isBuying.get()) {
            System.out.println("Cannot transfer a vehicle to a dealer who is not buying.");
            return false;
        }

        if (this.vehicleCatalog.get(vehicle.getVehicleId()) == null) {
            System.out.println("Cannot transfer a vehicle that is not owned by this dealer.");
            return false;
        }

        this.vehicleCatalog.remove(vehicle.getVehicleId());

        vehicle.setDealer(dealer);
        dealer.getVehicleCatalog().put(vehicle.getVehicleId(), vehicle);


        System.out.println("Successfully transferred vehicle " + vehicle.getVehicleId()+ " to dealer " + dealer.getDealerName());
        return dealer.getVehicleCatalog().get(vehicle.getVehicleId()) != null;
    }
}