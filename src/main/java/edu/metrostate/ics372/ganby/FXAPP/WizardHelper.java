package edu.metrostate.ics372.ganby.FXAPP;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Centralized helper class for launching dealer and vehicle-related wizards.
 */
public class WizardHelper {

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
                showAlert(Alert.AlertType.INFORMATION, "Success", msg);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        });

        cancelButton.setOnAction(e -> wizardStage.close());
        wizardStage.showAndWait();
    }

    /**
     * Opens wizard to transfer selected vehicles to another dealer.
     *
     * @param selectedVehicles list of selected vehicles to transfer
     * @param currentDealer    current dealer from whom vehicles are being transferred
     * @param vehicleList      list to refresh after transfer
     * @param vehicleTable     TableView to refresh after transfer
     * @param dealerTable      TableView of dealers to refresh after transfer
     */
    public static void openTransferVehicleWizard(
            List<Vehicle> selectedVehicles,
            Dealer currentDealer,
            ObservableList<Vehicle> vehicleList,
            TableView<Vehicle> vehicleTable,
            TableView<Dealer> dealerTable
    ) {
        if (selectedVehicles.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Vehicles Selected", "Please select vehicles to transfer.");
            return;
        }

        List<Dealer> otherDealers = DealerCatalog.getInstance().getDealers().stream()
                .filter(dealer -> !dealer.equals(currentDealer))
                .toList();

        if (otherDealers.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Other Dealers", "No available dealers to transfer to.");
            return;
        }

        ChoiceDialog<Dealer> dialog = new ChoiceDialog<>(otherDealers.get(0), otherDealers);
        dialog.setTitle("Transfer Vehicles");
        dialog.setHeaderText("Choose destination dealer:");
        dialog.setContentText("Dealer:");

        dialog.showAndWait().ifPresent(destinationDealer -> {
            ArrayList<Vehicle> toTransfer = new ArrayList<>(selectedVehicles);

            for (Vehicle v : toTransfer) {
                currentDealer.getVehicleCatalog().remove(v.getVehicleId());
            }

            DealerCatalog.getInstance().transferInventory(toTransfer, destinationDealer.getId());

            vehicleList.setAll(currentDealer.getVehicleCatalog().values());
            vehicleTable.refresh();
            dealerTable.refresh();

            showAlert(Alert.AlertType.INFORMATION, "Transfer Complete", "Vehicles transferred to " + destinationDealer.getName());
        });
    }

    /**
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
            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to add a vehicle.");
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

                // Instantiate the appropriate vehicle type
                Vehicle newVehicle = switch (category) {
                    case "SUV" -> new SUV(id, model, manufacturer, price, selectedDealer.getId(), acquisitionDate);
                    case "Sedan" -> new Sedan(id, model, manufacturer, price, selectedDealer.getId(), acquisitionDate);
                    case "Pickup" -> new Pickup(id, model, manufacturer, price, selectedDealer.getId(), acquisitionDate);
                    case "SportsCar" -> new SportsCar(id, model, manufacturer, price, selectedDealer.getId(), acquisitionDate);
                    default -> throw new IllegalArgumentException("Invalid vehicle type selected.");
                };

                newVehicle.setRentedOut(isRentable);

                // Add vehicle to catalog
                Dealer dealer = DealerCatalog.getInstance().getDealerWithId(selectedDealer.getId());
                dealer.addVehicle(newVehicle);

                // Refresh UI list
                vehicleObservableList.setAll(dealer.getVehicleCatalog().values());

                // Show success alert with vehicle details
                String msg = "%s %s (%s) was added to Dealer #%s".formatted(
                        manufacturer, model, category, selectedDealer.getId()
                );
                showAlert(Alert.AlertType.INFORMATION, "Success", msg);

                wizardStage.close();

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error Adding Vehicle", e.getMessage());
            }
        });

        // Cancel action
        cancelButton.setOnAction(event -> wizardStage.close());

        wizardStage.showAndWait();
    }



    /**
     * Shows a generic JavaFX alert dialog.
     *
     * @param type    the alert type
     * @param title   title of the alert
     * @param content body text of the alert
     */
    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
