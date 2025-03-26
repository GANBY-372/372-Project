package edu.metrostate.ics372.ganby.vehicle;

import java.time.LocalDateTime;

public class SportsCar extends Vehicle {

    // Constructor with rent status
    public SportsCar(String id, String model, String manufacturer, double price, String dealerId,
               LocalDateTime acquisitionDate, Boolean isRentedOut) {
        super(id, model, manufacturer, price, dealerId, acquisitionDate, isRentedOut);
    }

    // Constructor without rent status
    public SportsCar(String id, String model, String manufacturer, double price, String dealerId,
               LocalDateTime acquisitionDate) {
        super(id, model, manufacturer, price, dealerId, acquisitionDate);
    }

    @Override
    public String getType() {
        return "SportsCar";
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof SUV suv) {
            return this.id.get().equals(suv.getVehicleId());
        }
        return false;
    }

    @Override
    public String toString() {
        return getType() + " " + super.toString();
    }
}
