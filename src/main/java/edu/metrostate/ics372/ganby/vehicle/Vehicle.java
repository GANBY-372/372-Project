package edu.metrostate.ics372.ganby.vehicle;

import edu.metrostate.ics372.ganby.DataProcessing.JSONObjectBuilder;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import javafx.beans.property.*;

import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Vehicle {

    protected final StringProperty id;
    protected final StringProperty model;
    protected final StringProperty manufacturer;
    protected final DoubleProperty price;
    protected final StringProperty dealerId;
    protected final ObjectProperty<LocalDateTime> acquisitionDate;
    protected final BooleanProperty isRentedOut;

    // Constructor when rent status is specified
    public Vehicle(String id, String model, String manufacturer, double price, String dealerId,
                   LocalDateTime acquisitionDate, Boolean isRentedOut) {
        this.id = new SimpleStringProperty(id);
        this.model = new SimpleStringProperty(model);
        this.manufacturer = new SimpleStringProperty(manufacturer);
        this.price = new SimpleDoubleProperty(price);
        this.dealerId = new SimpleStringProperty(dealerId);
        this.acquisitionDate = new SimpleObjectProperty<>(acquisitionDate);
        this.isRentedOut = new SimpleBooleanProperty(isRentedOut);
    }

    // Constructor when rent status is not specified
    public Vehicle(String id, String model, String manufacturer, double price, String dealerId,
                   LocalDateTime acquisitionDate) {
        this(id, model, manufacturer, price, dealerId, acquisitionDate, false);
    }

    public abstract String getType();

    // Getters for JavaFX TableView binding
    public String getVehicleId() { return id.get(); }
    public String getModel() { return model.get(); }
    public String getManufacturer() { return manufacturer.get(); }
    public double getPrice() { return price.get(); }
    public String getDealerId() { return dealerId.get(); }
    public LocalDateTime getAcquisitionDate() { return acquisitionDate.get(); }
    public Boolean getIsRentedOut() { return isRentedOut.get(); }

    // Setters
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price.set(price);
    }

    public void setDealer(Dealer dealer) {
        if (dealer == null) throw new IllegalArgumentException("Dealer cannot be null");
        this.dealerId.set(dealer.getId());
    }

    public void setAcquisitionDate(LocalDateTime newAcquisitionDate) {
        if (newAcquisitionDate == null)
            throw new IllegalArgumentException("New acquisition date cannot be null");
        this.acquisitionDate.set(newAcquisitionDate);
    }

    public void setRentedStatus(Boolean rentedOut) {
        this.isRentedOut.set(rentedOut);
    }

    // JavaFX Properties for binding
    public StringProperty vehicleIdProperty() { return id; }
    public StringProperty modelProperty() { return model; }
    public StringProperty manufacturerProperty() { return manufacturer; }
    public DoubleProperty priceProperty() { return price; }
    public StringProperty dealerIdProperty() { return dealerId; }
    public ObjectProperty<LocalDateTime> acquisitionDateProperty() { return acquisitionDate; }
    public BooleanProperty isRentedOutProperty() { return isRentedOut; }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Vehicle vehicle) {
            return id.get().equals(vehicle.getVehicleId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id.get());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [id:" + getVehicleId()
                + " model:" + getModel()
                + " manufacturer:" + getManufacturer()
                + " price:" + getPrice()
                + " dealer id:" + getDealerId()
                + " acquisitionDate:" + getAcquisitionDate() + "]";
    }

    public JSONObject toJSON() {
        return JSONObjectBuilder.build(this);
    }
}
