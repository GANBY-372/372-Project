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
    /**
     * Creates a Vehicle object from the given JSON object.
     * This method extracts the necessary attributes from the JSON and maps them to the corresponding vehicle type.
     *
     * @param json the JSON object containing vehicle data
     * @return a Vehicle instance or null if the type is unknown or there was an error processing the data
     */
    @JvmStatic
    fun buildVehicleFromJSON(json: JSONObject): Vehicle? {
        try {
            val manufacturer = json["vehicle_manufacturer"].toString()
            val model = json["vehicle_model"].toString()
            val id = json["vehicle_id"].toString()
            val price = json["price"].toString().toDouble()
            val dealerId = json["dealership_id"].toString()
            val epochMillis = json["acquisition_date"].toString().toLong()

            val acquisitionDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                TimeZone.getDefault().toZoneId()
            )

            val type = json["vehicle_type"].toString().trim { it <= ' ' }.replace("\\s+".toRegex(), "")
                .uppercase(Locale.getDefault())

            return buildVehicle(id, model, manufacturer, price, dealerId, acquisitionDate, false, type)
        } catch (e: Exception) {
            System.err.println("Error creating vehicle from JSON: " + e.message)
            return null
        }
    }

    /**
     * Creates a Vehicle object from the given XML element representing a vehicle.
     * This method extracts the necessary attributes from the XML and maps them to the corresponding vehicle type.
     *
     * @param vehicleElement the XML element containing the vehicle data
     * @param dealerId       the dealer ID associated with this vehicle
     * @return a Vehicle instance or null if the type is unknown or there was an error processing the data
     * @throws IllegalArgumentException if the XML element is null
     */
    @JvmStatic
    fun buildVehicleFromXML(vehicleElement: Element, dealerId: String?, dealerName: String?): Vehicle? {
        requireNotNull(vehicleElement) { "XML vehicle element is null." }

        val manufacturer = vehicleElement.getElementsByTagName("Make").item(0).textContent
        val model = vehicleElement.getElementsByTagName("Model").item(0).textContent
        val id = vehicleElement.getAttribute("id")
        val price = vehicleElement.getElementsByTagName("Price").item(0).textContent.toDouble()

        val type = vehicleElement.getAttribute("type")
        if (type == null || type.isBlank()) {
            println("Skipping vehicle due to missing or empty vehicle type.")
            return null
        }

        // ✅ Extract rent status
        val isRentedNode = vehicleElement.getElementsByTagName("is_rented_out").item(0)
        val isRented = isRentedNode?.textContent?.trim()?.toBooleanStrictOrNull() ?: false

        val acquisitionDate = LocalDateTime.now() // Placeholder for now; can be improved later

        return buildVehicle(id, model, manufacturer, price, dealerId, acquisitionDate, isRented, type)
    }

    /**
     * Creates a Vehicle object based on the provided parameters.
     * This method determines the vehicle type and creates the appropriate instance.
     *
     * @param id              the unique ID of the vehicle
     * @param model           the model of the vehicle
     * @param manufacturer    the manufacturer of the vehicle
     * @param price           the price of the vehicle
     * @param dealerId        the ID of the dealer who owns the vehicle
     * @param acquisitionDate the date the vehicle was acquired
     * @param isRented        whether the vehicle is currently rented
     * @param type            the type of vehicle (e.g., SUV, Sedan)
     * @return a Vehicle instance or null if the type is unknown
     */
    @JvmStatic
    fun buildVehicle(
        id: String?,
        model: String?,
        manufacturer: String?,
        price: Double,
        dealerId: String?,
        acquisitionDate: LocalDateTime,
        isRented: Boolean,
        type: String?
    ): Vehicle? {
        // Safely unwrap all nullable Strings before use
        val nonNullId = requireNotNull(id) { "Vehicle ID cannot be null" }
        val nonNullModel = requireNotNull(model) { "Vehicle model cannot be null" }
        val nonNullManufacturer = requireNotNull(manufacturer) { "Vehicle manufacturer cannot be null" }
        val nonNullDealerId = requireNotNull(dealerId) { "Dealer ID cannot be null" }
        val normalizedType = requireNotNull(type) { "Vehicle type cannot be null" }
            .trim().replace("\\s+".toRegex(), "").uppercase(Locale.getDefault())

        return when (normalizedType) {
            "SUV" -> SUV(nonNullId, nonNullModel, nonNullManufacturer, price, nonNullDealerId, acquisitionDate, isRented)
            "SEDAN" -> Sedan(nonNullId, nonNullModel, nonNullManufacturer, price, nonNullDealerId, acquisitionDate, isRented)
            "PICKUP" -> Pickup(nonNullId, nonNullModel, nonNullManufacturer, price, nonNullDealerId, acquisitionDate, isRented)
            "SPORTSCAR" -> SportsCar(nonNullId, nonNullModel, nonNullManufacturer, price, nonNullDealerId, acquisitionDate, isRented)
            else -> {
                println("Unknown vehicle type: $type")
                null
            }
        }
    }

}
