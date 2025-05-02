package edu.metrostate.ics372.ganby.FXAPP;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for handling dealer-related actions such as:
 * - Toggle all dealers selected
 * - Rename dealer
 * - Enable/disable acquisition
 * - Delete selected dealers (with optional vehicle transfer)
 */
public class DealerActionHelper {

    /**
     * Toggles checkbox selection of all dealers in the table.
     * Updates associated UI elements and vehicle list.
     *
     * @param dealerTable TableView containing dealer rows
     * @param vehicleTable TableView for displaying vehicles
     * @param dealerList list of all dealers
     * @param vehicleList observable list for vehicle updates
     * @param dealerIdField TextField showing selected dealer ID
     * @param dealerNameField TextField showing selected dealer name
     * @param selectAllDealersButton the toggle button itself
     * @param addVehicleButton button that adds a vehicle (disabled when all dealers are selected)
     */
    public static void toggleSelectAllDealers(
            TableView<Dealer> dealerTable,
            TableView<Vehicle> vehicleTable,
            ObservableList<Dealer> dealerList,
            ObservableList<Vehicle> vehicleList,
            TextField dealerIdField,
            TextField dealerNameField,
            Button selectAllDealersButton,
            Button addVehicleButton
    ) {
        boolean allSelected = dealerTable.getItems().stream().allMatch(Dealer::isSelected);

        for (Dealer dealer : dealerTable.getItems()) {
            dealer.setSelected(!allSelected);
        }

        if (!allSelected) {
            dealerIdField.setText("ALL");
            dealerNameField.setText("All Dealers Selected");
            vehicleList.setAll(DealerCatalog.getInstance().getAllVehicles());
            addVehicleButton.setDisable(true);
            selectAllDealersButton.setText("Unselect All Dealers");
        } else {
            dealerIdField.clear();
            dealerNameField.clear();
            vehicleList.clear();
            addVehicleButton.setDisable(false);
            selectAllDealersButton.setText("Select All Dealers");
        }

        dealerTable.refresh();
        vehicleTable.setItems(vehicleList);
    }

    /**
     * Opens a modal wizard for creating and adding a new dealer.
     * Ensures the dealer ID is unique and inputs are valid before adding.
     *
     * @param dealerObservableList the observable list to update with the new dealer
     * @param ownerStage the parent stage for modal blocking
     */
    public static void openAddDealerWizard(ObservableList<Dealer> dealerObservableList, Stage ownerStage) {
        Stage wizardStage = new Stage();
        wizardStage.initModality(Modality.APPLICATION_MODAL);
        wizardStage.initOwner(ownerStage);
        wizardStage.setTitle("Add New Dealer");

        // Form inputs
        Label idLabel = new Label("Dealer ID:");
        TextField idField = new TextField();

        Label nameLabel = new Label("Dealer Name:");
        TextField nameField = new TextField();

        Label acquisitionLabel = new Label("Acquisition Enabled:");
        CheckBox acquisitionCheckBox = new CheckBox();
        acquisitionCheckBox.setSelected(true);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.addRow(0, idLabel, idField);
        gridPane.addRow(1, nameLabel, nameField);
        gridPane.addRow(2, acquisitionLabel, acquisitionCheckBox);

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        VBox layout = new VBox(10, gridPane, buttonBox);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 400, 220);
        wizardStage.setScene(scene);

        // Save logic
        saveButton.setOnAction(event -> {
            try {
                String dealerId = idField.getText().trim();
                String dealerName = nameField.getText().trim();
                boolean isBuying = acquisitionCheckBox.isSelected();

                if (dealerId.isBlank()) {
                    throw new IllegalArgumentException("Dealer ID cannot be empty.");
                }

                if (DealerCatalog.getInstance().getDealerWithId(dealerId) != null) {
                    throw new IllegalArgumentException("Dealer with ID already exists.");
                }

                Dealer newDealer = new Dealer(dealerId, dealerName.isBlank() ? null : dealerName, isBuying);
                DealerCatalog.getInstance().addDealer(newDealer);
                dealerObservableList.add(newDealer);

                wizardStage.close();

                String msg = "Dealer '%s' (ID: %s) was successfully added.".formatted(newDealer.getName(), dealerId);
                AlertHelper.showSuccess("Success", msg);
            } catch (Exception e) {
                AlertHelper.showError("Error", e.getMessage());
            }
        });

