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


class Dealer(id: String, name: String? = null, isBuying: Boolean? = null) {
    // Java-compatible constructor overloads
    constructor(id: String) : this(id, null, null)
    constructor(id: String, name: String) : this(id, name, null)

    private val id: StringProperty = SimpleStringProperty(id)
    private var name: StringProperty = SimpleStringProperty(name ?: "Dealer id#$id")
    var isAcquisitionEnabledProperty: BooleanProperty = SimpleBooleanProperty(isBuying ?: true)


    @JvmField
    val vehicleCatalog: HashMap<String, Vehicle> = HashMap()
    private val selected: BooleanProperty = SimpleBooleanProperty(false)

    fun getId(): String = id.get()
    fun idProperty(): StringProperty = id

    fun getName(): String = name.get()
    fun nameProperty(): StringProperty = name

    val isVehicleAcquisitionEnabled: Boolean
    get() = isAcquisitionEnabledProperty.get()

    fun enableVehicleAcquisition() {
        isAcquisitionEnabledProperty.set(true)
    }

    fun disableVehicleAcquisition() {
        isAcquisitionEnabledProperty.set(false)
    }

    fun findVehicleById(vehicleId: String): Vehicle? = vehicleCatalog[vehicleId]

    fun addVehicle(vehicle: Vehicle) {
        vehicleCatalog[vehicle.getVehicleId()] = vehicle
    }

    fun addVehicles(vehicles: ArrayList<Vehicle>) {
        vehicles.forEach { addVehicle(it) }
    }

    fun transferVehicle(vehicle: Vehicle?, dealer: Dealer?): Boolean {
        if (vehicle == null || dealer == null) return false
        if (!vehicleCatalog.containsKey(vehicle.getVehicleId())) return false

        vehicleCatalog.remove(vehicle.getVehicleId())
        vehicle.setDealer(dealer)
        dealer.vehicleCatalog[vehicle.getVehicleId()] = vehicle

        println("Transferred vehicle ${vehicle.getVehicleId()} to dealer ${dealer.getName()}")
        return true
    }

    override fun equals(other: Any?): Boolean {
        return other is Dealer && getId() == other.getId()
    }

    override fun hashCode(): Int = Objects.hash(getId())

    override fun toString(): String = name.get()

    val rentedOutVehicles: ArrayList<Vehicle>
    get() = ArrayList(vehicleCatalog.values.filter { it.getIsRentedOut() })

    fun getVehiclesByType(type: String?): HashMap<String, Vehicle> {
        val result = HashMap<String, Vehicle>()
        for (vehicle in vehicleCatalog.values) {
            if (vehicle.type.equals(type, ignoreCase = true)) {
                result[vehicle.getVehicleId()] = vehicle
            }
        }
        return result
    }

    fun transferVehicleSet(vehicles: Set<Vehicle?>?, dealer: Dealer?): Boolean {
        if (vehicles == null || dealer == null) return false
        return vehicles.all { transferVehicle(it, dealer) }
    }

    fun setName(name: String) {
        this.name.set(name)
    }

    fun selectedProperty(): BooleanProperty = selected
    fun isSelected(): Boolean = selected.get()
    fun setSelected(selected: Boolean) {
        this.selected.set(selected)
    }
}
