package edu.metrostate.ics372.ganby.json;

import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.TimeZone;

public class JSONObjectBuilder {

    public JSONObjectBuilder() {}

    public static JSONObject build (Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dealer_id", vehicle.getId());
        jsonObject.put("vehicle_manufacturer", vehicle.getManufacturer());
        jsonObject.put("vehicle_type", vehicle.getClass().getSimpleName());
        jsonObject.put("vehicle_model", vehicle.getModel());
        jsonObject.put("vehicle_price", vehicle.getPrice());
        jsonObject.put("acquisition_date",
            vehicle.getAcquisitionDate()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant().toEpochMilli()
        );
        validateJSON(jsonObject);
        return jsonObject;
    }

    private static void validateJSON(JSONObject jsonObject) {
        JSONParser parser = new JSONParser();
        try {
            parser.parse(jsonObject.toJSONString());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid JSON structure: " + e.getMessage(), e);
        }
    }
}