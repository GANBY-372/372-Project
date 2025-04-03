package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.util.List;

public class DataExporter {

    /**
     * Export dealer data to JSON file
     *
     * @param dealer   Dealer
     * @param filePath String
     */
    public void exportToFile(Dealer dealer, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            String json = convertDealerToJson(dealer);
            writer.write(json);
            System.out.println("Successfully exported to " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while exporting the dealer data: " + e.getMessage());
        }
    }

    /**
     * Convert dealer to JSON string
     *
     * @param dealer Dealer
     * @return String
     */
    private String convertDealerToJson(Dealer dealer) {
        JSONObject rootJson = new JSONObject();
        JSONArray carInventoryJson = new JSONArray();

        for (Vehicle vehicle : dealer.getVehicleCatalog().values()) {
            JSONObject vehicleJson = new JSONObject();
            vehicleJson.put("dealership_id", dealer.getId());
            vehicleJson.put("vehicle_type", vehicle.getType());
            vehicleJson.put("vehicle_manufacturer", vehicle.getManufacturer());
            vehicleJson.put("vehicle_model", vehicle.getModel());
            vehicleJson.put("vehicle_id", vehicle.getVehicleId());
            vehicleJson.put("price", vehicle.getPrice());
            ZoneId zoneId = ZoneId.of("America/Chicago");
            long epochMillis = vehicle.getAcquisitionDate().atZone(zoneId).toInstant().toEpochMilli();
            vehicleJson.put("acquisition_date", epochMillis);

            carInventoryJson.add(vehicleJson);
        }

        rootJson.put("car_inventory", carInventoryJson);

        // Print the JSON output
        return prettyPrintJson(rootJson.toJSONString());
    }

    /**
     * Pretty print JSON string
     *
     * @param json String
     * @return String
     */
    private String prettyPrintJson(String json) {
        StringBuilder formattedJson = new StringBuilder();
        int indentLevel = 0;
        boolean inQuotes = false;

        for (char c : json.toCharArray()) {
            switch (c) {
                case '{':
                case '[':
                    formattedJson.append(c);
                    if (!inQuotes) {
                        formattedJson.append("\n").append("  ".repeat(++indentLevel));
                    }
                    break;
                case '}':
                case ']':
                    if (!inQuotes) {
                        formattedJson.append("\n").append("  ".repeat(--indentLevel));
                    }
                    formattedJson.append(c);
                    break;
                case ',':
                    formattedJson.append(c);
                    if (!inQuotes) {
                        formattedJson.append("\n").append("  ".repeat(indentLevel));
                    }
                    break;
                case ':':
                    formattedJson.append(c).append(" ");
                    break;
                case '"':
                    formattedJson.append(c);
                    inQuotes = !inQuotes;
                    break;
                default:
                    formattedJson.append(c);
                    break;
            }
        }

        return formattedJson.toString();
    }

    /**
     * Convert entire dealer catalog to single flattened JSON file of vehicles
     *
     * @return JSONObject
     */
    public JSONObject convertDealerCatalogToJSON() {
        JSONObject rootJson = new JSONObject();
        JSONArray carInventoryJson = new JSONArray();

        for (Dealer dealer : DealerCatalog.getInstance().getDealers()) {
            for (Vehicle vehicle : dealer.getVehicleCatalog().values()) {
                JSONObject vehicleJson = new JSONObject();
                vehicleJson.put("dealership_id", dealer.getId());
                vehicleJson.put("vehicle_type", vehicle.getType());
                vehicleJson.put("vehicle_manufacturer", vehicle.getManufacturer());
                vehicleJson.put("vehicle_model", vehicle.getModel());
                vehicleJson.put("vehicle_id", vehicle.getVehicleId());
                vehicleJson.put("price", vehicle.getPrice());
                ZoneId zoneId = ZoneId.of("America/Chicago");
                long epochMillis = vehicle.getAcquisitionDate().atZone(zoneId).toInstant().toEpochMilli();
                vehicleJson.put("acquisition_date", epochMillis);

                carInventoryJson.add(vehicleJson);
            }
        }

        rootJson.put("car_inventory", carInventoryJson);
        return rootJson;
    }

    // TODO: Write the method

    /**
     * Save the current state of the dealer catalog (all dealers and vehicles)
     * to a JSON file at the specified path.
     *
     * @param filePath the full file path to save to
     */
    public void saveStateToFile(String filePath) {
        try {
            // Make sure the directory exists
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); // create the folder if it doesn't exist
            }

            try (FileWriter writer = new FileWriter(file)) {
                JSONObject fullCatalogJson = convertDealerCatalogToJSON();
                writer.write(prettyPrintJson(fullCatalogJson.toJSONString()));
                System.out.println("Dealer catalog successfully saved to " + filePath);
            }
        } catch (IOException e) {
            System.out.println("Error saving dealer catalog: " + filePath + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Prompts the user to export selected dealers to a single XML file.
     *
     * @param stage           the JavaFX window used for showing the save dialog
     * @param selectedDealers the list of dealers to export
     */
    public void promptAndExportDealersAsJSON(Stage stage, List<Dealer> selectedDealers) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Selected Dealers");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setInitialFileName("selected_dealers.json");

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                JSONObject json = convertDealersToJSON(selectedDealers);
                writer.write(prettyPrintJson(json.toJSONString()));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export Successful");
                alert.setHeaderText(null);
                alert.setContentText("Selected dealers exported to:\n" + file.getAbsolutePath());
                alert.showAndWait();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Export Failed");
                alert.setHeaderText("Could not export selected dealers");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }


    /**
     * Prompts the user to export selected dealers to a single XML file.
     *
     * @param stage           the JavaFX window used for showing the save dialog
     * @param selectedDealers the list of dealers to export
     */
    public void promptAndExportDealersAsXML(Stage stage, List<Dealer> selectedDealers) {
        try {
            XMLFileExporter xmlFileExporter = new XMLFileExporter();
            xmlFileExporter.exportDealers(stage, selectedDealers); // Handles file dialog, XML creation, and writing
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Failed");
            alert.setHeaderText("Could not export selected dealers");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }


    private JSONObject convertDealersToJSON(List<Dealer> dealers) {
        JSONObject rootJson = new JSONObject();
        JSONArray carInventoryJson = new JSONArray();

        for (Dealer dealer : dealers) {
            for (Vehicle vehicle : dealer.getVehicleCatalog().values()) {
                JSONObject vehicleJson = new JSONObject();
                vehicleJson.put("dealership_id", dealer.getId());
                vehicleJson.put("vehicle_type", vehicle.getType());
                vehicleJson.put("vehicle_manufacturer", vehicle.getManufacturer());
                vehicleJson.put("vehicle_model", vehicle.getModel());
                vehicleJson.put("vehicle_id", vehicle.getVehicleId());
                vehicleJson.put("price", vehicle.getPrice());
                ZoneId zoneId = ZoneId.of("America/Chicago");
                long epochMillis = vehicle.getAcquisitionDate().atZone(zoneId).toInstant().toEpochMilli();
                vehicleJson.put("acquisition_date", epochMillis);

                carInventoryJson.add(vehicleJson);
            }
        }

        rootJson.put("car_inventory", carInventoryJson);
        return rootJson;
    }


}
