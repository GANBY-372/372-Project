package org.example.demo.Vehicle;

import java.time.LocalDateTime;

/**
 * Represents a Sedan vehicle. A subclass of {@link Vehicle}.
 * Sedans can be rented out and are treated as regular vehicles in the system.
 */
public class Sedan extends Vehicle {
    private final String type = "SEDAN";

    /**
     * Constructs a Sedan with the specified parameters and explicit rent status.
     *
     * @param id              the unique vehicle ID
     * @param model           the model name of the sedan
     * @param manufacturer    the vehicle manufacturer
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer owning this vehicle
     * @param acquisitionDate the date the vehicle was acquired
     * @param isRentedOut     whether the vehicle is currently rented out
     */
    //In the case rent status is specified
    public Sedan(String id, String model, String manufacturer, double price, String dealerId,
                 LocalDateTime acquisitionDate, Boolean isRentedOut) {
        super(id, model, manufacturer, price, dealerId, acquisitionDate, isRentedOut);
    }

    /**
     * Constructs a Sedan with the specified parameters and default rent status (false).
     *
     * @param id              the unique vehicle ID
     * @param model           the model name of the sedan
     * @param manufacturer    the vehicle manufacturer
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer owning this vehicle
     * @param acquisitionDate the date the vehicle was acquired
     */
    public Sedan(String id, String model, String manufacturer, double price, String dealerId,
                 LocalDateTime acquisitionDate) {
        super(id, model, manufacturer, price, dealerId, acquisitionDate);
    }

    /**
     * Returns the vehicle type.
     *
     * @return a string indicating the type of this vehicle ("Sedan")
     */
    @Override
    public String getType() {
        return "Sedan";
    }

    /**
     * Compares this Sedan to another object for equality.
     * Two vehicles are considered equal if their IDs match.
     *
     * @param object the object to compare
     * @return true if the object is a Sedan with the same ID; false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Sedan sedan) {
            return this.id.get().equals(sedan.getVehicleId());
        }
        return false;
    }

    /**
     * Returns a string representation of the Sedan.
     *
     * @return a string containing the vehicle type and details
     */
    @Override
    public String toString() {
        return getType() + " " + super.toString();
    }
}
