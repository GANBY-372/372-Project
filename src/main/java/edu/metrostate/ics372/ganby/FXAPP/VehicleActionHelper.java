package edu.metrostate.ics372.ganby.FXAPP;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 * Utility class to perform various vehicle-related operations such as delete, modify price,
 * toggle rent status, and toggle selection in the vehicle table.
 */
public class VehicleActionHelper {

    /**
     * Deletes the selected vehicle from both UI and dealer catalog.
     *
     * @param vehicleTable the vehicle table from which to delete
     * @param dealerTable the dealer table for refreshing
     * @param vehicleObservableList the observable list backing the vehicle table
     */
    public static void deleteSelectedVehicle(TableView<Vehicle> vehicleTable,
                                             TableView<Dealer> dealerTable,
                                             ObservableList<Vehicle> vehicleObservableList) {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert(AlertType.WARNING, "No Vehicle Selected", "Please select a vehicle to delete.");
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Vehicle Deletion");
        confirm.setContentText("Are you sure you want to delete vehicle ID: " + selectedVehicle.getVehicleId() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Dealer dealer = DealerCatalog.getInstance().getDealerWithId(selectedVehicle.getDealerId());
                if (dealer != null) {
                    dealer.getVehicleCatalog().remove(selectedVehicle.getVehicleId());
                    vehicleObservableList.remove(selectedVehicle);
                    vehicleTable.refresh();
                    dealerTable.refresh();
                    showAlert(AlertType.INFORMATION, "Deleted", "Vehicle deleted successfully.");
                }
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
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert(Alert.AlertType.WARNING, "No Vehicle Selected", "Please select a vehicle to change rent status.");
            return;
        }

        // Attempt to toggle rent status
        boolean newStatus = !selectedVehicle.getIsRentedOut();
        String resultType = selectedVehicle.
                setRentedOut(newStatus).trim().replaceAll("\\s+", "").toUpperCase();

        // If SportsCar, show warning and skip change
        if ("SPORTSCAR".equals(resultType)) {
            showAlert(Alert.AlertType.WARNING, "Not Allowed", "SportsCars cannot be rented.");
            return;
        }

        // Refresh UI and update button label
        vehicleTable.refresh();
        toggleButton.setText(selectedVehicle.getIsRentedOut() ? "Set as Available" : "Set as Rented Out");
    }

    /**
     * Selects or unselects all vehicles in the table.
     *
     * @param vehicleTable the vehicle table to toggle selection
     */
    public static void toggleSelectAllVehicles(TableView<Vehicle> vehicleTable) {
        boolean allSelected = vehicleTable.getItems().stream().allMatch(Vehicle::isSelected);
        for (Vehicle vehicle : vehicleTable.getItems()) {
            vehicle.setSelected(!allSelected);
        }
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
