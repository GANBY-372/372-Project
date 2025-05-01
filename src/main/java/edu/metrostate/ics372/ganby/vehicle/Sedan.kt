package edu.metrostate.ics372.ganby.vehicle

import java.time.LocalDateTime

/**
 * Represents a Sedan vehicle. A subclass of [Vehicle].
 * Sedans can be rented out and are treated as regular vehicles in the system.
 */
class Sedan : Vehicle {
    override val type: String
        /**
         * Returns the vehicle type.
         *
         * @return a string indicating the type of this vehicle ("Sedan")
         */
        get() = "Sedan"

    /**
     * Constructs a Sedan with the specified parameters and explicit rent status.
     *
     * @param id              the unique vehicle ID
     * @param model           the model name of the sedan
     * @param manufacturer    the vehicle manufacturer
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer owning this vehicle
     * @param acquisitionDate the date the vehicle was acquired
     * @param isRentedOut     whether the vehicle is currently rented out
     */
    //In the case rent status is specified
    constructor(
        id: String, model: String, manufacturer: String, price: Double, dealerId: String,
        acquisitionDate: LocalDateTime, isRentedOut: Boolean
    ) : super(id, model, manufacturer, price, dealerId, acquisitionDate, isRentedOut)

    /**
     * Constructs a Sedan with the specified parameters and default rent status (false).
     *
     * @param id              the unique vehicle ID
     * @param model           the model name of the sedan
     * @param manufacturer    the vehicle manufacturer
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer owning this vehicle
     * @param acquisitionDate the date the vehicle was acquired
     */
    constructor(
        id: String, model: String, manufacturer: String, price: Double, dealerId: String,
        acquisitionDate: LocalDateTime
    ) : super(id, model, manufacturer, price, dealerId, acquisitionDate)

    /**
     * Compares this Sedan to another object for equality.
     * Two vehicles are considered equal if their IDs match.
     *
     * @param object the object to compare
     * @return true if the object is a Sedan with the same ID; false otherwise
     */
    override fun equals(`object`: Any?): Boolean {
        if (`object` === this) return true
        if (`object` == null) return false
        if (`object` is Sedan) {
            return getVehicleId() == `object`.getVehicleId()
        }
        return false
    }

    /**
     * Returns a string representation of the Sedan.
     *
     * @return a string containing the vehicle type and details
     */
    override fun toString(): String {
        return type + " " + super.toString()
    }
}
