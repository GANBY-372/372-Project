package edu.metrostate.ics372.ganby.json;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.Dealers;
import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class JSONFileExporter {

    public void exportToFile(Dealer dealer) {
        // Open file save dialog to select save location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save JSON File");

        // Set default extension to .json
        fileChooser.setSelectedFile(new File("dealer #" +dealer.getId() + " inventory.json"));

        // Show save file dialog and get user selection
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            System.out.println("Save operation cancelled.");
            return; // User cancelled the save operation
        }

        // Get the selected file path
        File fileToSave = fileChooser.getSelectedFile();

        // Create a JSONArray to hold the dealer's information and their vehicles
        JSONArray dealerArray = new JSONArray(); // Only one dealer, but we still use an array

        // Create a JSONObject for the dealer
        JSONObject dealerJSON = new JSONObject();
        dealerJSON.put("dealer_id", dealer.getId());
        dealerJSON.put("vehicle_acquisition_enabled", dealer.getVehicleAcquisitionStatus());

        // Get vehicles associated with the dealer
        Set<Vehicle> vehicles = dealer.getVehicles();
        JSONArray vehicleArray = new JSONArray(); // Array to store vehicles for this dealer

        // Add each vehicle associated with this dealer to the vehicleArray
        for (Vehicle vehicle : vehicles) {
            vehicleArray.add(JSONObjectBuilder.build(vehicle)); // Assuming build() converts Vehicle to JSONObject
        }

        // Add the vehicle array to the dealer JSON object
        dealerJSON.put("vehicles", vehicleArray);

        // Add the dealer JSON object to the dealer array (even though it only has one dealer)
        dealerArray.add(dealerJSON);

        // Write the dealer array to the selected file with pretty print
        try (FileWriter file = new FileWriter(fileToSave)) {
            // Format the JSON output with indentation for better readability
            file.write(dealerArray.toJSONString().replaceAll("},", "},\n"));
            System.out.println("Successfully exported dealer inventory to JSON file.");
        } catch (IOException e) {
            System.out.println("An error occurred while exporting to JSON: " + e.getMessage());
        }
    }


}
