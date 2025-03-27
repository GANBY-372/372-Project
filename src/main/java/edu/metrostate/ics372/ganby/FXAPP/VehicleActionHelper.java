package edu.metrostate.ics372.ganby.FXAPP;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

/**
 * Utility class to perform various vehicle-related operations such as delete, modify price,
 * toggle rent status, and toggle selection in the vehicle table.
 */
public class VehicleActionHelper {

    /**
     * Deletes all checkbox-selected vehicles from both UI and dealer catalog.
     *
     * @param vehicleTable           the vehicle table
     * @param dealerTable            the dealer table for refresh
     * @param vehicleObservableList  the observable list backing the vehicle table
     */
    public static void deleteSelectedVehicles(TableView<Vehicle> vehicleTable,
                                              TableView<Dealer> dealerTable,
                                              ObservableList<Vehicle> vehicleObservableList) {
        List<Vehicle> toDelete = vehicleTable.getItems().stream()
                .filter(Vehicle::isSelected)
                .toList();

        if (toDelete.isEmpty()) {
            showAlert(AlertType.WARNING, "No Vehicles Selected", "Please select vehicle(s) using the checkbox to delete.");
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete " + toDelete.size() + " vehicle(s)?");
        confirm.setContentText("Are you sure you want to permanently delete the selected vehicles?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                for (Vehicle vehicle : toDelete) {
                    Dealer dealer = DealerCatalog.getInstance().getDealerWithId(vehicle.getDealerId());
                    if (dealer != null) {
                        dealer.getVehicleCatalog().remove(vehicle.getVehicleId());
                    }
                    vehicleObservableList.remove(vehicle);
                }

                vehicleTable.refresh();
                dealerTable.refresh();
                showAlert(AlertType.INFORMATION, "Deleted", "Selected vehicles deleted successfully.");
            }
        });
    }

    /**
     * Modifies the price of the selected vehicle.
     *
     * @param vehicleTable the vehicle table with selection
     */
    public static void modifyVehiclePrice(TableView<Vehicle> vehicleTable) {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert(AlertType.WARNING, "No Vehicle Selected", "Please select a vehicle to modify.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedVehicle.getPrice()));
        dialog.setTitle("Modify Price");
        dialog.setHeaderText("Update vehicle price");
        dialog.setContentText("New price:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                double newPrice = Double.parseDouble(input);
                if (newPrice < 0) throw new NumberFormatException("Price must be non-negative.");
                selectedVehicle.setPrice(newPrice);
                vehicleTable.refresh();
                showAlert(AlertType.INFORMATION, "Updated", "Price updated to $" + newPrice);
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid price.");
            }
        });
    }

    /**
     * Toggles the rent status of the selected vehicle.
     * Prevents rent status changes for SportsCars and displays a warning.
     *
     * @param vehicleTable the TableView displaying vehicles
     * @param toggleButton the toggle button whose label should reflect current status
     */
    public static void toggleRentStatus(TableView<Vehicle> vehicleTable, Button toggleButton) {
        List<Vehicle> selectedVehicles = vehicleTable.getItems().stream()
                .filter(Vehicle::isSelected)
                .toList();

        if (selectedVehicles.isEmpty()) {
            showAlert(AlertType.WARNING, "No Vehicle Selected", "Please select vehicle(s) to change rent status.");
            return;
        }

        for (Vehicle vehicle : selectedVehicles) {
            String result = vehicle.setRentedOut(!vehicle.getIsRentedOut());

            // If it's a SportsCar, it should not be toggled
            if (result.trim().replaceAll("\\s+", "").toUpperCase().equals("SPORTSCAR")) {
                showAlert(AlertType.WARNING, "Action Not Allowed For Vehicle Id #" + vehicle.getVehicleId(),
                        "SportsCars cannot be rented.");
            }
        }

        vehicleTable.refresh();

        // Optional: update button label based on the first selected vehicle
        Vehicle first = selectedVehicles.get(0);
        toggleButton.setText(first.getIsRentedOut() ? "Set as Available" : "Set as Rented");
    }

    /**
     * Selects or unselects all vehicles in the table.
     *
     * @param vehicleTable the vehicle table to toggle selection
     */
    public static void toggleSelectAllVehicles(TableView<Vehicle> vehicleTable) {
        boolean allSelected = vehicleTable.getItems().stream().allMatch(Vehicle::isSelected);

        // Toggle each vehicle's checkbox value
        for (Vehicle vehicle : vehicleTable.getItems()) {
            vehicle.setSelected(!allSelected);
        }

        // Force refresh of the TableView to reflect changes
        vehicleTable.refresh();
    }

    /**
     * Shows a popup alert with given parameters.
     *
     * @param alertType the alert type (e.g., WARNING, ERROR)
     * @param title the alert dialog title
     * @param message the content of the alert
     */
    private static void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
