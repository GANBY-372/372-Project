package edu.metrostate.ics372.ganby.vehicle;

import org.json.simple.JSONObject;
import org.w3c.dom.Element;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * Utility class for building Vehicle instances from JSON and XML data.
 * This class provides methods to create Vehicle objects from both JSON and XML inputs.
 */
public class VehicleBuilder {

    /**
     * Creates a Vehicle object from the given JSON object.
     * This method extracts the necessary attributes from the JSON and maps them to the corresponding vehicle type.
     *
     * @param json the JSON object containing vehicle data
     * @return a Vehicle instance or null if the type is unknown or there was an error processing the data
     */
    public static Vehicle buildVehicleFromJSON(JSONObject json) {
        try {
            String manufacturer = json.get("vehicle_manufacturer").toString();
            String model = json.get("vehicle_model").toString();
            String id = json.get("vehicle_id").toString();
            double price = Double.parseDouble(json.get("price").toString());
            String dealerId = json.get("dealership_id").toString();
            long epochMillis = Long.parseLong(json.get("acquisition_date").toString());

            LocalDateTime acquisitionDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(epochMillis),
                    TimeZone.getDefault().toZoneId()
            );

            String type = json.get("vehicle_type").toString().trim().replaceAll("\\s+", "").toUpperCase();

            return buildVehicle(id, model, manufacturer, price, dealerId, acquisitionDate, false, type);


        } catch (Exception e) {
            System.err.println("Error creating vehicle from JSON: " + e.getMessage());
            return null;
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
    public static Vehicle buildVehicleFromXML(Element vehicleElement, String dealerId, String dealerName) {
        if (vehicleElement == null) {
            throw new IllegalArgumentException("XML vehicle element is null.");
        }

        String manufacturer = vehicleElement.getElementsByTagName("Make").item(0).getTextContent();
        String model = vehicleElement.getElementsByTagName("Model").item(0).getTextContent();
        String id = vehicleElement.getAttribute("id");
        double price = Double.parseDouble(vehicleElement.getElementsByTagName("Price").item(0).getTextContent());

        String type = vehicleElement.getAttribute("type");
        if (type == null || type.isBlank()) {
            System.out.println("Skipping vehicle due to missing or empty vehicle type.");
            return null;
        }

        LocalDateTime acquisitionDate = LocalDateTime.now(); // Placeholder for now; can be improved later

        return buildVehicle(id, model, manufacturer, price, dealerId, acquisitionDate, false, type);
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
    public static Vehicle buildVehicle(String id, String model, String manufacturer, double price, String dealerId, LocalDateTime acquisitionDate, boolean isRented, String type) {
        // Create the appropriate Vehicle based on type
        return switch (type.trim().replaceAll("\\s+", "").toUpperCase()) {
            case "SUV" -> new SUV(id, model, manufacturer, price, dealerId, acquisitionDate, false);
            case "SEDAN" -> new Sedan(id, model, manufacturer, price, dealerId, acquisitionDate, false);
            case "PICKUP" -> new Pickup(id, model, manufacturer, price, dealerId, acquisitionDate, false);
            case "SPORTSCAR" -> new SportsCar(id, model, manufacturer, price, dealerId, acquisitionDate, false);
            default -> {
                System.out.println("Unknown vehicle type: " + type);
                yield null;
            }
        };
    }
}
