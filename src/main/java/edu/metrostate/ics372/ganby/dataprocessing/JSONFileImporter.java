package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import edu.metrostate.ics372.ganby.vehicle.VehicleBuilder;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * Handles importing vehicle inventory data from a JSON file into the system.
 * Supports both direct file path input and GUI file selection via JavaFX Stage.
 */
public class JSONFileImporter {

    // JSON key constants used to extract vehicle and dealership information from the JSON structure.
    public static final String DEALER_ID_KEY = "dealership_id";
    public static final String VEHICLE_ID_KEY = "vehicle_id";
    public static final String VEHICLE_MANUFACTURER_KEY = "vehicle_manufacturer";
    public static final String VEHICLE_MODEL_KEY = "vehicle_model";
    public static final String VEHICLE_TYPE_KEY = "vehicle_type";
    public static final String PRICE_KEY = "price";
    public static final String ACQUISITION_DATE_KEY = "acquisition_date";
    public static final String ACQUISITION_STATUS_KEY = "acquisition_status";
    public static final String IS_RENTED_KEY = "is_rented_out";
    public static final String IS_RENTED_OUT_KEY = "is_rented_out";

    private JSONArray jsonArray;

    /**
     * Constructs a JSONFileImporter using a file selected via JavaFX file chooser.
     * If the user cancels the dialog, no file is processed.
     *
     * @param primaryStage the JavaFX stage to anchor the file chooser
     * @throws IOException if the selected file cannot be read
     * @throws ParseException if the JSON is malformed
     */
    public JSONFileImporter(Stage primaryStage) throws IOException, ParseException {
        File selectedFile = FileSelector.chooseJsonOpenFile(primaryStage);
        if (selectedFile != null) {
            parseFile(selectedFile.getAbsolutePath());
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    /**
     * Constructs a JSONFileImporter with a given file path.
     *
     * @param filePath the path to the JSON file
     * @throws IOException if the file cannot be read
     * @throws ParseException if the JSON is malformed
     */
    public JSONFileImporter(String filePath) throws IOException, ParseException {
        parseFile(filePath);
    }

    /**
     * Parses the JSON file at the specified path and loads the car inventory array.
     *
     * @param filePath path to the JSON file
     * @throws IOException if the file cannot be read
     * @throws ParseException if the JSON is malformed
     */
    private void parseFile(String filePath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            this.jsonArray = (JSONArray) jsonObject.get("car_inventory");
        }
    }

    /**
     * Processes the loaded JSON array, creating and registering vehicles from its contents.
     *
     * @return
     */
    public boolean processJSON() {
        if (jsonArray == null || jsonArray.isEmpty()) return false;

        for (Object obj : jsonArray) {
            try {
                createVehicle((JSONObject) obj);
            } catch (Exception e) {
                System.err.println("Skipping invalid vehicle: " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * Creates a Vehicle object from the given JSON object and adds it to the DealerCatalog.
     *
     * @param jsonObject JSON object representing a vehicle
     * @return the created Vehicle instance, or null if creation failed
     */
    private Vehicle createVehicle(JSONObject jsonObject) {
        String manufacturer = jsonObject.get(VEHICLE_MANUFACTURER_KEY).toString();
        String model = jsonObject.get(VEHICLE_MODEL_KEY).toString();
        String id = jsonObject.get(VEHICLE_ID_KEY).toString();
        double price = Double.parseDouble(jsonObject.get(PRICE_KEY).toString());
        String dealerId = jsonObject.get(DEALER_ID_KEY).toString();
        long epochMillis = Long.parseLong(jsonObject.get(ACQUISITION_DATE_KEY).toString());

        Object rentedOutObj = jsonObject.get(IS_RENTED_KEY);
        boolean isRentedOut = false;
        if (rentedOutObj != null) {
            isRentedOut = Boolean.parseBoolean(rentedOutObj.toString());
            System.out.println(isRentedOut);
        } else {
            System.out.println("is_rented_out NOT found during import");
        }

        LocalDateTime acquisitionDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                TimeZone.getDefault().toZoneId()
        );

        Vehicle vehicle = VehicleBuilder.buildVehicleFromJSON(jsonObject);

        if (vehicle != null) {
            DealerCatalog.getInstance().addVehicle(vehicle);
        }

        return vehicle;
    }

    /**
     * Standalone test method to import vehicles from a predefined JSON file path.
     * Not used in GUI — available for manual testing only.
     */
    private void runJsonPathImport() {
        try {
            System.out.println("\n[1] JSON import via file path:");
            JSONFileImporter importer = new JSONFileImporter("src/main/resources/Imports/inventory.json");
            importer.processJSON();
        } catch (Exception e) {
            System.err.println("JSON (path) import failed: " + e.getMessage());
        }
    }
}
