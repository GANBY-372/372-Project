/**
 * Dealer.kt
 * Represents a dealer in the motor dealership system.
 * Each dealer has a unique ID, name, acquisition status, and a catalog of vehicles.
 *
 * @author B, G
 */

package edu.metrostate.ics372.ganby.dealer

import edu.metrostate.ics372.ganby.vehicle.Vehicle
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import java.util.*

/**
 * Main Dealer class with JavaFX property bindings for use in a GUI.
 *
 * @property id Immutable dealer ID.
 * @property name Dealer's name.
 * @property isAcquisitionEnabledProperty Property indicating whether vehicle acquisition is enabled.
 */
class Dealer(id: String, name: String? = null, isBuying: Boolean? = null) {

    // Overloaded constructors for Java compatibility
    constructor(id: String) : this(id, null, null)
    constructor(id: String, name: String) : this(id, name, null)

    private val id: StringProperty = SimpleStringProperty(id)
    private var name: StringProperty = SimpleStringProperty(name ?: "Dealer id#$id")
    var isAcquisitionEnabledProperty: BooleanProperty = SimpleBooleanProperty(isBuying ?: true)

    /** Vehicle catalog mapped by vehicle ID */
    @JvmField
    val vehicleCatalog: HashMap<String, Vehicle> = HashMap()

    /** Selection property used for UI checkboxes */
    private val selected: BooleanProperty = SimpleBooleanProperty(false)

    /** Returns the dealer ID. */
    fun getId(): String = id.get()

    /** Returns the dealer ID as a JavaFX property. */
    fun idProperty(): StringProperty = id

    /** Returns the dealer name. */
    fun getName(): String = name.get()

    /** Returns the dealer name as a JavaFX property. */
    fun nameProperty(): StringProperty = name

    /** Indicates if vehicle acquisition is currently enabled for this dealer. */
    val isVehicleAcquisitionEnabled: Boolean
        get() = isAcquisitionEnabledProperty.get()

    /** Enables vehicle acquisition for the dealer. */
    fun enableVehicleAcquisition() {
        isAcquisitionEnabledProperty.set(true)
    }

    /** Disables vehicle acquisition for the dealer. */
    fun disableVehicleAcquisition() {
        isAcquisitionEnabledProperty.set(false)
    }

    /**
     * Finds a vehicle in this dealer's catalog by its ID.
     * @param vehicleId The ID of the vehicle to look up.
     * @return The Vehicle if found, or null.
     */
    fun findVehicleById(vehicleId: String): Vehicle? = vehicleCatalog[vehicleId]

    /**
     * Adds a single vehicle to the dealer's catalog.
     * @param vehicle The vehicle to add.
     */
    fun addVehicle(vehicle: Vehicle) {
        vehicleCatalog[vehicle.getVehicleId()] = vehicle
    }

    /**
     * Adds multiple vehicles to the dealer's catalog.
     * @param vehicles List of vehicles to add.
     */
    fun addVehicles(vehicles: ArrayList<Vehicle>) {
        vehicles.forEach { addVehicle(it) }
    }

    /**
     * Transfers a vehicle from this dealer to another dealer.
     *
     * @param vehicle The vehicle to transfer.
     * @param dealer The receiving dealer.
     * @return True if successful, false otherwise.
     */
    fun transferVehicle(vehicle: Vehicle?, dealer: Dealer?): Boolean {
        if (vehicle == null || dealer == null) return false
        if (!vehicleCatalog.containsKey(vehicle.getVehicleId())) return false

        vehicleCatalog.remove(vehicle.getVehicleId())
        vehicle.setDealer(dealer)
        dealer.vehicleCatalog[vehicle.getVehicleId()] = vehicle

        println("Transferred vehicle ${vehicle.getVehicleId()} to dealer ${dealer.getName()}")
        return true
    }

    /** Returns true if dealer IDs match. */
    override fun equals(other: Any?): Boolean {
        return other is Dealer && getId() == other.getId()
    }

    /** Generates hash based on dealer ID. */
    override fun hashCode(): Int = Objects.hash(getId())

    /** Returns the dealer name for display. */
    override fun toString(): String = name.get()

    /** Returns a list of all rented-out vehicles in the dealer's catalog. */
    val rentedOutVehicles: ArrayList<Vehicle>
        get() = ArrayList(vehicleCatalog.values.filter { it.getIsRentedOut() })

    /**
     * Returns a map of vehicles filtered by a given type.
     * @param type The vehicle type (e.g., "SUV", "Sedan").
     * @return Map of vehicles of the given type.
     */
    fun getVehiclesByType(type: String?): HashMap<String, Vehicle> {
        val result = HashMap<String, Vehicle>()
        for (vehicle in vehicleCatalog.values) {
            if (vehicle.type.equals(type, ignoreCase = true)) {
                result[vehicle.getVehicleId()] = vehicle
            }
        }
        return result
    }

    /**
     * Transfers a set of vehicles to another dealer.
     * @param vehicles Set of vehicles to transfer.
     * @param dealer The receiving dealer.
     * @return True if all transfers succeed, false if any fail.
     */
    fun transferVehicleSet(vehicles: Set<Vehicle?>?, dealer: Dealer?): Boolean {
        if (vehicles == null || dealer == null) return false
        return vehicles.all { transferVehicle(it, dealer) }
    }

    /**
     * Updates the dealer's name.
     * @param name New name for the dealer.
     */
    fun setName(name: String) {
        this.name.set(name)
    }

    /** Returns the selected state as a JavaFX property (for checkboxes). */
    fun selectedProperty(): BooleanProperty = selected

    /** Returns whether this dealer is currently selected in the UI. */
    fun isSelected(): Boolean = selected.get()

    /** Sets the selection state of the dealer. */
    fun setSelected(selected: Boolean) {
        this.selected.set(selected)
    }
}
