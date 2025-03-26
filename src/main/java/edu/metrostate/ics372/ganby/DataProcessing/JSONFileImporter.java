/**
 * JSONFileImporter.java
 * This class is for importing JSON files.
 * It opens a file explorer to select a file.
 */

package edu.metrostate.ics372.ganby.DataProcessing;

import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.SUV;
import edu.metrostate.ics372.ganby.vehicle.SportsCar;
import edu.metrostate.ics372.ganby.vehicle.Sedan;
import edu.metrostate.ics372.ganby.vehicle.Pickup;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.stage.FileChooser;
import java.awt.*;
import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class JSONFileImporter {

    // JSON key name constants
    public static final String DEALER_ID_KEY =              "dealership_id";
    public static final String VEHICLE_ID_KEY =             "vehicle_id";
    public static final String VEHICLE_MANUFACTURER_KEY =   "vehicle_manufacturer";
    public static final String VEHICLE_MODEL_KEY =          "vehicle_model";
    public static final String VEHICLE_TYPE_KEY =           "vehicle_type";
    public static final String PRICE_KEY =                  "price";
    public static final String ACQUISITION_DATE_KEY =       "acquisition_date";

    // JSON file reader and parser tools
    private Reader reader;
    private JSONParser jsonParser;

    // JSON object and array buckets
    JSONObject  jsonObject;
    JSONArray   jsonArray;

    // TODO: Decouple file chooser and constructor so we can use this class to load a state on boot
    /**
     * Constructor and file chooser and Importer method in one.
     * @throws FileNotFoundException
     */
    public JSONFileImporter(Stage primaryStage) throws FileNotFoundException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a JSON File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json")
            );

            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                this.reader = new FileReader(selectedFile);
                jsonParser = new JSONParser();

                this.jsonObject = (JSONObject) jsonParser.parse(reader);
                jsonArray = (JSONArray) jsonObject.get("car_inventory");

                System.out.println("Successfully parsed JSON file: " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("File selection cancelled.");
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: Rename this method, it looks like a basic getter, but it seems to create a dealer for the object
    // TODO: THis method throws exception for non numeric - we decided id should be a string so need to edit
    /**
     * Creates a dealer from a JSON object using the dealer ID associated with the object.
     * @param jsonObject JSON object containing the dealer ID
     * @return Dealer object
     * @throws IllegalArgumentException if the dealer ID is not numeric
     */
    private Dealer getDealer (JSONObject jsonObject) {
        String numericString  = jsonObject.get(DEALER_ID_KEY).toString();
        if (numericString == null || numericString.isBlank())
            throw new IllegalArgumentException("Value for dealer id is not numeric. Cannot create dealer.");
        return new Dealer(numericString);
    }

    /**
     * Creates a vehicle from a JSON object.
     * @param jsonObject JSON object containing vehicle data
     * @return Vehicle object
     * @throws IllegalAccessException if null object or empty object
     */
    public Vehicle createVehicle(JSONObject jsonObject) throws IllegalAccessException {
        if (jsonObject == null || jsonObject.isEmpty()){
            throw new IllegalArgumentException("JSON vehicle object is null or empty");
        }

        // Create the vehicle with JSON data
        String manufacturer =           jsonObject.get(VEHICLE_MANUFACTURER_KEY).toString();
        String model =                  jsonObject.get(VEHICLE_MODEL_KEY).toString();
        String id =                     jsonObject.get(VEHICLE_ID_KEY).toString();
        double price =                  Double.parseDouble(jsonObject.get(PRICE_KEY).toString());
        String dealerId =               jsonObject.get(DEALER_ID_KEY).toString();
        long epochSeconds =             Long.parseLong(jsonObject.get(ACQUISITION_DATE_KEY).toString());
        LocalDateTime acquisitionDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochSeconds),
                TimeZone.getDefault().toZoneId()
        );

        // Get vehicle type from JSON object
        String type = (String) jsonObject.get(VEHICLE_TYPE_KEY);

        if (type == null || type.isBlank()) {
            System.out.println("Skipping vehicle due to missing or empty vehicle type.");
            return null; // Skip this vehicle if the type is missing
        }

        // Create vehicle, category is an enum
        Vehicle vehicle;
        switch (type.trim().replaceAll("\\s+", "").toUpperCase()) {
            case "SUV" ->         vehicle = new SUV(id, model, manufacturer, price, dealerId, acquisitionDate);
            case "SEDAN" ->       vehicle = new Sedan(id, model, manufacturer, price, dealerId, acquisitionDate);
            case "PICKUP" ->      vehicle = new Pickup(id, model, manufacturer, price, dealerId, acquisitionDate);
            case "SPORTSCAR" ->  vehicle = new SportsCar(id, model, manufacturer, price, dealerId, acquisitionDate);
            default -> {
                System.out.println("Unknown category: " + type);
                return null;
            }
        }

        //vehicles.addVehicle(vehicle);     //Why are we adding to vehicles class and then to VehicleCatalog??

        // Add vehicle to catalog
        DealerCatalog.getInstance().addVehicle(vehicle);
        return vehicle;
    }

    /**
     * Processes the JSON array of objects. This adds JSON file list of vehicles to the catalog.
     * @throws IllegalAccessException if null or empty JSON array
     */
    public void processJSON() throws IllegalAccessException {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return;
        }

        for (Object object : jsonArray) {
            Vehicle vehicle = createVehicle((JSONObject) object);
        }
    }

    /* No usages.
    public void printDealerKeys () {
        for (Object object : jsonArray) {
            String dealerId = ((JSONObject) object).get(DEALER_ID_KEY).toString();
        }
    }
     */
}