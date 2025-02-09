package edu.metrostate.ics372.ganby.vehicle;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.json.JSONObjectBuilder;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;


@AllArgsConstructor
public class SportsCar implements Vehicle {

    private final String id;
    private final String model;
    private final String manufacturer;
    private double price;
    private Dealer dealer;
    private LocalDateTime acquisitionDate;

    public SportsCar (Builder builder) {
        this.id = builder.id;
        this.model = builder.model;
        this.manufacturer = builder.manufacturer;
        this.acquisitionDate = builder.acquisitionDate;
        this.price = builder.price;
        this.dealer = builder.dealer;
    }

    @Override
    public String getId () {
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
    public Dealer getDealer () {
        return dealer;
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

    @Override
    public void setDealer (Dealer dealer) {
        if (dealer == null) throw new IllegalArgumentException("Dealer cannot be null");
        this.dealer = dealer;
    }

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
        if (object instanceof SportsCar vehicle) {
            return id.equalsIgnoreCase(vehicle.getId());
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
            + " dealer:" + dealer
            + " acquisitionDate:" + acquisitionDate + "]";
    }

    public JSONObject toJSON () {
        return JSONObjectBuilder.build(this);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("dealer_id", dealer.getId());
//        jsonObject.put("vehicle_manufacturer", manufacturer);
//        jsonObject.put("vehicle_type", getClass().getSimpleName());
//        jsonObject.put("vehicle_model", model);
//        jsonObject.put("vehicle_price", price);
//        jsonObject.put("acquisition_date", acquisitionDate.atZone(TimeZone.getDefault().toZoneId()).toInstant().toEpochMilli());
//        return jsonObject;
    }

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

        public Builder dealer (Dealer dealer) {
            this.dealer = dealer;
            return this;
        }

        public Builder acquisitionDate (LocalDateTime acquisitionDate) {
            this.acquisitionDate = acquisitionDate;
            return this;
        }

        public SportsCar build () {
            return new SportsCar(this);
        }
    }
}