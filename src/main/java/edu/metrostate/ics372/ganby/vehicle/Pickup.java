package edu.metrostate.ics372.ganby.vehicle;

import java.time.LocalDateTime;

/**
 * Represents a Pickup vehicle in the system.
 * Inherits all standard behavior from the {@link Vehicle} superclass.
 */
public class Pickup extends Vehicle {

    /**
     * Constructs a Pickup with all attributes, including rent status.
     *
     * @param id              the unique ID of the vehicle
     * @param model           the model of the vehicle
     * @param manufacturer    the manufacturer of the vehicle
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer who owns the vehicle
     * @param acquisitionDate the date the vehicle was acquired
     * @param isRentedOut     whether the vehicle is currently rented
     */
    public Pickup(String id, String model, String manufacturer, double price, String dealerId,
                  LocalDateTime acquisitionDate, Boolean isRentedOut) {
        super(id, model, manufacturer, price, dealerId, acquisitionDate, isRentedOut);
    }

    /**
     * Constructs a Pickup with a default rent status of false.
     *
     * @param id              the unique ID of the vehicle
     * @param model           the model of the vehicle
     * @param manufacturer    the manufacturer of the vehicle
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer who owns the vehicle
     * @param acquisitionDate the date the vehicle was acquired
     */
    public Pickup(String id, String model, String manufacturer, double price, String dealerId,
                  LocalDateTime acquisitionDate) {
        super(id, model, manufacturer, price, dealerId, acquisitionDate);
    }

    /**
     * Returns the vehicle type string.
     *
     * @return "Pickup"
     */
    @Override
    public String getType() {
        return "Pickup";
    }

    /**
     * Checks if another object is equal to this Pickup.
     * Two vehicles are equal if their IDs are the same.
     *
     * @param object the object to compare
     * @return true if the object is a Vehicle and has the same ID
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Vehicle vehicle) {
            return this.id.get().equals(vehicle.getVehicleId());
        }
        return false;
    }

    /**
     * Returns a string representation of this Pickup.
     *
     * @return a string showing the vehicle type and details
     */
    @Override
    public String toString() {
        return getType() + " " + super.toString();
    }
}
