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

public class JSONFileImporter {

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

    public JSONFileImporter(Stage primaryStage) throws IOException, ParseException {
        File selectedFile = FileSelector.chooseJsonOpenFile(primaryStage);
        if (selectedFile != null) {
            parseFile(selectedFile.getAbsolutePath());
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    public JSONFileImporter(String filePath) throws IOException, ParseException {
        parseFile(filePath);
    }

    private void parseFile(String filePath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            this.jsonArray = (JSONArray) jsonObject.get("car_inventory");
        }
    }

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

    private Vehicle createVehicle(JSONObject jsonObject) {
        String manufacturer = jsonObject.get(VEHICLE_MANUFACTURER_KEY).toString();
        String model = jsonObject.get(VEHICLE_MODEL_KEY).toString();
        String id = jsonObject.get(VEHICLE_ID_KEY).toString();
        double price = Double.parseDouble(jsonObject.get(PRICE_KEY).toString());
        String dealerId = jsonObject.get(DEALER_ID_KEY).toString();
        long epochMillis = Long.parseLong(jsonObject.get(ACQUISITION_DATE_KEY).toString());


        Object rentedOutObj = jsonObject.get(IS_RENTED_KEY);
        boolean isRentedOut = false;
        if (rentedOutObj!= null) {
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
}
