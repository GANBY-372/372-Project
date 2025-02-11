/**
 * JSONFileExporter.java
 * @autor B, G
 */

package edu.metrostate.ics372.ganby.json;

import edu.metrostate.ics372.ganby.catalog.VehicleCatalog;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSONFileExporter {

    // File object, represents the file to write to
    private final File file;

    // Constructor creates a new file and exports the data
    public JSONFileExporter() {
        file = new File("./data.json");
        exportToFile();
    }

    // Exports the data to the file
    public void exportToFile() {
        // Convert vehicles to a JSON array
        JSONArray jsonArray = VehicleCatalog.getInstance().getVehicles().toJSONArray();

        // Write the JSON array to the file
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonArray.toJSONString()); // Correct method to write JSON data
            fileWriter.flush(); // Ensure data is fully written to the file
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception properly
        }
    }
}