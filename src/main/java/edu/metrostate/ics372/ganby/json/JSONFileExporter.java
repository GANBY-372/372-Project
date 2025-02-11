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

    // Method to export dealer inventory to a JSON file
    public void exportToFile() {
        // Open file save dialog to select save location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save JSON File");

        // Set default extension to .json
        fileChooser.setSelectedFile(new File("dealer_inventory.json"));

        // Show save file dialog and get user selection
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            System.out.println("Save operation cancelled.");
            return; // User cancelled the save operation
        }

        // Get the selected file path
        File fileToSave = fileChooser.getSelectedFile();

        // Get the list of dealers
        Dealers dealers = DealerCatalog.getInstance().getDealers();

        // Create a JSONArray to hold all the dealers and their vehicles
        JSONArray dealerArray = new JSONArray();

        // Use an Iterator to loop over the dealers
        Iterator<Dealer> dealerIterator = dealers.iterator();

        while (dealerIterator.hasNext()) {
            Dealer dealer = dealerIterator.next();
            // Create a JSONObject for the dealer
            JSONObject dealerJSON = new JSONObject();
            dealerJSON.put("dealer_id", dealer.getId());
            dealerJSON.put("vehicle_acquisition_enabled", dealer.vehicleAcquisitionEnabled());

            // Get vehicles associated with the dealer
            Set<Vehicle> vehicles = dealer.getVehicles();
            JSONArray vehicleArray = new JSONArray();

            // Add each vehicle associated with this dealer to the vehicleArray
            for (Vehicle vehicle : vehicles) {
                vehicleArray.add(JSONObjectBuilder.build(vehicle));
            }

            // Add the vehicle array to the dealer JSON object
            dealerJSON.put("vehicles", vehicleArray);

            // Add the dealer JSON object to the dealer array
            dealerArray.add(dealerJSON);
        }

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
