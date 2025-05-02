package edu.metrostate.ics372.ganby.vehicle

import edu.metrostate.ics372.ganby.dealer.Dealer
import javafx.beans.property.*
import java.time.LocalDateTime
import java.util.*

/**
 * Abstract base class representing a vehicle.
 *
 * This class provides shared attributes and behaviors for all vehicle types, such as:
 * - Vehicle ID
 * - Model
 * - Manufacturer
 * - Price
 * - Dealer association
 * - Acquisition date
 * - Rental status
 * - UI selection support
 *
 * It is designed for use with JavaFX applications by exposing JavaFX properties for all fields.
 *
 * @constructor Creates a new vehicle instance with required fields and optional rental flag.
 * @param id Unique vehicle identifier.
 * @param model Model name of the vehicle.
 * @param manufacturer Manufacturer name.
 * @param price Price of the vehicle (non-negative).
 * @param dealerId ID of the associated dealer.
 * @param acquisitionDate Date the vehicle was acquired.
 * @param isRentedOut Boolean flag indicating if the vehicle is currently rented out.
 */
abstract class Vehicle @JvmOverloads constructor(
    id: String,
    model: String,
    manufacturer: String,
    price: Double,
    dealerId: String,
    acquisitionDate: LocalDateTime,
    isRentedOut: Boolean = false
) {
    // JavaFX property bindings for all fields
    private val _id = SimpleStringProperty(id)
    private val _model = SimpleStringProperty(model)
    private val _manufacturer = SimpleStringProperty(manufacturer)
    private val _price = SimpleDoubleProperty(price)
    private val _dealerId = SimpleStringProperty(dealerId)
    private val _acquisitionDate = SimpleObjectProperty(acquisitionDate)
    private val _isRentedOut = SimpleBooleanProperty(isRentedOut)
    private val _selected = SimpleBooleanProperty(false)

    /**
     * Abstract vehicle type to be implemented by subclasses (e.g., "SUV", "Sedan").
     */
    abstract val type: String

    // ----- JavaFX Property Getters (for GUI bindings) -----

    fun idProperty(): StringProperty = _id
    fun modelProperty(): StringProperty = _model
    fun manufacturerProperty(): StringProperty = _manufacturer
    fun priceProperty(): DoubleProperty = _price
    fun dealerIdProperty(): StringProperty = _dealerId
    fun acquisitionDateProperty(): ObjectProperty<LocalDateTime> = _acquisitionDate
    fun isRentedOutProperty(): BooleanProperty = _isRentedOut
    fun selectedProperty(): BooleanProperty = _selected

    // ----- Standard Getters -----

    /** @return the vehicle's unique ID */
    fun getVehicleId(): String = _id.get()

    /** @return the vehicle's model name */
    fun getModel(): String = _model.get()

    /** @return the vehicle's manufacturer */
    fun getManufacturer(): String = _manufacturer.get()

    /** @return the vehicle's current price */
    fun getPrice(): Double = _price.get()

    /** @return the ID of the dealer who owns this vehicle */
    fun getDealerId(): String = _dealerId.get()

    /** @return the acquisition date */
    fun getAcquisitionDate(): LocalDateTime = _acquisitionDate.get()

    /** @return whether the vehicle is currently rented out */
    fun getIsRentedOut(): Boolean = _isRentedOut.get()

    /** @return whether the vehicle is selected in the UI */
    fun isSelected(): Boolean = _selected.get()

    // ----- Setters -----

    /**
     * Updates the vehicle's price.
     * @param newPrice the new price (must be non-negative)
     * @throws IllegalArgumentException if newPrice < 0
     */
    fun setPrice(newPrice: Double) {
        require(newPrice >= 0) { "Price cannot be negative" }
        _price.set(newPrice)
    }

    /**
     * Sets the dealer for this vehicle and updates the dealer ID.
     * @param dealer the dealer object (must not be null)
     * @throws IllegalArgumentException if dealer is null
     */
    fun setDealer(dealer: Dealer) {
        dealer?.let {
            _dealerId.set(it.getId())
        } ?: throw IllegalArgumentException("Dealer cannot be null")
    }

    /**
     * Updates the dealer ID for this vehicle.
     * @param newDealerId new dealer ID
     */
    fun setDealerId(newDealerId: String) {
        _dealerId.set(newDealerId)
    }

    /**
     * Updates the acquisition date of the vehicle.
     * @param newDate the new acquisition date (non-null)
     * @throws IllegalArgumentException if newDate is null
     */
    fun setAcquisitionDate(newDate: LocalDateTime) {
        requireNotNull(newDate) { "New acquisition date cannot be null" }
        _acquisitionDate.set(newDate)
    }

    /**
     * Updates rental status.
     * @param rentedOut true if the vehicle is currently rented out
     */
    fun setRentedOut(rentedOut: Boolean) {
        _isRentedOut.set(rentedOut)
    }

    /**
     * Updates UI selection status for table checkboxes.
     * @param isSelected true to mark as selected
     */
    fun setSelected(isSelected: Boolean) {
        _selected.set(isSelected)
    }

    // ----- Object Overrides -----

    /** Two vehicles are equal if they have the same ID. */
    override fun equals(other: Any?): Boolean {
        return other is Vehicle && _id.get() == other._id.get()
    }

    override fun hashCode(): Int = Objects.hash(_id.get())

    override fun toString(): String {
        return "${javaClass.simpleName} [id: ${getVehicleId()}, model: ${getModel()}, manufacturer: ${getManufacturer()}, price: ${getPrice()}, dealerId: ${getDealerId()}, acquisitionDate: ${getAcquisitionDate()}]"
    }
}
