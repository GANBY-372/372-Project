package edu.metrostate.ics372.ganby.vehicle

import java.time.LocalDateTime

/**
 * Represents a Pickup vehicle in the system.
 * Inherits all standard behavior from the [Vehicle] superclass.
 */
class Pickup : Vehicle {
    override val type: String
        /**
         * Returns the vehicle type string.
         *
         * @return "Pickup"
         */
        get() = "Pickup"

    /**
     * Constructs a Pickup with all attributes, including rent status.
     *
     * @param id              the unique ID of the vehicle
     * @param model           the model of the vehicle
     * @param manufacturer    the manufacturer of the vehicle
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer who owns the vehicle
     * @param acquisitionDate the date the vehicle was acquired
     * @param isRentedOut     whether the vehicle is currently rented
     */
    //In the case rent status is specified
    constructor(
        id: String, model: String, manufacturer: String, price: Double, dealerId: String,
        acquisitionDate: LocalDateTime, isRentedOut: Boolean
    ) : super(id, model, manufacturer, price, dealerId, acquisitionDate, isRentedOut)

    /**
     * Constructs a Pickup with a default rent status of false.
     *
     * @param id              the unique ID of the vehicle
     * @param model           the model of the vehicle
     * @param manufacturer    the manufacturer of the vehicle
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer who owns the vehicle
     * @param acquisitionDate the date the vehicle was acquired
     */
    constructor(
        id: String, model: String, manufacturer: String, price: Double, dealerId: String,
        acquisitionDate: LocalDateTime
    ) : super(id, model, manufacturer, price, dealerId, acquisitionDate)

    /**
     * Checks if another object is equal to this Pickup.
     * Two vehicles are equal if their IDs are the same.
     *
     * @param object the object to compare
     * @return true if the object is a Vehicle and has the same ID
     */
    override fun equals(`object`: Any?): Boolean {
        if (`object` === this) return true
        if (`object` == null) return false
        if (`object` is Vehicle) {
            return getVehicleId() == `object`.getVehicleId()
        }
        return false
    }


    /**
     * Returns a string representation of this Pickup.
     *
     * @return a string showing the vehicle type and details
     */
    override fun toString(): String {
        return type + " " + super.toString()
    }
}
