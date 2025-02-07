package edu.metrostate.ics372.GANBY.JSON;

import edu.metrostate.ics372.GANBY.Dealer.*;
import edu.metrostate.ics372.GANBY.Catalogs.*;
import edu.metrostate.ics372.GANBY.Vehicle.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class JSONFileImporter {
    public static final String DEALER_ID_KEY = "dealership_id";
    public static final String VEHICLE_ID_KEY = "vehicle_id";
    public static final String VEHICLE_MANUFACTURER_KEY = "vehicle_manufacturer";
    public static final String VEHICLE_MODEL_KEY = "vehicle_model";
    public static final String VEHICLE_TYPE_KEY = "vehicle_type";
    public static final String PRICE_KEY = "price";
    public static final String ACQUISITION_DATE_KEY = "acquisition_date";

    private JSONArray jsonArray;
    private DealerCatalog dealerCatalog;

    public JSONFileImporter() throws FileNotFoundException {
        dealerCatalog = DealerCatalog.getInstance();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/data.json");
            if (inputStream == null) {
                throw new IllegalArgumentException("File 'inventory.json' not found in the project's package path");
            }
            Reader reader = new InputStreamReader(inputStream);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            jsonArray = (JSONArray) jsonObject.get("car_inventory");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Dealer getOrCreateDealer(int dealerId) {
        return dealerCatalog.getDealerCollection().computeIfAbsent(dealerId, Dealer::new);
    }

    private Vehicle createVehicle(JSONObject jsonObject) {
        int dealerId = Integer.parseInt(jsonObject.get(DEALER_ID_KEY).toString());
        int vehicleId = Integer.parseInt(jsonObject.get(VEHICLE_ID_KEY).toString());
        String manufacturer = jsonObject.get(VEHICLE_MANUFACTURER_KEY).toString();
        String model = jsonObject.get(VEHICLE_MODEL_KEY).toString();
        String type = jsonObject.get(VEHICLE_TYPE_KEY).toString();
        double price = Double.parseDouble(jsonObject.get(PRICE_KEY).toString());
        long epochSeconds = Long.parseLong(jsonObject.get(ACQUISITION_DATE_KEY).toString());
        LocalDateTime acquisitionDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochSeconds), TimeZone.getDefault().toZoneId());

        return new Vehicle(dealerId, vehicleId, type, manufacturer, model, price, acquisitionDate);
    }

    public void processJSON() {
        for (Object object : jsonArray) {
            JSONObject jsonVehicle = (JSONObject) object;
            Vehicle vehicle = createVehicle(jsonVehicle);
            Dealer dealer = getOrCreateDealer(vehicle.getDealerId());
            dealer.addVehicle(vehicle);
            dealerCatalog.addVehicle(vehicle);
            System.out.println(vehicle);
        }
    }

    public void printDealerKeys() {
        for (Object object : jsonArray) {
            String dealerId = ((JSONObject) object).get(DEALER_ID_KEY).toString();
            System.out.println(dealerId);
        }
    }
}
