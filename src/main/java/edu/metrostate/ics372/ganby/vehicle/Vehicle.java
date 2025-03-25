package edu.metrostate.ics372.ganby.vehicle;

import edu.metrostate.ics372.ganby.DataProcessing.JSONObjectBuilder;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Vehicle {

    protected final String id;    //id is string because some IDs may contain letters
    protected final String model;
    protected final String manufacturer;
    protected double price;
    protected String dealerId;
    protected LocalDateTime acquisitionDate;
    protected Boolean isRentedOut;


    //In the case rent status is specified
    public Vehicle(String id, String model, String manufacturer, double price, String dealerId,
                   LocalDateTime acquisitionDate, Boolean isRentedOut) {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.price = price;
        this.dealerId = dealerId;
        this.acquisitionDate = acquisitionDate;
        this.isRentedOut = isRentedOut;
    }

    //In the case rent status is not specified
    public Vehicle(String id, String model, String manufacturer, double price, String dealerId,
                   LocalDateTime acquisitionDate) {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.price = price;
        this.dealerId = dealerId;
        this.acquisitionDate = acquisitionDate;
        isRentedOut = false;    //False by default but can be set later
    }

    public abstract String getType();

    public void setRentedOut(Boolean rentedOut) {
        isRentedOut = rentedOut;
    }

    public String getVehicleId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public String getDealerId() {
        return dealerId;
    }

    public LocalDateTime getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }


    public void setDealer(Dealer dealer) {
        if (dealer == null) throw new IllegalArgumentException("Dealer cannot be null");
        this.dealerId = dealer.getDealerId();
    }


    public void setAcquisitionDate(LocalDateTime newAcquisitionDate) {
        if (newAcquisitionDate == null)
            throw new IllegalArgumentException("New acquisition date cannot be null");
        this.acquisitionDate = newAcquisitionDate;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Vehicle vehicle) {
            return id.equals(vehicle.getVehicleId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [id:" + id
                + " model:" + model
                + " manufacturer:" + manufacturer
                + " price:" + price
                + " dealer id:" + dealerId
                + " acquisitionDate:" + acquisitionDate + "]";
    }

    public JSONObject toJSON() {
        return JSONObjectBuilder.build(this);
    }
}