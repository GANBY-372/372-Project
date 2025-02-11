package edu.metrostate.ics372.ganby.json;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JSONFileExporter {

    // Export dealer to file
    public void exportToFile(Dealer dealer, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Convert the dealer to JSON format
            String json = convertDealerToJson(dealer);
            writer.write(json);
            System.out.println("Successfully exported to " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while exporting the dealer data: " + e.getMessage());
        }
    }

    // Method to convert a dealer object to a JSON string using json-simple
    private String convertDealerToJson(Dealer dealer) {
        // Create JSON object for the dealer
        JSONObject dealerJson = new JSONObject();
        dealerJson.put("dealerId", dealer.getId());

        // Add vehicles to the dealer JSON
        JSONArray vehiclesJson = new JSONArray();
        for (Vehicle vehicle : dealer.getVehicles()) {
            JSONObject vehicleJson = new JSONObject();
            vehicleJson.put("id", vehicle.getId());
            vehicleJson.put("model", vehicle.getModel());
            vehicleJson.put("manufacturer", vehicle.getManufacturer());
            vehicleJson.put("price", vehicle.getPrice());
            vehicleJson.put("dealer", dealer.toString()); // This is the dealer information

            vehiclesJson.add(vehicleJson);
        }

        // Add vehicle acquisition status to the dealer JSON
        dealerJson.put("acquisitionStatus", dealer.getVehicleAcquisitionStatus());
        dealerJson.put("vehicles", vehiclesJson);

        // Return the JSON string representation of the dealer object
        return dealerJson.toJSONString();
    }
}
