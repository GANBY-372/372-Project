package org.example.demo.Vehicle;

import java.time.LocalDateTime;

/**
 * Represents an SUV vehicle in the system.
 * Extends the {@link Vehicle} class with SUV-specific identification.
 */
public class SUV extends Vehicle {
  private final String type = "SUV";
  
   /**
     * Constructs an SUV with all attributes, including rent status.
     *
     * @param id              the unique vehicle ID
     * @param model           the model name
     * @param manufacturer    the vehicle's manufacturer
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer owning this vehicle
     * @param acquisitionDate the date the vehicle was acquired
     * @param isRentedOut     whether the vehicle is currently rented
     */
    //In the case rent status is specified
    public SUV(String id, String model, String manufacturer, double price, String dealerId,
               LocalDateTime acquisitionDate, Boolean isRentedOut) {
        super(id, model, manufacturer, price, dealerId, acquisitionDate, isRentedOut);
    }

    /**
     * Constructs an SUV with a default rent status of false.
     *
     * @param id              the unique vehicle ID
     * @param model           the model name
     * @param manufacturer    the vehicle's manufacturer
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer owning this vehicle
     * @param acquisitionDate the date the vehicle was acquired
     */
    public SUV(String id, String model, String manufacturer, double price, String dealerId,
               LocalDateTime acquisitionDate) {
        super(id, model, manufacturer, price, dealerId, acquisitionDate);
    }

    /**
     * Returns the type of this vehicle.
     *
     * @return "SUV"
     */
    @Override
    public String getType() {
        return "SUV";
    }

    /**
     * Determines if another object is equal to this SUV.
     * Two SUVs are considered equal if they have the same vehicle ID.
     *
     * @param object the object to compare
     * @return true if the object is an SUV with the same ID, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SUV suv = (SUV) object;
        return this.id.get().equals(suv.getVehicleId());
    }

    /**
     * Returns a string representation of this SUV.
     *
     * @return a formatted string with vehicle type and core details
     */
    @Override
    public String toString() {
        return getType() + " " + super.toString();
    }
}
