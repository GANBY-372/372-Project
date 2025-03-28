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
     * Opens a modal wizard to create and add a new dealer.
     *
     * @param dealerObservableList list to update after adding
     * @param ownerStage the parent window for modality
     */
    public static void openAddDealerWizard(ObservableList<Dealer> dealerObservableList, Stage ownerStage) {
        Stage wizardStage = new Stage();
        wizardStage.initModality(Modality.APPLICATION_MODAL);
        wizardStage.initOwner(ownerStage);
        wizardStage.setTitle("Add New Dealer");

        Label nameLabel = new Label("Dealer Name:");
        TextField nameField = new TextField();
        Label idLabel = new Label("Dealer ID:");
        TextField idField = new TextField();

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.addRow(0, idLabel, idField);
        gridPane.addRow(1, nameLabel, nameField);

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        VBox layout = new VBox(10, gridPane, buttonBox);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 400, 200);
        wizardStage.setScene(scene);

        saveButton.setOnAction(event -> {
            try {
                String dealerId = idField.getText().trim();
                String dealerName = nameField.getText().trim();

                if (dealerId.isBlank() || dealerName.isBlank()) {
                    throw new IllegalArgumentException("Dealer ID and Name cannot be empty.");
                }

                if (DealerCatalog.getInstance().getDealerWithId(dealerId) != null) {
                    throw new IllegalArgumentException("Dealer with ID already exists.");
                }

                Dealer newDealer = new Dealer(dealerId, dealerName);
                DealerCatalog.getInstance().addDealer(newDealer);
                dealerObservableList.add(newDealer);

                wizardStage.close();

                // Show success message with details
                String msg = "Dealer '%s' (ID: %s) was successfully added.".formatted(dealerName, dealerId);
                FXController.showAlert(Alert.AlertType.INFORMATION, "Success", msg);
            } catch (Exception e) {
                FXController.showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        });

        cancelButton.setOnAction(e -> wizardStage.close());
        wizardStage.showAndWait();
    }

    /**
     * Renames the selected dealer using a dialog.
     *
     * @param dealerTable TableView containing dealers
     * @param dealerNameField TextField to update the name in the detail pane
     */
    public static void renameDealer(TableView<Dealer> dealerTable, TextField dealerNameField) {
        Dealer selectedDealer = dealerTable.getSelectionModel().getSelectedItem();
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();

        if (selectedDealers.isEmpty()) {
            FXController.showAlert(AlertType.WARNING, "No Dealer Selected", "Please check at least one dealer to toggle acquisition.");
            return;
        }

       /* for (Dealer dealer : selectedDealers) {
            boolean current = dealer.getIsVehicleAcquisitionEnabled();
            if (current) {
                DealerCatalog.getInstance().disableDealerAcquisition(dealer.getId());
            } else {
                DealerCatalog.getInstance().enableDealerAcquisition(dealer.getId());
            }
        }
        */
        if (selectedDealer == null) {
            FXController.showAlert(AlertType.WARNING, "No Dealer Selected", "Please select a dealer to edit.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedDealer.getName());
        dialog.setTitle("Edit Dealer Name");
        dialog.setHeaderText("Update name for Dealer ID: " + selectedDealer.getId());
        dialog.setContentText("New Dealer Name:");

        dialog.showAndWait().ifPresent(newName -> {
            if (newName.trim().isEmpty()) {
                FXController.showAlert(AlertType.ERROR, "Invalid Input", "Dealer name cannot be empty.");
            } else {
                selectedDealer.setName(newName.trim());
                dealerTable.refresh();
                dealerNameField.setText(newName.trim());
                FXController.showAlert(AlertType.INFORMATION, "Updated", "Dealer name successfully updated.");
            }
        });
    }

    /**
     * Enables or disables acquisition for the selected dealer.
     *
     * @param dealerTable TableView of dealers
     */
    public static void toggleAcquisitionStatus(TableView<Dealer> dealerTable) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();

        if (selectedDealers.isEmpty()) {
            FXController.showAlert(AlertType.WARNING, "No Dealer Selected", "Please check at least one dealer to toggle acquisition.");
            return;
        }

        for (Dealer dealer : selectedDealers) {
            boolean current = dealer.getIsVehicleAcquisitionEnabled();
            if (current) {
                DealerCatalog.getInstance().disableDealerAcquisition(dealer.getId());
            } else {
                DealerCatalog.getInstance().enableDealerAcquisition(dealer.getId());
            }
        }

        dealerTable.refresh();
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
            FXController.showAlert(AlertType.WARNING, "No Dealers Selected", "Please select dealers to delete.");
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
                        FXController.showAlert(AlertType.ERROR, "No Available Dealers", "No other dealers to transfer vehicles to.");
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
                        FXController.showAlert(AlertType.INFORMATION, "Transfer Complete",
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
