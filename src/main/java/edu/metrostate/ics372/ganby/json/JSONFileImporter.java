/**
 * JSONFileImporter.java
 * @author B, Y, G
 * This class is for importing JSON files.
 * It opens a file explorer to select a file.
 */

package edu.metrostate.ics372.ganby.json;

import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.catalog.VehicleCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.Dealers;
import edu.metrostate.ics372.ganby.vehicle.*;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class JSONFileImporter {

    // JSON key name constants
    public static final String DEALER_ID_KEY = "dealership_id";
    public static final String VEHICLE_ID_KEY = "vehicle_id";
    public static final String VEHICLE_MANUFACTURER_KEY = "vehicle_manufacturer";
    public static final String VEHICLE_MODEL_KEY = "vehicle_model";
    public static final String VEHICLE_TYPE_KEY = "vehicle_type";
    public static final String PRICE_KEY = "price";
    public static final String ACQUISITION_DATE_KEY = "acquisition_date";

    // JSON file reader and parser tools
    private Reader reader;
    private JSONParser jsonParser;

    // JSON object and array buckets
    JSONObject jsonObject;
    JSONArray jsonArray;

    // Getters
    // Dealers and Vehicles
    @Getter
    private Dealers dealers;
    @Getter
    private Vehicles vehicles;
    private Dealer dealer;
    private Vehicle vehicle;

    // Constructor reads JSON file and initializes JSON parser
    public JSONFileImporter() throws FileNotFoundException {

        // Create collections for dealers and vehicles, these are Sets of respective objects
        this.dealers = new Dealers();
        this.vehicles = new Vehicles();

        //Asked CHATGPT 4.0 to write code to allow user to choose file to import.
        try {
            // File chooser dialog
            Frame frame = new Frame();
            FileDialog fileDialog = new FileDialog(frame, "Select a File", FileDialog.LOAD);
            fileDialog.setVisible(true);

            String directory = fileDialog.getDirectory();
            String filename = fileDialog.getFile();

            if (filename != null) {
                File selectedFile = new File(directory, filename);

                // ✅ Use FileReader instead of InputStream
                // reader reads the selected file
                this.reader = new FileReader(selectedFile);

                jsonParser = new JSONParser();

                // Parse the JSON file and get the JSON array of objects
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


    // Method to create a dealer from a JSON object
    // ***NOTE*** We may want to rename this method to something like createDealer [delete this comment after discussion]
    private Dealer getDealer (JSONObject jsonObject) {
        String numericString  = jsonObject.get(DEALER_ID_KEY).toString();
        if (numericString == null || numericString.isBlank())
            throw new IllegalArgumentException("Value for dealer id is not numeric. Cannot create dealer.");
        return new Dealer(Integer.parseInt(numericString));
    }

    // Create Vehicle from JSON object
    public Vehicle createVehicle (JSONObject jsonObject) throws IllegalAccessException {
        if (jsonObject == null || jsonObject.isEmpty())
            throw new IllegalArgumentException("JSON vehicle object is null or empty");

        // Create a dealer
        this.dealer = getDealer(jsonObject);
        dealers.addDealer(dealer);

        DealerCatalog.getInstance().getDealers().addDealer(dealer);

        // Create the vehicle with JSON data
        String manufacturer = jsonObject.get(VEHICLE_MANUFACTURER_KEY).toString();
        String model = jsonObject.get(VEHICLE_MODEL_KEY).toString();
        String id = jsonObject.get(VEHICLE_ID_KEY).toString();
        double price = Double.parseDouble(jsonObject.get(PRICE_KEY).toString());
        long epochSeconds = Long.parseLong(jsonObject.get(ACQUISITION_DATE_KEY).toString());
        LocalDateTime acquisitionDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochSeconds),
            TimeZone.getDefault().toZoneId()
        );

        // Get vehicle type from JSON object
        String type = jsonObject.get(VEHICLE_TYPE_KEY).toString();
        if (type == null || type.isBlank())
            throw new IllegalArgumentException("JSON vehicle type is null or empty");

        VehicleCategory category = VehicleCategory.fromString(type);
        if (category == null)
            throw new IllegalArgumentException("JSON vehicle category is null or empty");

        // Create vehicle, category is an enum
        if (category.equals(VehicleCategory.SUV)) {
            this.vehicle = new SUV.Builder().dealer(dealer)
                .id(id)
                .manufacturer(manufacturer)
                .model(model)
                .price(price)
                .acquisitionDate(acquisitionDate)
                .build();
        } else if (category.equals(VehicleCategory.PICKUP)) {
            this.vehicle = new Sedan.Builder().dealer(dealer)
                .id(id)
                .manufacturer(manufacturer)
                .model(model)
                .price(price)
                .acquisitionDate(acquisitionDate)
                .build();
    } else if (category.equals(VehicleCategory.SEDAN)) {
            this.vehicle = new Sedan.Builder().dealer(dealer)
                .id(id)
                .manufacturer(manufacturer)
                .model(model)
                .price(price)
                .acquisitionDate(acquisitionDate)
                .build();
        } else if (category.equals(VehicleCategory.SPORTS_CAR)) {
            this.vehicle = new SportsCar.Builder().dealer(dealer)
                .id(id)
                .manufacturer(manufacturer)
                .model(model)
                .price(price)
                .acquisitionDate(acquisitionDate)
                .build();
        }
        else { throw new IllegalArgumentException("Unknown category: " + category); }
        vehicles.addVehicle(vehicle);

        // Add vehicle to catalog
        VehicleCatalog.getInstance().getVehicles().addVehicle(vehicle);
        return vehicle;
    }

    // Process JSON file
    public void processJSON() throws IllegalAccessException {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return;
        }

        for (Object object : jsonArray) {
            Vehicle vehicle = createVehicle((JSONObject) object);

            //prints the vehicle, but it's commented out for clean console output
            //System.out.println(vehicle.toString());
        }
    }


//    public List<Dealer> getDealers () {
//        List<Dealer> dealers = new ArrayList<>();
//        for (Object object : jsonArray) {
//            Dealer dealer = getDealer((JSONObject) object);
//            if (!dealers.contains(dealer)) {
//                System.out.println("Added dealer " + dealer.toString());
//                dealers.add(dealer);
//            } else { System.out.println(dealer.toString() + " already added"); }
//        }
//        return dealers;
//    }

    public void printDealerKeys () {
        for (Object object : jsonArray) {
            String dealerId = ((JSONObject) object).get(DEALER_ID_KEY).toString();
            //ystem.out.println(dealerId);
        }
    }
}