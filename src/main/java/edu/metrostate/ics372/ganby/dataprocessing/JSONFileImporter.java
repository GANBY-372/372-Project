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

public class JSONFileImporter {

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

    private void createVehicle(JSONObject jsonObject) {
        String manufacturer = jsonObject.get(VEHICLE_MANUFACTURER_KEY).toString();
        String model = jsonObject.get(VEHICLE_MODEL_KEY).toString();
        String id = jsonObject.get(VEHICLE_ID_KEY).toString();
        double price = Double.parseDouble(jsonObject.get(PRICE_KEY).toString());
        String dealerId = jsonObject.get(DEALER_ID_KEY).toString();

        String dealerName = jsonObject.containsKey(DEALER_NAME_KEY)
                ? jsonObject.get(DEALER_NAME_KEY).toString()
                : null;

        Boolean acquisitionStatus = jsonObject.containsKey(ACQUISITION_STATUS_KEY)
                ? Boolean.parseBoolean(jsonObject.get(ACQUISITION_STATUS_KEY).toString())
                : null;

        boolean isRentedOut = jsonObject.containsKey(IS_RENTED_KEY) &&
                Boolean.parseBoolean(jsonObject.get(IS_RENTED_KEY).toString());

        long epochMillis = Long.parseLong(jsonObject.get(ACQUISITION_DATE_KEY).toString());
        LocalDateTime acquisitionDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                TimeZone.getDefault().toZoneId()
        );

        //Create dealer if not exists, using a single line with null-safe fallback handled in Dealer class
        Dealer dealer = DealerCatalog.getInstance().getDealerWithId(dealerId);
        if (dealer == null) {
            dealer = new Dealer(dealerId, dealerName, acquisitionStatus);
            DealerCatalog.getInstance().addDealer(dealer);
        } else { //if dealer already exists, update name and status
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

        // ✅ Create vehicle and apply rental status
        Vehicle vehicle = VehicleBuilder.buildVehicleFromJSON(jsonObject);
        if (vehicle != null) {
            vehicle.setRentedOut(isRentedOut);
            DealerCatalog.getInstance().addVehicleFromAutosave(vehicle);
        }

    }

}
