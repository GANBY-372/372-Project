package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.util.List;

/**
 * Utility class for exporting dealer and vehicle data to a JSON file.
 * Supports exporting selected dealers interactively and saving the entire catalog programmatically.
 */
public class JSONFileExporter {

    /**
     * Launches a FileChooser dialog to save a JSON export of selected dealers and their vehicles.
     *
     * @param stage           the parent JavaFX stage
     * @param selectedDealers the dealers selected for export
     * @return true if export was successful, false otherwise
     */
    public boolean exportDealers(Stage stage, List<Dealer> selectedDealers) {
        File file = FileSelector.chooseJsonSaveFile(stage);
        if (file == null) {
            return false; // User cancelled
        }

        try (FileWriter writer = new FileWriter(file)) {
            JSONObject json = convertDealersToJSON(selectedDealers);
            writer.write(prettyPrintJson(json.toJSONString()));
            showSuccessAlert(file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            showErrorAlert(e.getMessage());
            return false;
        }
    }

    /**
     * Converts a list of dealers and their vehicle inventories into a JSON object.
     *
     * @param dealers the list of dealers to include in the JSON
     * @return a JSONObject representing the serialized data
     */
    private JSONObject convertDealersToJSON(List<Dealer> dealers) {
        JSONObject rootJson = new JSONObject();
        JSONArray carInventoryJson = new JSONArray();

        for (Dealer dealer : dealers) {
            for (Vehicle vehicle : dealer.vehicleCatalog.values()) {
                JSONObject vehicleJson = new JSONObject();
                vehicleJson.put("dealership_id", dealer.getId());
                vehicleJson.put("dealer_name", dealer.getName());
                vehicleJson.put("acquisition_status", dealer.isVehicleAcquisitionEnabled());
                vehicleJson.put("vehicle_type", vehicle.getType());
                vehicleJson.put("vehicle_manufacturer", vehicle.getManufacturer());
                vehicleJson.put("vehicle_model", vehicle.getModel());
                vehicleJson.put("vehicle_id", vehicle.getVehicleId());
                vehicleJson.put("price", vehicle.getPrice());
                vehicleJson.put("is_rented_out", vehicle.getIsRentedOut());

                long epochMillis = vehicle.getAcquisitionDate()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
                vehicleJson.put("acquisition_date", epochMillis);

                carInventoryJson.add(vehicleJson);
            }
        }

        rootJson.put("car_inventory", carInventoryJson);
        return rootJson;
    }


    /**
     * Pretty-prints raw JSON string with indentation and spacing.
     *
     * @param json the compact JSON string
     * @return formatted JSON string for readability
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
     * Displays a success alert dialog with the export path.
     *
     * @param path the file path where export was saved
     */
    private void showSuccessAlert(String path) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Successful");
        alert.setHeaderText(null);
        alert.setContentText("Exported dealers to:\n" + path);
        alert.showAndWait();
    }

    /**
     * Displays an error alert dialog with the provided message.
     *
     * @param message the error message to show
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Export Failed");
        alert.setHeaderText("Error exporting dealers");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
