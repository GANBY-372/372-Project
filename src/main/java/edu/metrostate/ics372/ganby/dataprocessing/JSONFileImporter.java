package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class JSONFileImporter {

    // JSON Keys
    public static final String DEALER_ID_KEY = "dealership_id";
    public static final String VEHICLE_ID_KEY = "vehicle_id";
    public static final String VEHICLE_MANUFACTURER_KEY = "vehicle_manufacturer";
    public static final String VEHICLE_MODEL_KEY = "vehicle_model";
    public static final String VEHICLE_TYPE_KEY = "vehicle_type";
    public static final String PRICE_KEY = "price";
    public static final String ACQUISITION_DATE_KEY = "acquisition_date";

    private JSONArray jsonArray;

    /**
     * GUI-based constructor that opens a file chooser.
     */
    public JSONFileImporter(Stage primaryStage) throws IOException, ParseException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a JSON File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            parseFile(selectedFile.getAbsolutePath());
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    /**
     * Constructor for headless usage (e.g. autosave import).
     */
    public JSONFileImporter(String filePath) throws IOException, ParseException {
        parseFile(filePath);
    }

    /**
     * Parses a JSON file and stores the data internally.
     */
    private void parseFile(String filePath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            this.jsonArray = (JSONArray) jsonObject.get("car_inventory");
        }
    }

    /**
     * Processes the parsed JSON and loads vehicles into the DealerCatalog.
     */
    public void processJSON() {
        if (jsonArray == null || jsonArray.isEmpty()) return;

        for (Object obj : jsonArray) {
            try {
                createVehicle((JSONObject) obj);
            } catch (Exception e) {
                System.err.println("Skipping invalid vehicle: " + e.getMessage());
            }
        }
    }

    /**
     * Creates and adds a vehicle to the catalog.
     * @param jsonObject json object that contains vehicle info
     */
    private Vehicle createVehicle(JSONObject jsonObject) {
        String manufacturer = jsonObject.get(VEHICLE_MANUFACTURER_KEY).toString();
        String model = jsonObject.get(VEHICLE_MODEL_KEY).toString();
        String id = jsonObject.get(VEHICLE_ID_KEY).toString();
        double price = Double.parseDouble(jsonObject.get(PRICE_KEY).toString());
        String dealerId = jsonObject.get(DEALER_ID_KEY).toString();
        long epochMillis = Long.parseLong(jsonObject.get(ACQUISITION_DATE_KEY).toString());

        LocalDateTime acquisitionDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                TimeZone.getDefault().toZoneId()
        );

        String type = jsonObject.get(VEHICLE_TYPE_KEY).toString().trim();


        Vehicle vehicle = VehicleBuilder.buildVehicleFromJSON(jsonObject);

        if (vehicle != null) {
            DealerCatalog.getInstance().addVehicle(vehicle);
        }

        return vehicle;
    }
}