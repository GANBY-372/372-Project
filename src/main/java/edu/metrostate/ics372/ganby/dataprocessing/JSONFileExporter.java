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

public class JSONFileExporter {

    public boolean exportDealers(Stage stage, List<Dealer> selectedDealers) {
        File file = FileSelector.chooseJsonSaveFile(stage);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                JSONObject json = convertDealersToJSON(selectedDealers);
                writer.write(prettyPrintJson(json.toJSONString()));
                showSuccessAlert(file.getAbsolutePath());
            } catch (IOException e) {
                showErrorAlert(e.getMessage());
            }
        }
        return false;
    }


    private JSONObject convertDealersToJSON(List<Dealer> dealers) {
        JSONObject rootJson = new JSONObject();
        JSONArray carInventoryJson = new JSONArray();

        for (Dealer dealer : dealers) {
            for (Vehicle vehicle : dealer.vehicleCatalog.values()) {
                JSONObject vehicleJson = new JSONObject();
                vehicleJson.put("dealership_id", dealer.getId());
                vehicleJson.put("vehicle_type", vehicle.getType());
                vehicleJson.put("vehicle_manufacturer", vehicle.getManufacturer());
                vehicleJson.put("vehicle_model", vehicle.getModel());
                vehicleJson.put("vehicle_id", vehicle.getVehicleId());
                vehicleJson.put("price", vehicle.getPrice());
                vehicleJson.put("is_rented_out", vehicle.getIsRentedOut());

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

    private void showSuccessAlert(String path) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Successful");
        alert.setHeaderText(null);
        alert.setContentText("Exported dealers to:\n" + path);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Export Failed");
        alert.setHeaderText("Error exporting dealers");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void saveStateToFile(String filePath) {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
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
            for (Vehicle vehicle : dealer.vehicleCatalog.values()) {
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
                vehicleJson.put("is_rented_out", vehicle.getIsRentedOut()); //added isRented

                carInventoryJson.add(vehicleJson);
            }
        }
        rootJson.put("car_inventory", carInventoryJson);
        return rootJson;
    }
}
