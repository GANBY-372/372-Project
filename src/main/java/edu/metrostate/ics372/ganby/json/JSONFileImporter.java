package edu.metrostate.ics372.ganby.json;

import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.catalog.VehicleCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.Dealers;
import edu.metrostate.ics372.ganby.vehicle.*;
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

    private Dealers dealers;
    private Vehicles vehicles;

    private Dealer dealer;
    private Vehicle vehicle;



    public JSONFileImporter() throws FileNotFoundException {
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
        dealers.addDealer(dealer);
        DealerCatalog.getInstance().getDealers().addDealer(dealer);

        String manufacturer = jsonObject.get(VEHICLE_MANUFACTURER_KEY).toString();
        String model = jsonObject.get(VEHICLE_MODEL_KEY).toString();
        String id = jsonObject.get(VEHICLE_ID_KEY).toString();
        double price = Double.parseDouble(jsonObject.get(PRICE_KEY).toString());
        long epochSeconds = Long.parseLong(jsonObject.get(ACQUISITION_DATE_KEY).toString());
        LocalDateTime acquisitionDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochSeconds),
            TimeZone.getDefault().toZoneId()
        );

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
        vehicles.addVehicle(vehicle);
        VehicleCatalog.getInstance().getVehicles().addVehicle(vehicle);
        return vehicle;
    }

    public Dealers getDealers () {
        return dealers;
    }

    public Vehicles getVehicles () {
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