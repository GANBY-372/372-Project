package edu.metrostate.ics372.ganby.vehicle;

import java.time.LocalDateTime;


public class Pickup extends Vehicle {

    private final String type;

    //This is the case that category is not specified, but I imagine it's never going to be specified.
     /*
    public Pickup(String id, String model, String manufacturer, double price, String dealerId, LocalDateTime acquisitionDate,
        Boolean isRentedOut String type) {
        super(id,model,manufacturer,price,dealerId,acquisitionDate);
        this.type = "Pickup";
    }

     */

    //In the case rent status is specified
    public Pickup(String id, String model, String manufacturer, double price, String dealerId,
               LocalDateTime acquisitionDate,Boolean isRentedOut) {
        super(id,model,manufacturer,price,dealerId,acquisitionDate, isRentedOut);
        type = "Pickup";
    }

    //In the case rent status is not specified
    public Pickup(String id, String model, String manufacturer, double price, String dealerId,
               LocalDateTime acquisitionDate) {
        super(id,model,manufacturer,price,dealerId,acquisitionDate);
        type = "Pickup";
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals (Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Pickup pickup) {
            return id.equals(pickup.getId());
        }
        return false;
    }


    @Override
    public String toString () {
        return type + " " + super.toString();
    }
}