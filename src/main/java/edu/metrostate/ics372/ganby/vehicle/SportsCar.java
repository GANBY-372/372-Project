package edu.metrostate.ics372.ganby.vehicle;

import java.time.LocalDateTime;

/**
 * Represents a sports car, which cannot be rented out.
 */
public class SportsCar extends Vehicle {

    /**
     * Constructs a new SportsCar with an explicit rent status, but always forces rent status to false.
     *
     * @param id               the unique vehicle ID
     * @param model            the model name
     * @param manufacturer     the manufacturer name
     * @param price            the price of the vehicle
     * @param dealerId         the dealer's ID who owns the vehicle
     * @param acquisitionDate  the date the vehicle was acquired
     * @param isRentedOut      ignored - SportsCar is never rented
     */
    public SportsCar(String id, String model, String manufacturer, double price, String dealerId,
                     LocalDateTime acquisitionDate, Boolean isRentedOut) {
        super(id, model, manufacturer, price, dealerId, acquisitionDate, false);
    }

    /**
     * Constructs a new SportsCar, defaulting rent status to false.
     *
     * @param id              the unique vehicle ID
     * @param model           the model name
     * @param manufacturer    the manufacturer name
     * @param price           the price of the vehicle
     * @param dealerId        the dealer's ID who owns the vehicle
     * @param acquisitionDate the date the vehicle was acquired
     */
    public SportsCar(String id, String model, String manufacturer, double price, String dealerId,
                     LocalDateTime acquisitionDate) {
        super(id, model, manufacturer, price, dealerId, acquisitionDate, false);
    }

    /**
     * Returns the vehicle type.
     *
     * @return "SportsCar"
     */
    @Override
    public String getType() {
        return "SportsCar";
    }

    /**
     * Overrides rent status setter. SportsCars cannot be rented, so this does nothing but return type, which will be
     * checked and then once found out to be SportsCar
     *
     * @param rentStatus the attempted rent status (ignored)
     */
    @Override
    public String setRentedOut(Boolean rentStatus) {
        return this.getType();
    }

    /**
     * Equality based on vehicle ID.
     *
     * @param object another object
     * @return true if same vehicle ID
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
     * Returns String representation of object
     *
     *  another object
     * @return String representation of object
     */
    @Override
    public String toString() {
        return getType() + " " + super.toString();
    }
}
