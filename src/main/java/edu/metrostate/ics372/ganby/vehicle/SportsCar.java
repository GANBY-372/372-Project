package edu.metrostate.ics372.ganby.vehicle;

import java.time.LocalDateTime;


public class SportsCar extends Vehicle {

    final private String type;

     /*
    public SportsCar(String id, String model, String manufacturer, double price, String dealerId, LocalDateTime acquisitionDate,
        Boolean isRentedOut String type) {
        super(id,model,manufacturer,price,dealerId,acquisitionDate);
        this.type = "SportsCar";
    }

     */

    //In the case rent status is specified
    public SportsCar(String id, String model, String manufacturer, double price, String dealerId,
               LocalDateTime acquisitionDate,Boolean isRentedOut) {
        super(id,model,manufacturer,price,dealerId,acquisitionDate, isRentedOut);
        type = "SportsCar";
    }

    //In the case rent status is not specified
    public SportsCar(String id, String model, String manufacturer, double price, String dealerId,
               LocalDateTime acquisitionDate) {
        super(id,model,manufacturer,price,dealerId,acquisitionDate);
        type = "SportsCar";
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals (Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof SportsCar sportsCar) {
            return id.equals(sportsCar.getId());
        }
        return false;
    }


    @Override
    public String toString () {
        return type + " " + super.toString();
    }
}