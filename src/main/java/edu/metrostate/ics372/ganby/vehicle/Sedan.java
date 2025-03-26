package edu.metrostate.ics372.ganby.vehicle;

import java.time.LocalDateTime;


public class Sedan extends Vehicle {

    private final String type;

    //In the case rent status is specified
    public Sedan(String id, String model, String manufacturer, double price, String dealerId,
                 LocalDateTime acquisitionDate,Boolean isRentedOut) {
        super(id,model,manufacturer,price,dealerId,acquisitionDate, isRentedOut);
        type = "Sedan";
    }

    //In the case rent status is not specified
    public Sedan(String id, String model, String manufacturer, double price, String dealerId,
                 LocalDateTime acquisitionDate) {
        super(id,model,manufacturer,price,dealerId,acquisitionDate);
        type = "Sedan";
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals (Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Sedan sedan) {
            return id.equals(sedan.getVehicleId());
        }
        return false;
    }


    @Override
    public String toString () {
        return type + " " + super.toString();
    }
}