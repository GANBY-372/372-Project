package edu.metrostate.ics372.ganby.json;

import edu.metrostate.ics372.ganby.catalog.VehicleCatalog;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSONFileExporter {

    private final File file;

    public JSONFileExporter() {
        file = new File("./data.json");
        exportToFile();
    }

    public void exportToFile() {
        JSONArray jsonArray = VehicleCatalog.getInstance().getVehicles().toJSONArray();

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonArray.toJSONString()); // Correct method to write JSON data
            fileWriter.flush(); // Ensure data is fully written to the file
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception properly
        }
    }
}