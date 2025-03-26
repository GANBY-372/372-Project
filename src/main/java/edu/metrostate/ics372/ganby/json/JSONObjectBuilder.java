/**
 * JSONObjectBuilder.java
 * @autor B, G
 * This class is for building JSON objects.
 */
package edu.metrostate.ics372.ganby.json;

import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.TimeZone;

public class JSONObjectBuilder {

    public JSONObjectBuilder() {}

    // TODO: Maybe change the name to something more descriptive
    /**
     * Build a JSON object from a vehicle object
     * @param vehicle the vehicle object
     * @return the JSON object
     */
    public static JSONObject build (Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        // Create a new JSON object
        JSONObject jsonObject = new JSONObject();

        // Add vehicle data to the JSON object
        jsonObject.put("dealer_id", vehicle.getId());
        jsonObject.put("vehicle_manufacturer", vehicle.getManufacturer());
        jsonObject.put("vehicle_type", vehicle.getClass().getSimpleName());
        jsonObject.put("vehicle_model", vehicle.getModel());
        jsonObject.put("vehicle_price", vehicle.getPrice());
        jsonObject.put("acquisition_date",
            vehicle.getAcquisitionDate()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant().toEpochMilli()
        );

        // Validate the JSON object
        validateJSON(jsonObject);
        return jsonObject;
    }

    /**
     * Validate a JSON object
     * @param jsonObject the JSON object
     * @throws RuntimeException if the JSON object is invalid
     */
    private static void validateJSON(JSONObject jsonObject) {
        JSONParser parser = new JSONParser();
        try {
            // Parse the JSON object to validate it
            parser.parse(jsonObject.toJSONString());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid JSON structure: " + e.getMessage(), e);
        }
    }
}