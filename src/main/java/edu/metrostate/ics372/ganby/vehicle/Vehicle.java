package edu.metrostate.ics372.ganby.vehicle;


import edu.metrostate.ics372.ganby.dealer.Dealer;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;

public interface Vehicle {
    String getId ();
    String getModel();
    String getManufacturer();
    double getPrice();
    Dealer getDealer();
    LocalDateTime getAcquisitionDate ();

    public void setPrice (double price);
    public void setDealer(Dealer dealer);
    public void setAcquisitionDate (LocalDateTime acquisitionDate);

    public JSONObject toJSON ();
}