package edu.metrostate.ics372.ganby.FXAPP;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling dealer-related actions such as:
 * - Toggle all dealers selected
 * - Rename dealer
 * - Enable/disable acquisition
 * - Delete selected dealers (with optional vehicle transfer)
 */
public class DealerActionHelper {

    /**
     * Toggles all checkboxes in the dealer table.
     *
     * @param dealerTable TableView containing dealers
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
     * Renames the selected dealer using a dialog.
     *
     * @param dealerTable TableView containing dealers
     * @param dealerNameField TextField to update the name in the detail pane
     */
    public static void renameDealer(TableView<Dealer> dealerTable, TextField dealerNameField) {
        Dealer selectedDealer = dealerTable.getSelectionModel().getSelectedItem();

        if (selectedDealer == null) {
            showAlert(AlertType.WARNING, "No Dealer Selected", "Please select a dealer to edit.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedDealer.getName());
        dialog.setTitle("Edit Dealer Name");
        dialog.setHeaderText("Update name for Dealer ID: " + selectedDealer.getId());
        dialog.setContentText("New Dealer Name:");

        dialog.showAndWait().ifPresent(newName -> {
            if (newName.trim().isEmpty()) {
                showAlert(AlertType.ERROR, "Invalid Input", "Dealer name cannot be empty.");
            } else {
                selectedDealer.setName(newName.trim());
                dealerTable.refresh();
                dealerNameField.setText(newName.trim());
                showAlert(AlertType.INFORMATION, "Updated", "Dealer name successfully updated.");
            }
        });
    }

    /**
     * Enables or disables acquisition for the selected dealer.
     *
     * @param dealerTable TableView of dealers
     * @param toggleButton Button to update text after toggle
     * @param dealerIdField TextField to show dealer ID
     * @param dealerNameField TextField to show dealer name
     */
    public static void toggleAcquisitionStatus(TableView<Dealer> dealerTable,
                                               Button toggleButton,
                                               TextField dealerIdField,
                                               TextField dealerNameField) {
        Dealer selectedDealer = dealerTable.getSelectionModel().getSelectedItem();
        if (selectedDealer != null) {
            boolean current = selectedDealer.getIsVehicleAcquisitionEnabled();
            if (current) {
                DealerCatalog.getInstance().disableDealerAcquisition(selectedDealer.getId());
            } else {
                DealerCatalog.getInstance().enableDealerAcquisition(selectedDealer.getId());
            }

            toggleButton.setText(current ? "Enable Acquisition" : "Disable Acquisition");
            dealerIdField.setText(selectedDealer.getId());
            dealerNameField.setText(selectedDealer.getName());
            dealerTable.refresh();
        } else {
            showAlert(AlertType.WARNING, "No Dealer Selected", "Please select a dealer to toggle acquisition status.");
        }
    }

    /**
     * Deletes selected dealers and handles optional vehicle transfer.
     *
     * @param dealerTable TableView of dealers
     * @param dealerObservableList Dealer observable list
     * @param vehicleObservableList Vehicle observable list
     * @param vehicleTable Vehicle TableView
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
            showAlert(AlertType.WARNING, "No Dealers Selected", "Please select dealers to delete.");
            return;
        }

        for (Dealer dealer : new ArrayList<>(selectedDealers)) {
            List<Dealer> otherDealers = DealerCatalog.getInstance().getDealers().stream()
                    .filter(d -> !d.equals(dealer))
                    .toList();

            int vehicleCount = dealer.getVehicleCatalog().size();

            if (vehicleCount > 0) {
                Alert confirm = new Alert(AlertType.CONFIRMATION);
                confirm.setTitle("Delete Dealer");
                confirm.setHeaderText("Dealer " + dealer.getName() + " has " + vehicleCount + " vehicle(s).");
                confirm.setContentText("Choose an option for the dealer's vehicle inventory:");

                ButtonType deleteVehicles = new ButtonType("Delete Vehicles", ButtonData.YES);
                ButtonType transferVehicles = new ButtonType("Transfer Vehicles", ButtonData.NO);
                ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

                confirm.getButtonTypes().setAll(deleteVehicles, transferVehicles, cancel);

                ButtonType response = confirm.showAndWait().orElse(cancel);

                if (response == deleteVehicles) {
                    dealer.getVehicleCatalog().clear();
                    finalizeDeletion(dealer, dealerObservableList, vehicleObservableList);
                } else if (response == transferVehicles) {
                    if (otherDealers.isEmpty()) {
                        showAlert(AlertType.ERROR, "No Available Dealers", "No other dealers to transfer vehicles to.");
                        continue;
                    }

                    ChoiceDialog<Dealer> dialog = new ChoiceDialog<>(otherDealers.get(0), otherDealers);
                    dialog.setTitle("Transfer Inventory");
                    dialog.setHeaderText("Transfer vehicles from " + dealer.getName());
                    dialog.setContentText("Transfer to:");

                    dialog.showAndWait().ifPresent(destination -> {
                        ArrayList<Vehicle> toTransfer = new ArrayList<>(dealer.getVehicleCatalog().values());
                        DealerCatalog.getInstance().transferInventory(toTransfer, destination.getId());
                        dealer.getVehicleCatalog().clear();
                        finalizeDeletion(dealer, dealerObservableList, vehicleObservableList);
                        showAlert(AlertType.INFORMATION, "Transfer Complete",
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
     * Finalizes the deletion of a dealer from catalog and UI.
     */
    private static void finalizeDeletion(Dealer dealer,
                                         ObservableList<Dealer> dealerObservableList,
                                         ObservableList<Vehicle> vehicleObservableList) {

        DealerCatalog.getInstance().getDealers().remove(dealer);
        DealerCatalog.getInstance().getDealerCatalog().remove(dealer.getId());
        dealerObservableList.remove(dealer);

        // Clear vehicles if they belonged to the deleted dealer
        vehicleObservableList.clear();
    }

    /**
     * Shows a JavaFX alert dialog.
     */
    private static void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
