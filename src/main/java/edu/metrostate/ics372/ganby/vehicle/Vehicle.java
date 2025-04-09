package edu.metrostate.ics372.ganby.vehicle;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract base class representing a vehicle.
 * Provides shared attributes and behaviors for all vehicle types such as ID, model, price, acquisition date, and rental status.
 * Supports JavaFX property bindings for UI integration.
 */
public abstract class Vehicle {

    // Properties for JavaFX bindings
    protected final StringProperty id;
    protected final StringProperty model;
    protected final StringProperty manufacturer;
    protected final DoubleProperty price;
    protected final StringProperty dealerId;
    protected final ObjectProperty<LocalDateTime> acquisitionDate;
    protected final BooleanProperty isRentedOut;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    // --- JavaFX Bindable Properties ---

    public StringProperty vehicleIdProperty() {
        return id;
    }

    public StringProperty modelProperty() {
        return model;
    }

    public StringProperty manufacturerProperty() {
        return manufacturer;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public StringProperty dealerIdProperty() {
        return dealerId;
    }

    public ObjectProperty<LocalDateTime> acquisitionDateProperty() {
        return acquisitionDate;
    }

    public BooleanProperty isRentedOutProperty() {
        return isRentedOut;
    }

    /**
     * Constructs a vehicle with rental status.
     *
     * @param id              the vehicle ID
     * @param model           the vehicle model
     * @param manufacturer    the manufacturer of the vehicle
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the associated dealer
     * @param acquisitionDate the date the vehicle was acquired
     * @param isRentedOut     rental status of the vehicle
     */
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

    /**
     * Constructs a vehicle with rental status defaulted to false.
     *
     * @param id              the vehicle ID
     * @param model           the vehicle model
     * @param manufacturer    the manufacturer of the vehicle
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the associated dealer
     * @param acquisitionDate the date the vehicle was acquired
     */
    public Vehicle(String id, String model, String manufacturer, double price, String dealerId,
                   LocalDateTime acquisitionDate) {
        this(id, model, manufacturer, price, dealerId, acquisitionDate, false);
    }

    /**
     * Returns the vehicle type (e.g., SUV, Sedan).
     *
     * @return the vehicle type
     */
    public abstract String getType();

    // --- Getters ---

    /**
     * @return the vehicle ID
     */
    public String getVehicleId() {
        return id.get();
    }

    /**
     * @return the vehicle model
     */
    public String getModel() {
        return model.get();
    }

    /**
     * @return the vehicle manufacturer
     */
    public String getManufacturer() {
        return manufacturer.get();
    }

    /**
     * @return the vehicle price
     */
    public double getPrice() {
        return price.get();
    }

    /**
     * @return the dealer ID this vehicle belongs to
     */
    public String getDealerId() {
        return dealerId.get();
    }

    /**
     * @return the acquisition date of the vehicle
     */
    public LocalDateTime getAcquisitionDate() {
        return acquisitionDate.get();
    }

    /**
     * @return true if rented out, false otherwise
     */
    public Boolean getIsRentedOut() {
        return isRentedOut.get();
    }

    // --- Setters ---

    /**
     * Sets the price of the vehicle.
     *
     * @param price new price (must be non-negative)
     */
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price.set(price);
    }

    /**
     * Sets the dealer this vehicle belongs to.
     *
     * @param dealer the dealer instance
     */
    public void setDealer(Dealer dealer) {
        if (dealer == null) throw new IllegalArgumentException("Dealer cannot be null");
        this.dealerId.set(dealer.getId());
    }

    /**
     * Sets the dealer ID directly.
     *
     * @param dealerId dealer's ID
     */
    public void setDealerId(String dealerId) {
        this.dealerId.set(dealerId);
    }

    /**
     * Sets the acquisition date of the vehicle.
     *
     * @param newAcquisitionDate the new acquisition date
     */
    public void setAcquisitionDate(LocalDateTime newAcquisitionDate) {
        if (newAcquisitionDate == null)
            throw new IllegalArgumentException("New acquisition date cannot be null");
        this.acquisitionDate.set(newAcquisitionDate);
    }

    /**
     * Sets the rented-out status of the vehicle.
     * Override this method in subclasses if rental is not allowed.
     *
     * @param rentedOut the new rent status
     * @return the vehicle type (can be used to restrict rental for certain types)
     */
    public void setRentedOut(Boolean rentedOut) {
        this.isRentedOut.set(rentedOut);
    }

    // --- Selection Support ---

    /**
     * Gets the selected property for checkbox bindings.
     *
     * @return the selected property
     */
    public BooleanProperty selectedProperty() {
        return selected;
    }

    /**
     * @return true if the vehicle is selected
     */
    public boolean isSelected() {
        return selected.get();
    }

    /**
     * Sets the selected state of the vehicle (used in UI).
     *
     * @param selected true if selected, false otherwise
     */
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }


    public void VehicleBuilderFromType(String type){

    }

    // --- Object Overrides ---

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null || !(object instanceof Vehicle vehicle)) return false;
        return id.get().equals(vehicle.getVehicleId());
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
}
