package edu.metrostate.ics372.ganby.vehicle

import java.time.LocalDateTime

/**
 * Represents a sports car, which cannot be rented out.
 */
class SportsCar : Vehicle {
    override val type: String
        /**
         * Returns the vehicle type.
         *
         * @return "SportsCar"
         */
        get() = "SportsCar"

    /**
     * Constructs a new SportsCar with an explicit rent status, but always forces rent status to false.
     *
     * @param id               the unique vehicle ID
     * @param model            the model name
     * @param manufacturer     the manufacturer name
     * @param price            the price of the vehicle
     * @param dealerId         the dealer's ID who owns the vehicle
     * @param acquisitionDate  the date the vehicle was acquired
     * @param isRentedOut      ignored - SportsCar is never rented
     */
    //In the case rent status is specified
    constructor(
        id: String, model: String, manufacturer: String, price: Double, dealerId: String,
        acquisitionDate: LocalDateTime, isRentedOut: Boolean
    ) : super(id, model, manufacturer, price, dealerId, acquisitionDate, false)

    /**
     * Constructs a new SportsCar, defaulting rent status to false.
     *
     * @param id              the unique vehicle ID
     * @param model           the model name
     * @param manufacturer    the manufacturer name
     * @param price           the price of the vehicle
     * @param dealerId        the dealer's ID who owns the vehicle
     * @param acquisitionDate the date the vehicle was acquired
     */
    constructor(
        id: String, model: String, manufacturer: String, price: Double, dealerId: String,
        acquisitionDate: LocalDateTime
    ) : super(id, model, manufacturer, price, dealerId, acquisitionDate, false)

    /**
     * Equality based on vehicle ID.
     *
     * @param object another object
     * @return true if same vehicle ID
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
     * Returns String representation of object
     *
     * another object
     * @return String representation of object
     */
    override fun toString(): String {
        return type + " " + super.toString()
    }
}
