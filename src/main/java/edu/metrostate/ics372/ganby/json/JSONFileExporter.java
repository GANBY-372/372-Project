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
            String json = convertDealerToJson(dealer);
            writer.write(json);
            System.out.println("Successfully exported to " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while exporting the dealer data: " + e.getMessage());
        }
    }

    // Convert a dealer object to a pretty-printed JSON string
    private String convertDealerToJson(Dealer dealer) {
        JSONObject rootJson = new JSONObject();
        JSONArray carInventoryJson = new JSONArray();

        for (Vehicle vehicle : dealer.getVehicles()) {
            JSONObject vehicleJson = new JSONObject();
            vehicleJson.put("dealership_id", String.valueOf(dealer.getId()));
            vehicleJson.put("vehicle_type", vehicle.getModel());
            vehicleJson.put("vehicle_manufacturer", vehicle.getManufacturer());
            vehicleJson.put("vehicle_model", vehicle.getModel());
            vehicleJson.put("vehicle_id", String.valueOf(vehicle.getId()));
            vehicleJson.put("price", vehicle.getPrice());
            vehicleJson.put("acquisition_date", vehicle.getAcquisitionDate());

            carInventoryJson.add(vehicleJson);
        }

        rootJson.put("car_inventory", carInventoryJson);

        // Pretty-print the JSON output
        return prettyPrintJson(rootJson.toJSONString());
    }

    // Manually format JSON for vertical output
    private String prettyPrintJson(String json) {
        StringBuilder formattedJson = new StringBuilder();
        int indentLevel = 0;
        boolean inQuotes = false;

        for (char c : json.toCharArray()) {
            switch (c) {
                case '{':
                case '[':
                    formattedJson.append(c);
                    if (!inQuotes) {
                        formattedJson.append("\n").append("  ".repeat(++indentLevel));
                    }
                    break;
                case '}':
                case ']':
                    if (!inQuotes) {
                        formattedJson.append("\n").append("  ".repeat(--indentLevel));
                    }
                    formattedJson.append(c);
                    break;
                case ',':
                    formattedJson.append(c);
                    if (!inQuotes) {
                        formattedJson.append("\n").append("  ".repeat(indentLevel));
                    }
                    break;
                case ':':
                    formattedJson.append(c).append(" ");
                    break;
                case '"':
                    formattedJson.append(c);
                    inQuotes = !inQuotes;
                    break;
                default:
                    formattedJson.append(c);
                    break;
            }
        }

        return formattedJson.toString();
    }
}
