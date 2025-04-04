package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
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

/**
 * Utility class responsible for exporting dealer and vehicle data to a JSON file.
 * Supports exporting one or more dealers into a single structured JSON document.
 */
public class JSONFileExporter {

    /**
     * Prompts the user with a file dialog to choose a save location,
     * then exports the provided dealers and their vehicles into a JSON file.
     *
     * @param stage           the JavaFX stage to anchor the file dialog
     * @param selectedDealers the list of dealers to export
     */
    public void exportDealers(Stage stage, List<Dealer> selectedDealers) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Dealers as JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setInitialFileName("selected_dealers.json");

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                JSONObject json = convertDealersToJSON(selectedDealers);
                writer.write(prettyPrintJson(json.toJSONString()));
                showSuccessAlert(file.getAbsolutePath());
            } catch (IOException e) {
                showErrorAlert(e.getMessage());
            }
        }
    }

    /**
     * Converts the provided list of dealers and their vehicles into a nested JSON structure.
     *
     * @param dealers the list of dealers to convert
     * @return a JSONObject representing the car inventory
     */
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

                long epochMillis = vehicle.getAcquisitionDate()
                        .atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli();
                vehicleJson.put("acquisition_date", epochMillis);

                carInventoryJson.add(vehicleJson);
            }
        }

        rootJson.put("car_inventory", carInventoryJson);
        return rootJson;
    }

    /**
     * Formats a raw JSON string into indented and readable output.
     *
     * @param json the raw JSON string
     * @return a pretty-printed version of the input JSON
     */
    private static String prettyPrintJson(String json) {
        StringBuilder formatted = new StringBuilder();
        int indent = 0;
        boolean inQuotes = false;

        for (char c : json.toCharArray()) {
            switch (c) {
                case '{', '[' -> {
                    formatted.append(c);
                    if (!inQuotes) formatted.append("\n").append("  ".repeat(++indent));
                }
                case '}', ']' -> {
                    if (!inQuotes) formatted.append("\n").append("  ".repeat(--indent));
                    formatted.append(c);
                }
                case ',' -> {
                    formatted.append(c);
                    if (!inQuotes) formatted.append("\n").append("  ".repeat(indent));
                }
                case ':' -> formatted.append(": ");
                case '"' -> {
                    formatted.append(c);
                    inQuotes = !inQuotes;
                }
                default -> formatted.append(c);
            }
        }

        return formatted.toString();
    }

    /**
     * Displays a confirmation alert when the export operation is successful.
     *
     * @param path the full path to the saved JSON file
     */
    private void showSuccessAlert(String path) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Successful");
        alert.setHeaderText(null);
        alert.setContentText("Exported dealers to:\n" + path);
        alert.showAndWait();
    }

    /**
     * Displays an error alert when the export operation fails.
     *
     * @param message the error message to display
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Export Failed");
        alert.setHeaderText("Error exporting dealers");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void saveStateToFile(String filePath) {
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

    public static JSONObject convertDealerCatalogToJSON() {
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
}