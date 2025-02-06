package edu.metrostate.ics372.GANBY.Vehicle;

import edu.metrostate.ics372.GANBY.Dealer.Dealer;
import lombok.Setter;

import java.time.LocalDateTime;

public class Vehicle {
    private final int DealerId;
    private final int vehicleId;
    private final String type;
    private final String manufacturer;
    private final String model;
    private double price;
    @Setter
    private LocalDateTime acquisitionDate;

   public Vehicle(int dealerId, int vehicleId, String type, String manufacturer, String model, double price, LocalDateTime acquisitionDate) {
       this.DealerId = dealerId;
       this.vehicleId = vehicleId;
       this.type = type;
       this.manufacturer = manufacturer;
       this.model = model;
       this.price = price;
       this.acquisitionDate = acquisitionDate;
   }

   public int getDealerId() {
       return DealerId;
   }
   public int getVehicleId() {
       return vehicleId;
   }
   public String getType() {
       return type;
   }
   public String getManufacturer() {
       return manufacturer;
   }
   public String getModel() {
       return model;
   }
   public double getPrice() {
       return price;
   }
   public LocalDateTime getAcquisitionDate() {
       return acquisitionDate;
   }

    @Override
    public String toString() {
        return "Type: " +  type + "\nManufacturer: " + manufacturer + "\nModel: " + model + "\nPrice: " + price +
                "\nAcquisition Date: " + acquisitionDate;
    }
}
