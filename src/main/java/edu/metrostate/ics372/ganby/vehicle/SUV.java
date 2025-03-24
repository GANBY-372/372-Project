package edu.metrostate.ics372.ganby.vehicle;

import java.time.LocalDateTime;


public class SUV extends Vehicle {

    private final String type;

   /*
    public SUV(String id, String model, String manufacturer, double price, String dealerId, LocalDateTime acquisitionDate,
        Boolean isRentedOut String type) {
        super(id,model,manufacturer,price,dealerId,acquisitionDate);
        this.type = "SUV";
    }

     */

    //In the case rent status is specified
    public SUV(String id, String model, String manufacturer, double price, String dealerId,
                  LocalDateTime acquisitionDate,Boolean isRentedOut) {
        super(id,model,manufacturer,price,dealerId,acquisitionDate, isRentedOut);
        type = "SUV";
    }

    //In the case rent status is not specified
    public SUV(String id, String model, String manufacturer, double price, String dealerId,
               LocalDateTime acquisitionDate) {
        super(id,model,manufacturer,price,dealerId,acquisitionDate);
        type = "SUV";
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals (Object object) {
            if (object == this) return true;
            if (object == null) return false;
            if (object instanceof SUV suv) {
                return this.id.equals(suv.getVehicleId());
            }
            return false;
    }


    @Override
    public String toString () {
        return type + " " + super.toString();
    }
}