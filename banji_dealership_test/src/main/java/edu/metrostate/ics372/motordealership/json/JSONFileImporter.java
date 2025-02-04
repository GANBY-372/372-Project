package edu.metrostate.ics372.motordealership.json;


import edu.metrostate.ics372.motordealership.dealer.Dealer;
import edu.metrostate.ics372.motordealership.dealer.DealerCatalog;
import edu.metrostate.ics372.motordealership.vehicle.*;
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

    private Reader reader;
    private JSONParser jsonParser;
    JSONObject jsonObject;
    JSONArray jsonArray;

    private Set<Dealer> dealers;
    private Set<Vehicle> vehicles;

    private Dealer dealer;
    private Vehicle vehicle;

    public JSONFileImporter () throws FileNotFoundException {

        this.dealers = new HashSet<>();
        this.vehicles = new HashSet<>();
            try {

                InputStream inputStream = getClass().getResourceAsStream("/inventory.json");

                if (inputStream == null) {
                    throw new IllegalArgumentException("File 'inventory.json' not found in the project's package path");
                }

                this.reader = new InputStreamReader(inputStream);

                jsonParser = new JSONParser();

                this.jsonObject = (JSONObject) jsonParser.parse(reader);
                jsonArray = (JSONArray) jsonObject.get("car_inventory");
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException(e.getMessage());
            } catch (ParseException | IOException e) {
                throw new RuntimeException(e);
            }
        }

    private Dealer getDealer (JSONObject jsonObject) {
        String numericString  = jsonObject.get(DEALER_ID_KEY).toString();
        if (numericString == null || numericString.isBlank())
            throw new IllegalArgumentException("Value for dealer id is not numeric. Cannot create dealer.");
        return new Dealer(Integer.parseInt(numericString));
    }

    public Vehicle createVehicle (JSONObject jsonObject) throws IllegalAccessException {
        if (jsonObject == null || jsonObject.isEmpty())
            throw new IllegalArgumentException("JSON vehicle object is null or empty");

        this.dealer = getDealer(jsonObject);
        dealers.add(dealer);
        DealerCatalog.getInstance().addDealer(dealer);

        String manufacturer = jsonObject.get(VEHICLE_MANUFACTURER_KEY).toString();
        String model = jsonObject.get(VEHICLE_MODEL_KEY).toString();
        String id = jsonObject.get(VEHICLE_ID_KEY).toString();
        double price = Double.parseDouble(jsonObject.get(PRICE_KEY).toString());
        long epochSeconds = Long.parseLong(jsonObject.get(ACQUISITION_DATE_KEY).toString());
        LocalDateTime acquisitionDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochSeconds),
            TimeZone.getDefault().toZoneId()
        );
        ;

        String type = jsonObject.get(VEHICLE_TYPE_KEY).toString();
        if (type == null || type.isBlank())
            throw new IllegalArgumentException("JSON vehicle type is null or empty");

        VehicleCategory category = VehicleCategory.fromString(type);
        if (category == null)
            throw new IllegalArgumentException("JSON vehicle category is null or empty");

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
        vehicles.add(vehicle);
        VehicleCatalog.getInstance().addVehicle(vehicle);
        return vehicle;
    }

    public Set<Dealer> getDealers () {
        return dealers;
    }

    public Set<Vehicle> getVehicles () {
        return vehicles;
    }

    public void processJSON () throws IllegalAccessException {
        for (Object object : jsonArray) {
            Vehicle vehicle = createVehicle((JSONObject) object);
            System.out.println(vehicle.toString());
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
            System.out.println(dealerId);
        }
    }
}