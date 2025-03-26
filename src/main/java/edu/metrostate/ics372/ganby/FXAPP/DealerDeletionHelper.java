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
 * Utility class for handling dealer deletion operations within the JavaFX UI.
 * <p>
 * Supports:
 * <ul>
 *     <li>Deleting dealers with or without vehicles.</li>
 *     <li>Deleting all vehicles along with the dealer.</li>
 *     <li>Transferring dealer inventory to another dealer before deletion.</li>
 * </ul>
 */
public class DealerDeletionHelper {

    /**
     * Handles dealer deletion logic, offering options to remove or transfer vehicles before deletion.
     *
     * @param selectedDealer        The dealer selected for deletion
     * @param dealerObservableList  The observable dealer list backing the TableView
     * @param vehicleObservableList The observable vehicle list backing the TableView
     * @param dealerTable           The dealer TableView (used for UI refreshing)
     * @param vehicleTable          The vehicle TableView (used for UI refreshing)
     */
    public static void deleteDealer(Dealer selectedDealer,
                                    ObservableList<Dealer> dealerObservableList,
                                    ObservableList<Vehicle> vehicleObservableList,
                                    TableView<Dealer> dealerTable,
                                    TableView<Vehicle> vehicleTable) {

        if (selectedDealer == null) {
            showAlert(AlertType.WARNING, "No Dealer Selected", "Please select a dealer to delete.");
            return;
        }

        // Get list of other dealers for possible transfer
        List<Dealer> otherDealers = DealerCatalog.getInstance().getDealers().stream()
                .filter(d -> !d.equals(selectedDealer))
                .toList();

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Delete Dealer");
        confirm.setHeaderText("Dealer has " + selectedDealer.getVehicleCatalog().size() + " vehicle(s).");
        confirm.setContentText("Choose an option for the dealer's vehicle inventory:");

        ButtonType deleteVehicles = new ButtonType("Delete Vehicles", ButtonData.YES);
        ButtonType transferVehicles = new ButtonType("Transfer Vehicles", ButtonData.NO);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        confirm.getButtonTypes().setAll(deleteVehicles, transferVehicles, cancel);

        confirm.showAndWait().ifPresent(response -> {
            if (response == deleteVehicles) {
                // Delete all vehicles and the dealer
                selectedDealer.getVehicleCatalog().clear();
                finalizeDeletion(selectedDealer, dealerObservableList, vehicleObservableList, dealerTable, vehicleTable);
            } else if (response == transferVehicles) {
                handleTransferBeforeDeletion(selectedDealer, otherDealers, dealerObservableList, vehicleObservableList, dealerTable, vehicleTable);
            }
        });
    }

    /**
     * Handles vehicle transfer to another dealer before the selected dealer is deleted.
     *
     * @param currentDealer         The dealer whose vehicles are to be transferred
     * @param otherDealers          A list of other dealers to choose from
     * @param dealerObservableList  Observable list of dealers
     * @param vehicleObservableList Observable list of vehicles
     * @param dealerTable           Dealer table to refresh UI
     * @param vehicleTable          Vehicle table to refresh UI
     */
    private static void handleTransferBeforeDeletion(Dealer currentDealer,
                                                     List<Dealer> otherDealers,
                                                     ObservableList<Dealer> dealerObservableList,
                                                     ObservableList<Vehicle> vehicleObservableList,
                                                     TableView<Dealer> dealerTable,
                                                     TableView<Vehicle> vehicleTable) {

        if (otherDealers.isEmpty()) {
            showAlert(AlertType.ERROR, "No Available Dealers", "No other dealers to transfer vehicles to.");
            return;
        }

        ChoiceDialog<Dealer> dialog = new ChoiceDialog<>(otherDealers.get(0), otherDealers);
        dialog.setTitle("Transfer Inventory");
        dialog.setHeaderText("Choose a dealer to transfer inventory to before deletion:");
        dialog.setContentText("Transfer to:");

        dialog.showAndWait().ifPresent(destination -> {
            ArrayList<Vehicle> toTransfer = new ArrayList<>(currentDealer.getVehicleCatalog().values());
            DealerCatalog.getInstance().transferInventory(toTransfer, destination.getId());
            currentDealer.getVehicleCatalog().clear();  // Clear after transfer
            finalizeDeletion(currentDealer, dealerObservableList, vehicleObservableList, dealerTable, vehicleTable);

            showAlert(AlertType.INFORMATION, "Transfer Successful",
                    "Vehicles transferred to " + destination.getName() + " before deletion.");
        });
    }

    /**
     * Finalizes the deletion of a dealer, removing them from all relevant collections and refreshing the UI.
     *
     * @param dealer                The dealer to be deleted
     * @param dealerObservableList Observable list of dealers
     * @param vehicleObservableList Observable list of vehicles
     * @param dealerTable           Dealer TableView to refresh
     * @param vehicleTable          Vehicle TableView to refresh
     */
    private static void finalizeDeletion(Dealer dealer,
                                         ObservableList<Dealer> dealerObservableList,
                                         ObservableList<Vehicle> vehicleObservableList,
                                         TableView<Dealer> dealerTable,
                                         TableView<Vehicle> vehicleTable) {
        DealerCatalog.getInstance().getDealers().remove(dealer);
        DealerCatalog.getInstance().getDealerCatalog().remove(dealer.getId());
        dealerObservableList.remove(dealer);

        vehicleObservableList.clear();
        dealerTable.refresh();
        vehicleTable.refresh();
    }

    /**
     * Utility method for showing JavaFX alerts with provided content.
     *
     * @param type    The type of alert (INFO, WARNING, ERROR, etc.)
     * @param title   The title of the alert dialog
     * @param content The message/body of the alert
     */
    private static void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
