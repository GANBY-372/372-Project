/**
 * JSONFileImporter.java
 * This class is for importing JSON files.
 * It opens a file explorer to select a file.
 */

package edu.metrostate.ics372.ganby.json;
import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
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

public class JSONImport {

    public static final String DEALER_ID_KEY =              "dealership_id";
    public static final String VEHICLE_ID_KEY =             "vehicle_id";
    public static final String VEHICLE_MANUFACTURER_KEY =   "vehicle_manufacturer";
    public static final String VEHICLE_MODEL_KEY =          "vehicle_model";
    public static final String VEHICLE_TYPE_KEY =           "vehicle_type";
    public static final String PRICE_KEY =                  "price";
    public static final String ACQUISITION_DATE_KEY =       "acquisition_date";

    // JSON object and array buckets
    JSONObject  jsonObject;
    JSONArray   jsonArray;

    /**
     * Import JSON file to populate the catalog
     * @throws FileNotFoundException if file not found
     */
    public void importFile(File selectedFile) throws IOException, ParseException {
        try {
            Reader reader = new FileReader(selectedFile);

            JSONParser jsonParser = new JSONParser();

            // Parse the JSON file and get the JSON array of objects
            this.jsonObject = (JSONObject) jsonParser.parse(reader);
            jsonArray = (JSONArray) jsonObject.get("car_inventory");

            System.out.println("Successfully parsed JSON file: " + selectedFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}