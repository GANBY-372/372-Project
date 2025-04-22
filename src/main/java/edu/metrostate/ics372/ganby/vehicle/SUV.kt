package edu.metrostate.ics372.ganby.vehicle

import java.time.LocalDateTime

/**
 * Represents an SUV vehicle in the system.
 * Extends the [Vehicle] class with SUV-specific identification.
 */
class SUV : Vehicle {
    override val type: String
        /**
         * Returns the type of this vehicle.
         *
         * @return "SUV"
         */
        get() = "SUV"

    /**
     * Constructs an SUV with all attributes, including rent status.
     *
     * @param id              the unique vehicle ID
     * @param model           the model name
     * @param manufacturer    the vehicle's manufacturer
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer owning this vehicle
     * @param acquisitionDate the date the vehicle was acquired
     * @param isRentedOut     whether the vehicle is currently rented
     */
    //In the case rent status is specified
    constructor(
        id: String, model: String, manufacturer: String, price: Double, dealerId: String,
        acquisitionDate: LocalDateTime, isRentedOut: Boolean
    ) : super(id, model, manufacturer, price, dealerId, acquisitionDate, isRentedOut)

    /**
     * Constructs an SUV with a default rent status of false.
     *
     * @param id              the unique vehicle ID
     * @param model           the model name
     * @param manufacturer    the vehicle's manufacturer
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer owning this vehicle
     * @param acquisitionDate the date the vehicle was acquired
     */
    constructor(
        id: String, model: String, manufacturer: String, price: Double, dealerId: String,
        acquisitionDate: LocalDateTime
    ) : super(id, model, manufacturer, price, dealerId, acquisitionDate)

    /**
     * Determines if another object is equal to this SUV.
     * Two SUVs are considered equal if they have the same vehicle ID.
     *
     * @param object the object to compare
     * @return true if the object is an SUV with the same ID, false otherwise
     */
    override fun equals(`object`: Any?): Boolean {
        if (this === `object`) return true
        if (`object` == null || javaClass != `object`.javaClass) return false

        val suv = `object` as SUV
        return id.get() == suv.vehicleId
    }

    /**
     * Returns a string representation of this SUV.
     *
     * @return a formatted string with vehicle type and core details
     */
    override fun toString(): String {
        return type + " " + super.toString()
    }
}
