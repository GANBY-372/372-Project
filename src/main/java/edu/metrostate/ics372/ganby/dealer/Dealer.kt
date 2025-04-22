/**
 * Dealer.java
 * @author B, G
 * This class represents a dealer in the motor dealership system.
 * ID is a unique identifier for the dealer and immutable.
 * vehicleAcquisitionEnabled is a boolean flag that indicates state.
 */
package edu.metrostate.ics372.ganby.dealer

import edu.metrostate.ics372.ganby.vehicle.Vehicle
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import java.util.*

class Dealer {
    private val id: StringProperty // Unique identifier for the dealer
    private var name: StringProperty // Dealer name (optional)
    var isAcquisitionEnabledProperty: BooleanProperty // Whether the dealer is buying vehicles

    /**
     * Get the vehicle collection
     * @return HashMap<String></String>, Vehicle> vehicleCatalog
     */
    @JvmField
    val vehicleCatalog: HashMap<String, Vehicle> // Dealer's vehicle inventory
    private val selected: BooleanProperty = SimpleBooleanProperty(false)

    // TODO: Javadocs missing on methods, add them
    /**
     * Constructor for Dealer if name is not specified
     * @param id String
     */
    constructor(id: String) {
        this.id = SimpleStringProperty(id)
        this.name = SimpleStringProperty("Dealer id#$id")
        this.isAcquisitionEnabledProperty = SimpleBooleanProperty(true)
        this.vehicleCatalog = HashMap()
    }

    /**
     * Constructor for Dealer if name is specified
     * @param id String
     * @param name Name
     */
    constructor(id: String?, name: String?) {
        this.id = SimpleStringProperty(id)
        this.name = SimpleStringProperty(name)
        this.isAcquisitionEnabledProperty = SimpleBooleanProperty(true)
        this.vehicleCatalog = HashMap()
    }

    /**
     * Constructor for Dealer if name and isBuying are specified
     * @param id String
     * @param name Name
     * @param isBuying boolean
     */
    constructor(id: String?, name: String?, isBuying: Boolean) {
        this.id = SimpleStringProperty(id)
        this.name = SimpleStringProperty(name)
        this.isAcquisitionEnabledProperty = SimpleBooleanProperty(isBuying)
        this.vehicleCatalog = HashMap()
    }

    /**
     * Get the dealer id
     * @return String dealer id
     */
    fun getId(): String {
        return id.get()
    }

    fun idProperty(): StringProperty {
        return id
    }

    /**
     * Get the dealer name
     * @return String dealer name
     */
    fun getName(): String {
        return name.get()
    }

    fun nameProperty(): StringProperty {
        return name
    }

    val isVehicleAcquisitionEnabled: Boolean
        /**
         * Get the vehicle acquisition status
         * @return boolean
         */
        get() = isAcquisitionEnabledProperty.get()

    /**
     * Set the dealer acquisition status to true / enabled
     * @param dealerId String
     */
    fun enableVehicleAcquisition(dealerId: String?) {
        isAcquisitionEnabledProperty.set(true)
    }

    /**
     * Set the dealer acquisition status to false / disabled
     * @param dealerId String
     */
    fun disableVehicleAcquisition(dealerId: String?) {
        isAcquisitionEnabledProperty.set(false)
    }

    /**
     * Find a vehicle by its id
     * @param vehicleId String
     * @return Vehicle
     */
    fun findVehicleById(vehicleId: String): Vehicle? {
        return vehicleCatalog[vehicleId]
    }

    /**
     * Add a vehicle to the dealer's vehicle collection
     * @param vehicle Vehicle
     */
    fun addVehicle(vehicle: Vehicle) {
        vehicleCatalog[vehicle.vehicleId] = vehicle
    }

    /**
     * Adds multiple vehicles at once. Will be used for transferring vehicles from one dealer to another.
     * @param vehicles arraylist containing vehicles
     */
    fun addVehicles(vehicles: ArrayList<Vehicle>) {
        for (vehicle in vehicles) {
            addVehicle(vehicle)
        }
    }

    /**
     * Transfer a vehicle from this dealer to another
     * TODO: finish javadoc, add comments to code that give the basics of what the code is doing, @param and @return?
     *
     */
    fun transferVehicle(vehicle: Vehicle?, dealer: Dealer?): Boolean {
        if (vehicle == null || dealer == null) return false
        if (!vehicleCatalog.containsKey(vehicle.vehicleId)) return false

        vehicleCatalog.remove(vehicle.vehicleId)
        vehicle.setDealer(dealer)
        dealer.vehicleCatalog[vehicle.vehicleId] = vehicle

        println("Transferred vehicle " + vehicle.vehicleId + " to dealer " + dealer.getName())
        return true
    }

    /**
     * Equals method to compare two dealers
     * @param object Object
     * @return boolean
     */
    override fun equals(`object`: Any?): Boolean {
        if (`object` === this) return true
        if (`object` !is Dealer) return false
        return getId() == `object`.getId()
    }

    override fun hashCode(): Int {
        return Objects.hash(getId())
    }

    override fun toString(): String {
        return name.get()
    }

    val rentedOutVehicles: ArrayList<Vehicle>
        get() {
            val rentedOutVehicles = ArrayList<Vehicle>()
            for (vehicle in vehicleCatalog.values) {
                if (vehicle.getIsRentedOut()) {
                    rentedOutVehicles.add(vehicle)
                }
            }
            return rentedOutVehicles
        }

    fun getVehiclesByType(type: String?): HashMap<String, Vehicle> {
        val result = HashMap<String, Vehicle>()
        for (vehicle in vehicleCatalog.values) {
            if (vehicle.type.equals(type, ignoreCase = true)) {
                result[vehicle.vehicleId] = vehicle
            }
        }
        return result
    }

    fun transferVehicleSet(vehicles: Set<Vehicle?>?, dealer: Dealer?): Boolean {
        if (vehicles == null || dealer == null) return false
        for (vehicle in vehicles) {
            if (!transferVehicle(vehicle, dealer)) return false
        }
        return true
    }

    fun setName(name: String) {
        this.name.set(name)
    }

    fun selectedProperty(): BooleanProperty {
        return selected
    }

    fun isSelected(): Boolean {
        return selected.get()
    }

    fun setSelected(selected: Boolean) {
        this.selected.set(selected)
    }
}