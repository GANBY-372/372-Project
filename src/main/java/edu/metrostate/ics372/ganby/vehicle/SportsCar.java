package edu.metrostate.ics372.ganby.vehicle;

import edu.metrostate.ics372.ganby.json.JSONObjectBuilder;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.Objects;

public class SportsCar implements Vehicle {

    private final String id;    //id is string because some IDs may contain letters
    private final String model;
    private final String manufacturer;
    private double price;
    private String dealerId;
    private LocalDateTime acquisitionDate;

    public SportsCar(String id, String model, String manufacturer, double price, String dealerId, LocalDateTime acquisitionDate) {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.price = price;
        this.dealerId = dealerId;
        this.acquisitionDate = acquisitionDate;
    }

    @Override
    public String getVehicleId() {
        return id;
    }

    @Override
    public String getModel () {
        return model;
    }

    @Override
    public String getManufacturer () {
        return manufacturer;
    }

    @Override
    public double getPrice () {
        return price;
    }

    @Override
    public String getDealerId () {
        return dealerId;
    }

    @Override
    public LocalDateTime getAcquisitionDate () {
        return acquisitionDate;
    }

    @Override
    public void setPrice (double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }


    /*
    public void setDealer (Dealer dealer) {
        if (dealer == null) throw new IllegalArgumentException("Dealer cannot be null");
        this.dealer = dealer;
    }


     */
    @Override
    public void setAcquisitionDate (LocalDateTime newAcquisitionDate) {
        if (newAcquisitionDate == null)
            throw new IllegalArgumentException("New acquisition date cannot be null");
        this.acquisitionDate = newAcquisitionDate;
    }

    @Override
    public boolean equals (Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof SportsCar sportsCar) {
            return id == sportsCar.getVehicleId();
        }
        return false;
    }

    @Override
    public int hashCode () {
        return Objects.hashCode(id);
    }

    @Override
    public String toString () {
        return getClass().getSimpleName() +
                " [id:" + id
                + " model:" + model
                + " manufacturer:" + manufacturer
                + " price:" + price
                + " dealer id:" + dealerId
                + " acquisitionDate:" + acquisitionDate + "]";
    }

    public JSONObject toJSON () {
        return JSONObjectBuilder.build(this);
    }


    /*
    public static Builder builder () {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private String model;
        private String manufacturer;
        private double price;
        private Dealer dealer;
        private LocalDateTime acquisitionDate;

        public Builder id (String id) {
            this.id = id;
            return this;
        }

        public Builder model (String model) {
            this.model = model;
            return this;
        }

        public Builder manufacturer (String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public Builder price (double price) {
            this.price = price;
            return this;
        }

        public Builder dealer (int id) {
            this.dealerId = dealer;
            return this;
        }

        public Builder acquisitionDate (LocalDateTime acquisitionDate) {
            this.acquisitionDate = acquisitionDate;
            return this;
        }

        public SUV build () {
            return new SUV(this);
        }
    }

     */
}