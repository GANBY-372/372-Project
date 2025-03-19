package edu.metrostate.ics372.ganby.vehicle;


import org.json.simple.JSONObject;

import java.time.LocalDateTime;

public interface Vehicle {
    String getVehicleId();  //id is string because some IDs may contain letters
    String getModel();
    String getManufacturer();
    double getPrice();
    String getDealerId();
    LocalDateTime getAcquisitionDate ();

    public void setPrice (double price);
    //public void setDealer(Dealer dealer);  Do we need this? It's already implemented in the addVehicle() method
    //                                       in the DealerCatalog class.
    public void setAcquisitionDate (LocalDateTime acquisitionDate);

    public JSONObject toJSON ();
}