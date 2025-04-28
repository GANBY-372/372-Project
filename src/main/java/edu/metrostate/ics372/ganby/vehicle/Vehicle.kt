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
    dealerName: String, // <-- ADD THIS
    acquisitionDate: LocalDateTime,
    isRentedOut: Boolean = false
) {
    // Properties for JavaFX bindings
    val id: StringProperty = SimpleStringProperty(id)
    protected var model: StringProperty = SimpleStringProperty(model)
    protected var manufacturer: StringProperty = SimpleStringProperty(manufacturer)
    protected var price: DoubleProperty = SimpleDoubleProperty(price)
    protected var dealerId: StringProperty = SimpleStringProperty(dealerId)
    protected var dealerName: StringProperty = SimpleStringProperty(dealerName) // <-- ADD THIS
    protected var acquisitionDate: ObjectProperty<LocalDateTime> = SimpleObjectProperty(acquisitionDate)
    val isRentedOutProperty: BooleanProperty = SimpleBooleanProperty(isRentedOut)
    private val selected: BooleanProperty = SimpleBooleanProperty(false)

    /**
     * Abstract property representing the vehicle type.
     */
    abstract val type: String

    // JavaFX property accessors
    fun vehicleIdProperty(): StringProperty = id
    fun modelProperty(): StringProperty = model
    fun manufacturerProperty(): StringProperty = manufacturer
    fun priceProperty(): DoubleProperty = price
    fun dealerIdProperty(): StringProperty = dealerId
    fun dealerNameProperty(): StringProperty = dealerName // <-- ADD THIS
    fun acquisitionDateProperty(): ObjectProperty<LocalDateTime> = acquisitionDate

    // Standard getters
    val vehicleId: String get() = id.get()
    fun getModel(): String = model.get()
    fun getManufacturer(): String = manufacturer.get()
    fun getPrice(): Double = price.get()
    fun getDealerId(): String = dealerId.get()
    fun getDealerName(): String = dealerName.get() // <-- ADD THIS
    fun getAcquisitionDate(): LocalDateTime = acquisitionDate.get()
    fun getIsRentedOut(): Boolean = isRentedOutProperty.get()

    // Setters
    fun setPrice(newPrice: Double) {
        require(newPrice >= 0) { "Price cannot be negative" }
        price.set(newPrice)
    }

    fun setDealer(dealer: Dealer) {
        dealerId.set(dealer.getId())
        dealerName.set(dealer.getName()) // <-- ADD THIS
    }

    fun setDealerId(newDealerId: String) {
        dealerId.set(newDealerId)
    }

    fun setDealerName(newDealerName: String) { // <-- ADD THIS
        dealerName.set(newDealerName)
    }

    fun setAcquisitionDate(newDate: LocalDateTime) {
        acquisitionDate.set(newDate)
    }

    fun setRentedOut(rentedOut: Boolean) {
        isRentedOutProperty.set(rentedOut)
    }

    // Selection support
    fun selectedProperty(): BooleanProperty = selected
    fun isSelected(): Boolean = selected.get()
    fun setSelected(isSelected: Boolean) {
        selected.set(isSelected)
    }

    // Object overrides
    override fun equals(other: Any?): Boolean {
        return other is Vehicle && id.get() == other.id.get()
    }

    override fun hashCode(): Int = Objects.hash(id.get())

    override fun toString(): String {
        return "${javaClass.simpleName} [id: $vehicleId, model: ${getModel()}, manufacturer: ${getManufacturer()}, price: ${getPrice()}, dealerId: ${getDealerId()}, dealerName: ${getDealerName()}, acquisitionDate: ${getAcquisitionDate()}]"
    }
}
