package edu.metrostate.ics372.ganby.vehicle

import org.json.simple.JSONObject
import org.w3c.dom.Element
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

/**
 * Utility class for building Vehicle instances from JSON and XML data.
 * This class provides methods to create Vehicle objects from both JSON and XML inputs.
 */
object VehicleBuilder {

    @JvmStatic
    fun buildVehicleFromJSON(json: JSONObject): Vehicle? {
        try {
            val manufacturer = json["vehicle_manufacturer"].toString()
            val model = json["vehicle_model"].toString()
            val id = json["vehicle_id"].toString()
            val price = json["price"].toString().toDouble()
            val dealerId = json["dealership_id"].toString()
            val epochMillis = json["acquisition_date"].toString().toLong()
            val dealerName = json["dealer_name"].toString()
            val acquisitionDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                TimeZone.getDefault().toZoneId()
            )

            val type = json["vehicle_type"].toString().trim { it <= ' ' }
                .replace("\\s+".toRegex(), "")
                .uppercase(Locale.getDefault())

            return buildVehicle(id, model, manufacturer, price, dealerId, dealerName, acquisitionDate, false, type)
        } catch (e: Exception) {
            System.err.println("Error creating vehicle from JSON: " + e.message)
            return null
        }
    }

    @JvmStatic
    fun buildVehicleFromXML(vehicleElement: Element, dealerId: String?, dealerName: String?): Vehicle? {
        requireNotNull(vehicleElement) { "XML vehicle element is null." }
        requireNotNull(dealerName) { "Dealer name cannot be null." }

        val manufacturer = vehicleElement.getElementsByTagName("Make").item(0).textContent
        val model = vehicleElement.getElementsByTagName("Model").item(0).textContent
        val id = vehicleElement.getAttribute("id")
        val price = vehicleElement.getElementsByTagName("Price").item(0).textContent.toDouble()

        val type = vehicleElement.getAttribute("type")
        if (type == null || type.isBlank()) {
            println("Skipping vehicle due to missing or empty vehicle type.")
            return null
        }

        val acquisitionDate = LocalDateTime.now() // You can improve this later if needed

        return buildVehicle(id, model, manufacturer, price, dealerId, dealerName, acquisitionDate, false, type)
    }

    @JvmStatic
    fun buildVehicle(
        id: String?,
        model: String?,
        manufacturer: String?,
        price: Double,
        dealerId: String?,
        dealerName: String?,
        acquisitionDate: LocalDateTime,
        isRented: Boolean,
        type: String?
    ): Vehicle? {
        val nonNullId = requireNotNull(id) { "Vehicle ID cannot be null" }
        val nonNullModel = requireNotNull(model) { "Vehicle model cannot be null" }
        val nonNullManufacturer = requireNotNull(manufacturer) { "Vehicle manufacturer cannot be null" }
        val nonNullDealerId = requireNotNull(dealerId) { "Dealer ID cannot be null" }
        val nonNullDealerName = requireNotNull(dealerName) { "Dealer Name cannot be null" }
        val normalizedType = requireNotNull(type) { "Vehicle type cannot be null" }
            .trim().replace("\\s+".toRegex(), "").uppercase(Locale.getDefault())

        return when (normalizedType) {
            "SUV" -> SUV(nonNullId, nonNullModel, nonNullManufacturer, price, nonNullDealerId, nonNullDealerName, acquisitionDate, isRented)
            "SEDAN" -> Sedan(nonNullId, nonNullModel, nonNullManufacturer, price, nonNullDealerId, nonNullDealerName, acquisitionDate, isRented)
            "PICKUP" -> Pickup(nonNullId, nonNullModel, nonNullManufacturer, price, nonNullDealerId, nonNullDealerName, acquisitionDate, isRented)
            "SPORTSCAR" -> SportsCar(nonNullId, nonNullModel, nonNullManufacturer, price, nonNullDealerId, nonNullDealerName, acquisitionDate, isRented)
            else -> {
                println("Unknown vehicle type: $type")
                null
            }
        }
    }
}
