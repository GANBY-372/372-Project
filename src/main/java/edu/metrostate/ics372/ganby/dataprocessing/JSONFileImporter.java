package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.dealer.Dealer;
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
 * Imports vehicle and dealer data from a JSON file into the application's data model.
 * Handles parsing, dealer creation, vehicle construction, and property initialization.
 */
public class JSONFileImporter {

    // JSON keys used in the file
    public static final String DEALER_ID_KEY = "dealership_id";
    public static final String DEALER_NAME_KEY = "dealer_name";
    public static final String VEHICLE_ID_KEY = "vehicle_id";
    public static final String VEHICLE_MANUFACTURER_KEY = "vehicle_manufacturer";
    public static final String VEHICLE_MODEL_KEY = "vehicle_model";
    public static final String VEHICLE_TYPE_KEY = "vehicle_type";
    public static final String PRICE_KEY = "price";
    public static final String ACQUISITION_DATE_KEY = "acquisition_date";
    public static final String ACQUISITION_STATUS_KEY = "acquisition_status";
    public static final String IS_RENTED_KEY = "is_rented_out";

    private JSONArray jsonArray;

    /**
     * Constructs the importer using a JavaFX file chooser to select the JSON file.
     * @param primaryStage The primary window used to show the dialog.
     * @throws IOException if file reading fails.
     * @throws ParseException if JSON is invalid.
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
     * Constructs the importer directly from a file path.
     * @param filePath Path to the JSON file.
     * @throws IOException if file reading fails.
     * @throws ParseException if JSON is invalid.
     */
    public JSONFileImporter(String filePath) throws IOException, ParseException {
        parseFile(filePath);
    }

    /**
     * Parses the JSON file into a JSONArray for later processing.
     * @param filePath The path of the JSON file to parse.
     * @throws IOException if reading fails.
     * @throws ParseException if JSON parsing fails.
     */
    private void parseFile(String filePath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            this.jsonArray = (JSONArray) jsonObject.get("car_inventory");
        }
    }

    /**
     * Processes all vehicles in the parsed JSON array.
     * Creates or updates corresponding dealers and adds vehicles to the catalog.
     * @return true if at least one vehicle was processed; false otherwise.
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
        return true;
    }

    /**
     * Extracts dealer and vehicle information from the JSON object and adds them to the catalog.
     * @param jsonObject JSON object representing a vehicle record.
     */
    private void createVehicle(JSONObject jsonObject) {
        // Read required fields from JSON
        String manufacturer = jsonObject.get(VEHICLE_MANUFACTURER_KEY).toString();
        String model = jsonObject.get(VEHICLE_MODEL_KEY).toString();
        String id = jsonObject.get(VEHICLE_ID_KEY).toString();
        double price = Double.parseDouble(jsonObject.get(PRICE_KEY).toString());
        String dealerId = jsonObject.get(DEALER_ID_KEY).toString();

        // Optional fields with null-checks
        String dealerName = jsonObject.containsKey(DEALER_NAME_KEY)
                ? jsonObject.get(DEALER_NAME_KEY).toString()
                : null;

        Boolean acquisitionStatus = jsonObject.containsKey(ACQUISITION_STATUS_KEY)
                ? Boolean.parseBoolean(jsonObject.get(ACQUISITION_STATUS_KEY).toString())
                : null;

        boolean isRentedOut = jsonObject.containsKey(IS_RENTED_KEY)
                && Boolean.parseBoolean(jsonObject.get(IS_RENTED_KEY).toString());

        long epochMillis = Long.parseLong(jsonObject.get(ACQUISITION_DATE_KEY).toString());
        LocalDateTime acquisitionDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                TimeZone.getDefault().toZoneId()
        );

        // Create or update dealer
        Dealer dealer = DealerCatalog.getInstance().getDealerWithId(dealerId);
        if (dealer == null) {
            dealer = new Dealer(dealerId, dealerName, acquisitionStatus);
            DealerCatalog.getInstance().addDealer(dealer);
        } else {
            if (dealerName != null) {
                dealer.setName(dealerName);
            }
            if (acquisitionStatus != null) {
                if (acquisitionStatus) {
                    dealer.enableVehicleAcquisition();
                } else {
                    dealer.disableVehicleAcquisition();
                }
            }
        }

        // Build and add vehicle
        Vehicle vehicle = VehicleBuilder.buildVehicleFromJSON(jsonObject);
        if (vehicle != null) {
            vehicle.setRentedOut(isRentedOut);
            DealerCatalog.getInstance().addVehicleFromAutosave(vehicle);
        }
    }
}