        cancelButton.setOnAction(e -> wizardStage.close());
        wizardStage.showAndWait();
    }

    /**
     * Prompts user to rename a selected dealer.
     * Validates that exactly one dealer is selected and updates the name.
     *
     * @param dealerTable the dealer TableView to query selected dealer from
     * @param dealerNameField the UI text field to update with the new name
     */
    public static void renameDealer(TableView<Dealer> dealerTable, TextField dealerNameField) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();

        if (selectedDealers.isEmpty()) {
            AlertHelper.showWarning("No Dealer Selected", "Please select a dealer to edit.");
            return;
        }

        if (selectedDealers.size() > 1) {
            AlertHelper.showWarning("Multiple Dealers Selected", "Please select only one dealer to edit.");
            return;
        }

        Dealer selectedDealer = selectedDealers.getFirst();

        TextInputDialog dialog = new TextInputDialog(selectedDealer.getName());
        dialog.setTitle("Edit Dealer Name");
        dialog.setHeaderText("Update name for Dealer ID: " + selectedDealer.getId());
        dialog.setContentText("New Dealer Name:");

        dialog.showAndWait().ifPresent(newName -> {
            if (newName.trim().isEmpty()) {
                AlertHelper.showError("Invalid Input", "Dealer name cannot be empty.");
            } else {
                selectedDealer.setName(newName.trim());
                dealerTable.refresh();
                dealerNameField.setText(newName.trim());
                AlertHelper.showSuccess("Updated", "Dealer name successfully updated.");
            }
        });
    }

    /**
     * Sets acquisition status for all selected dealers.
     *
     * @param dealerTable TableView containing dealers
     * @param enable true to enable acquisition, false to disable it
     */
    public static void setAcquisitionStatus(TableView<Dealer> dealerTable, boolean enable) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();

        if (selectedDealers.isEmpty()) {
            AlertHelper.showWarning("No Dealer Selected", "Please check at least one dealer.");
            return;
        }

        for (Dealer dealer : selectedDealers) {
            if (enable) {
                DealerCatalog.getInstance().enableDealerAcquisition(dealer.getId());
            } else {
                DealerCatalog.getInstance().disableDealerAcquisition(dealer.getId());
            }
        }

        dealerTable.refresh();
    }

    /**
     * Deletes selected dealers. If a dealer has vehicles, user can delete or transfer them.
     *
     * @param selectedDealers list of dealers selected for deletion
     * @param dealerObservableList the observable dealer list (UI)
     * @param vehicleObservableList the observable vehicle list (UI)
     * @param dealerTable the dealer TableView
     * @param vehicleTable the vehicle TableView
     */
    public static void deleteDealers(List<Dealer> selectedDealers,
                                     ObservableList<Dealer> dealerObservableList,
                                     ObservableList<Vehicle> vehicleObservableList,
                                     TableView<Dealer> dealerTable,
                                     TableView<Vehicle> vehicleTable) {

        selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();

        if (selectedDealers.isEmpty()) {
            AlertHelper.showWarning("No Dealers Selected", "Please select dealers to delete.");
            return;
        }

        for (Dealer dealer : new ArrayList<>(selectedDealers)) {
            List<Dealer> otherDealers = DealerCatalog.getInstance().getDealers().stream()
                    .filter(d -> !d.equals(dealer))
                    .toList();

            int vehicleCount = dealer.vehicleCatalog.size();

            if (vehicleCount > 0) {
                ButtonType deleteVehicles = new ButtonType("Delete Vehicles", ButtonBar.ButtonData.YES);
                ButtonType transferVehicles = new ButtonType("Transfer Vehicles", ButtonBar.ButtonData.NO);
                ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                Optional<ButtonType> response = AlertHelper.showConfirmation(
                        "Delete Dealer",
                        "Dealer " + dealer.getName() + " has " + vehicleCount + " vehicle(s).",
                        "Choose an option for the dealer's vehicle inventory:",
                        deleteVehicles, transferVehicles, cancel
                );

                if (response.orElse(cancel) == deleteVehicles) {
                    dealer.vehicleCatalog.clear();
                    finalizeDeletion(dealer, dealerObservableList, vehicleObservableList);
                } else if (response.orElse(cancel) == transferVehicles) {
                    if (otherDealers.isEmpty()) {
                        AlertHelper.showError("No Available Dealers", "No other dealers to transfer vehicles to.");
                        continue;
                    }

                    ChoiceDialog<Dealer> dialog = new ChoiceDialog<>(otherDealers.getFirst(), otherDealers);
                    dialog.setTitle("Transfer Inventory");
                    dialog.setHeaderText("Transfer vehicles from " + dealer.getName());
                    dialog.setContentText("Transfer to:");

                    dialog.showAndWait().ifPresent(destination -> {
                        ArrayList<Vehicle> toTransfer = new ArrayList<>(dealer.vehicleCatalog.values());
                        DealerCatalog.getInstance().transferInventory(toTransfer, destination.getId());
                        dealer.vehicleCatalog.clear();
                        finalizeDeletion(dealer, dealerObservableList, vehicleObservableList);
                        AlertHelper.showSuccess("Transfer Complete",
                                "Vehicles transferred to " + destination.getName() + ".");
                    });
                }
            } else {
                finalizeDeletion(dealer, dealerObservableList, vehicleObservableList);
            }
        }

        dealerTable.refresh();
        vehicleTable.refresh();
    }

    /**
     * Finalizes the deletion of a dealer from catalog and observable lists.
     *
     * @param dealer the dealer to remove
     * @param dealerObservableList observable list of dealers (UI)
     * @param vehicleObservableList observable list of vehicles (UI)
     */
    private static void finalizeDeletion(Dealer dealer,
                                         ObservableList<Dealer> dealerObservableList,
                                         ObservableList<Vehicle> vehicleObservableList) {

        DealerCatalog.getInstance().getDealers().remove(dealer);
        DealerCatalog.getInstance().getDealerCatalog().remove(dealer.getId());
        dealerObservableList.remove(dealer);

        // Clear vehicle list from UI
        vehicleObservableList.clear();
    }
}
