package edu.metrostate.ics372.ganby.vehicle

import edu.metrostate.ics372.ganby.dealer.Dealer
import javafx.beans.property.*
import java.time.LocalDateTime
import java.util.*

/**
 * Abstract base class representing a vehicle.
 * Provides shared attributes and behaviors for all vehicle types such as ID, model, price, acquisition date, and rental status.
 * Supports JavaFX property bindings for UI integration.
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
    // Backing properties (renamed to avoid name conflict with methods)
    private val _id = SimpleStringProperty(id)
    private val _model = SimpleStringProperty(model)
    private val _manufacturer = SimpleStringProperty(manufacturer)
    private val _price = SimpleDoubleProperty(price)
    private val _dealerId = SimpleStringProperty(dealerId)
    private val _acquisitionDate = SimpleObjectProperty(acquisitionDate)
    private val _isRentedOut = SimpleBooleanProperty(isRentedOut)
    private val _selected = SimpleBooleanProperty(false)

    // Abstract type
    abstract val type: String

    // JavaFX property accessors
    fun idProperty(): StringProperty = _id
    fun modelProperty(): StringProperty = _model
    fun manufacturerProperty(): StringProperty = _manufacturer
    fun priceProperty(): DoubleProperty = _price
    fun dealerIdProperty(): StringProperty = _dealerId
    fun acquisitionDateProperty(): ObjectProperty<LocalDateTime> = _acquisitionDate
    fun isRentedOutProperty(): BooleanProperty = _isRentedOut
    fun selectedProperty(): BooleanProperty = _selected

    // Standard getters
    fun getVehicleId(): String = _id.get()
    fun getModel(): String = _model.get()
    fun getManufacturer(): String = _manufacturer.get()
    fun getPrice(): Double = _price.get()
    fun getDealerId(): String = _dealerId.get()
    fun getAcquisitionDate(): LocalDateTime = _acquisitionDate.get()
    fun getIsRentedOut(): Boolean = _isRentedOut.get()
    fun isSelected(): Boolean = _selected.get()

    // Setters
    fun setPrice(newPrice: Double) {
        require(newPrice >= 0) { "Price cannot be negative" }
        _price.set(newPrice)
    }

    fun setDealer(dealer: Dealer) {
        dealer?.let {
            _dealerId.set(it.getId())
        } ?: throw IllegalArgumentException("Dealer cannot be null")
    }

    fun setDealerId(newDealerId: String) {
        _dealerId.set(newDealerId)
    }

    fun setAcquisitionDate(newDate: LocalDateTime) {
        requireNotNull(newDate) { "New acquisition date cannot be null" }
        _acquisitionDate.set(newDate)
    }

    fun setRentedOut(rentedOut: Boolean) {
        _isRentedOut.set(rentedOut)
    }

    fun setSelected(isSelected: Boolean) {
        _selected.set(isSelected)
    }

    // Object overrides
    override fun equals(other: Any?): Boolean {
        return other is Vehicle && _id.get() == other._id.get()
    }

    override fun hashCode(): Int = Objects.hash(_id.get())

    override fun toString(): String {
        return "${javaClass.simpleName} [id: ${getVehicleId()}, model: ${getModel()}, manufacturer: ${getManufacturer()}, price: ${getPrice()}, dealerId: ${getDealerId()}, acquisitionDate: ${getAcquisitionDate()}]"
    }
}
