package edu.metrostate.ics372.ganby.FXAPP;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            FXController.showAlert(AlertType.WARNING, "No Vehicles Selected", "Please select vehicle(s) using the checkbox to delete.");
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
                FXController.showAlert(AlertType.INFORMATION, "Deleted", "Selected vehicles deleted successfully.");
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
            FXController.showAlert(AlertType.WARNING, "No Vehicle Selected", "Please select a vehicle to modify.");
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
                FXController.showAlert(AlertType.INFORMATION, "Updated", "Price updated to $" + newPrice);
            } catch (NumberFormatException e) {
                FXController.showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid price.");
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
            FXController.showAlert(AlertType.WARNING, "No Vehicle Selected", "Please select vehicle(s) to change rent status.");
            return;
        }

        boolean allRentedOut = true;
        boolean hasNonSportsCar = false;

        // Determine if all non-sportscars are rented out
        for (Vehicle vehicle : selectedVehicles) {
            String type = vehicle.getType().trim().replaceAll("\\s+", "");
            boolean isSportsCar = type.equalsIgnoreCase("SportsCar");

            if (!isSportsCar && !vehicle.getIsRentedOut()) {
                allRentedOut = false;
            }

            if (!isSportsCar) {
                hasNonSportsCar = true;
            }
        }

        // Determine the new status we want to set
        boolean newRentStatus = !allRentedOut; // true = rent out, false = make available

        if (!hasNonSportsCar && newRentStatus) {
            FXController.showAlert(AlertType.WARNING, "Action Not Allowed",
                    "All selected vehicles are SportsCars, which cannot be rented.");
            return;
        }

        // Update rent status
        for (Vehicle vehicle : selectedVehicles) {
            String type = vehicle.getType().trim().replaceAll("\\s+", "");
            boolean isSportsCar = type.equalsIgnoreCase("SportsCar");

            if (isSportsCar && newRentStatus) {
                // Only show warning if we're trying to rent out a SportsCar
                FXController.showAlert(AlertType.WARNING,
                        "Action Not Allowed For Vehicle Id #" + vehicle.getVehicleId(),
                        "SportsCars cannot be rented.");
                continue;
            }

            vehicle.setRentedOut(newRentStatus);
        }

        vehicleTable.refresh();
        toggleButton.setText(newRentStatus ? "Set All as Available" : "Set All as Rented");
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

    /*
     * Opens a modal wizard to add a new vehicle to the selected dealer.
     * Handles all input fields, validation, and updates the dealer's vehicle catalog.
     *
     * @param selectedDealer        the dealer to add the vehicle to
     * @param vehicleObservableList the observable list to update the vehicle table
     */
    /**
     * Opens a modal wizard to add a new vehicle to the selected dealer.
     *
     * @param selectedDealer the dealer to add the vehicle to
     * @param vehicleObservableList the observable list to refresh the vehicle table
     */
    public static void openAddVehicleWizard(Dealer selectedDealer, ObservableList<Vehicle> vehicleObservableList) {
        if (selectedDealer == null) {
            FXController.showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to add a vehicle.");
            return;
        }

        Stage wizardStage = new Stage();
        wizardStage.initModality(Modality.APPLICATION_MODAL);
        wizardStage.setTitle("Add New Vehicle Wizard");

        // Create form labels and input fields
        Label idLabel = new Label("Vehicle ID:");
        TextField idField = new TextField();

        Label manufacturerLabel = new Label("Manufacturer:");
        TextField manufacturerField = new TextField();

        Label modelLabel = new Label("Model:");
        TextField modelField = new TextField();

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();

        Label categoryLabel = new Label("Category:");
        ComboBox<String> categoryComboBox = new ComboBox<>(
                FXCollections.observableArrayList("SUV", "Sedan", "Pickup", "SportsCar"));

        Label acquisitionDateLabel = new Label("Acquisition Date:");
        DatePicker acquisitionDatePicker = new DatePicker();

        Label rentableLabel = new Label("Is Rented Out:");
        CheckBox rentableCheckBox = new CheckBox();

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.addRow(0, idLabel, idField);
        gridPane.addRow(1, manufacturerLabel, manufacturerField);
        gridPane.addRow(2, modelLabel, modelField);
        gridPane.addRow(3, priceLabel, priceField);
        gridPane.addRow(4, categoryLabel, categoryComboBox);
        gridPane.addRow(5, acquisitionDateLabel, acquisitionDatePicker);
        gridPane.addRow(6, rentableLabel, rentableCheckBox);

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        VBox layout = new VBox(10, gridPane, buttonBox);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 400, 350);
        wizardStage.setScene(scene);

        // Save action
        saveButton.setOnAction(event -> {
            try {
                String id = idField.getText().trim();
                String manufacturer = manufacturerField.getText().trim();
                String model = modelField.getText().trim();
                String category = categoryComboBox.getValue();
                String priceText = priceField.getText().trim();
                LocalDateTime acquisitionDate = acquisitionDatePicker.getValue() != null
                        ? acquisitionDatePicker.getValue().atStartOfDay()
                        : null;
                boolean isRentable = rentableCheckBox.isSelected();

                // Validate input
                if (id.isEmpty() || manufacturer.isEmpty() || model.isEmpty() || category == null || acquisitionDate == null) {
                    throw new IllegalArgumentException("All fields must be filled.");
                }

                double price;
                try {
                    price = Double.parseDouble(priceText);
                    if (price < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Price must be a valid non-negative number.");
                }

                // Use VehicleBuilder to create the vehicle
                Vehicle newVehicle = VehicleBuilder.buildVehicle(id, model, manufacturer, price,
                        selectedDealer.getId(), acquisitionDate, isRentable, category);

                if (newVehicle == null) {
                    throw new IllegalArgumentException("Invalid vehicle type selected.");
                }

                // Add vehicle to catalog
                Dealer dealer = DealerCatalog.getInstance().getDealerWithId(selectedDealer.getId());
                dealer.addVehicle(newVehicle);

                // Refresh UI list
                vehicleObservableList.setAll(dealer.getVehicleCatalog().values());

                // Show success alert with vehicle details
                String msg = "%s %s (%s) was added to Dealer #%s".formatted(
                        manufacturer, model, category, selectedDealer.getId()
                );
                FXController.showAlert(Alert.AlertType.INFORMATION, "Success", msg);

                wizardStage.close();

            } catch (Exception e) {
                FXController.showAlert(Alert.AlertType.ERROR, "Error Adding Vehicle", e.getMessage());
            }
        });

        // Cancel action
        cancelButton.setOnAction(event -> wizardStage.close());

        wizardStage.showAndWait();
    }


    /**
     * Opens wizard to transfer selected vehicles to another dealer.
     *
     * @param selected list of selected vehicles to transfer
     * @param currentDealer    current dealer from whom vehicles are being transferred
     * @param vehicleList      list to refresh after transfer
     * @param vehicleTable     TableView to refresh after transfer
     * @param dealerTable      TableView of dealers to refresh after transfer
     */
    public static void openTransferVehicleWizard(
            List<Vehicle> selected,
            Dealer currentDealer,
            ObservableList<Vehicle> vehicleList,
            TableView<Vehicle> vehicleTable,
            TableView<Dealer> dealerTable
    ) {
        // Get vehicles that are checked (not just selected)
        List<Vehicle> selectedVehicles = vehicleTable.getItems().stream()
                .filter(Vehicle::isSelected)
                .toList();

        if (selectedVehicles.isEmpty()) {
            FXController.showAlert(Alert.AlertType.WARNING, "No Vehicles Selected", "Please select vehicles to transfer.");
            return;
        }

        List<Dealer> otherDealers = DealerCatalog.getInstance().getDealers().stream()
                .filter(dealer -> !dealer.equals(currentDealer))
                .toList();

        if (otherDealers.isEmpty()) {
            FXController.showAlert(Alert.AlertType.WARNING, "No Other Dealers", "No available dealers to transfer to.");
            return;
        }

        ChoiceDialog<Dealer> dialog = new ChoiceDialog<>(otherDealers.getFirst(), otherDealers);
        dialog.setTitle("Transfer Vehicles");
        dialog.setHeaderText("Choose destination dealer:");
        dialog.setContentText("Dealer:");

        dialog.showAndWait().ifPresent(destinationDealer -> {
            ArrayList<Vehicle> toTransfer = new ArrayList<>(selectedVehicles);

            // Remove from current dealer
            for (Vehicle v : toTransfer) {
                currentDealer.getVehicleCatalog().remove(v.getVehicleId());
                v.setDealerId(destinationDealer.getId());  // Update the dealer ID
            }

            // Transfer to destination dealer
            DealerCatalog.getInstance().transferInventory(toTransfer, destinationDealer.getId());

            // Refresh UI
            vehicleList.setAll(currentDealer.getVehicleCatalog().values());
            vehicleTable.refresh();
            dealerTable.refresh();

            FXController.showAlert(Alert.AlertType.INFORMATION, "Transfer Complete",
                    "Vehicles transferred to " + destinationDealer.getName());
        });
    }



    /**
     * Filters vehicles by their type (e.g., SUV, Sedan, Pickup) based on the dealer selection.
     *
     * @param type                 the vehicle type to filter (e.g., "SUV")
     * @param selectedDealer       the currently selected dealer
     * @param allDealersSelected  true if all dealers are selected
     * @param outputList          the observable list to populate with filtered vehicles
     */
    public static void filterByType(String type, Dealer selectedDealer, boolean allDealersSelected,
                                    ObservableList<Vehicle> outputList) {
        if (allDealersSelected) {
            outputList.setAll(
                    DealerCatalog.getInstance().getAllVehicles().stream()
                            .filter(v -> v.getType().equalsIgnoreCase(type))
                            .toList()
            );
        } else if (selectedDealer != null) {
            outputList.setAll(
                    selectedDealer.getVehicleCatalog().values().stream()
                            .filter(v -> v.getType().equalsIgnoreCase(type))
                            .toList()
            );
        }
    }

    /**
     * Filters and returns all rented-out vehicles based on dealer selection.
     *
     * @param selectedDealer       the currently selected dealer
     * @param allDealersSelected   true if all dealers are selected
     * @param outputList           the observable list to populate with rented vehicles
     */
    public static void filterByRented(Dealer selectedDealer, boolean allDealersSelected,
                                      ObservableList<Vehicle> outputList) {
        List<Vehicle> rentedVehicles;

        if (allDealersSelected) {
            rentedVehicles = DealerCatalog.getInstance().getAllVehicles().stream()
                    .filter(Vehicle::getIsRentedOut)
                    .toList();
        } else if (selectedDealer != null) {
            rentedVehicles = selectedDealer.getRentedOutVehicles();
        } else {
            return;
        }

        outputList.setAll(rentedVehicles);
    }
}
