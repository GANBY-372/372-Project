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
        // Create the main JSON object
        JSONObject rootJson = new JSONObject();

        // Create a JSON array for car inventory
        JSONArray carInventoryJson = new JSONArray();

        for (Vehicle vehicle : dealer.getVehicles()) {
            JSONObject vehicleJson = new JSONObject();
            vehicleJson.put("dealership_id", String.valueOf(dealer.getId()));
            vehicleJson.put("vehicle_type", vehicle.getManufacturer()); // Assuming Vehicle class has a getType() method
            vehicleJson.put("vehicle_manufacturer", vehicle.getManufacturer());
            vehicleJson.put("vehicle_model", vehicle.getModel());
            vehicleJson.put("vehicle_id", String.valueOf(vehicle.getId()));
            vehicleJson.put("price", vehicle.getPrice());
            vehicleJson.put("acquisition_date", vehicle.getAcquisitionDate()); // Assuming acquisitionDate is Instant

            carInventoryJson.add(vehicleJson);
        }

        // Add car inventory array to root JSON
        rootJson.put("car_inventory", carInventoryJson);

        // Return the JSON string representation of the dealer object
        return rootJson.toJSONString();
    }
}
