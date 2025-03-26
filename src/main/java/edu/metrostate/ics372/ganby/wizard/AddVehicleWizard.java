package edu.metrostate.ics372.ganby.wizard;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;

/**
 * Wizard class to allow adding a new vehicle to a selected dealer.
 */
public class AddVehicleWizard {

    private final Dealer selectedDealer;
    private ObservableList<Vehicle> vehicleObservableList = FXCollections.observableArrayList();

    public AddVehicleWizard(Dealer dealer) {
        this.selectedDealer = dealer;
    }

    public AddVehicleWizard(Dealer dealer, ObservableList<Vehicle> vehicleObservableList) {
        this.selectedDealer = dealer;
        this.vehicleObservableList = vehicleObservableList;
    }

    /**
     * Show the wizard dialog for adding a new vehicle.
     */
    public void show() {
        if (selectedDealer == null) {
            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer first.");
            return;
        }

        Stage wizardStage = new Stage();
        wizardStage.initModality(Modality.APPLICATION_MODAL);
        wizardStage.setTitle("Add New Vehicle Wizard");

        Label idLabel = new Label("Vehicle ID:");
        TextField idField = new TextField();

        Label makeLabel = new Label("Make:");
        TextField makeField = new TextField();

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();

        Label categoryLabel = new Label("Category:");
        ComboBox<String> categoryComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "SUV", "Sedan", "Pickup", "SportsCar"
        ));

        Label acquisitionDateLabel = new Label("Acquisition Date:");
        DatePicker acquisitionDatePicker = new DatePicker();

        Label rentableLabel = new Label("Is Rentable:");
        CheckBox rentableCheckBox = new CheckBox();

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.addRow(0, idLabel, idField);
        gridPane.addRow(1, makeLabel, makeField);
        gridPane.addRow(2, priceLabel, priceField);
        gridPane.addRow(3, categoryLabel, categoryComboBox);
        gridPane.addRow(4, acquisitionDateLabel, acquisitionDatePicker);
        gridPane.addRow(5, rentableLabel, rentableCheckBox);

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        VBox layout = new VBox(10, gridPane, buttonBox);

        Scene scene = new Scene(layout, 400, 300);
        wizardStage.setScene(scene);

        saveButton.setOnAction(event -> {
            try {
                String id = idField.getText().trim();
                String make = makeField.getText().trim();
                String category = categoryComboBox.getValue();
                String priceText = priceField.getText().trim();
                LocalDateTime acquisitionDate = acquisitionDatePicker.getValue() != null ?
                        acquisitionDatePicker.getValue().atStartOfDay() : null;
                boolean isRentable = rentableCheckBox.isSelected();

                if (id.isEmpty() || make.isEmpty() || category == null || acquisitionDate == null) {
                    throw new IllegalArgumentException("All fields must be filled.");
                }

                double price;
                try {
                    price = Double.parseDouble(priceText);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Price must be a valid number.");
                }

                Vehicle newVehicle = switch (category) {
                    case "SUV" -> new SUV(id, category, make, price, selectedDealer.getId(), acquisitionDate);
                    case "Sedan" -> new Sedan(id, category, make, price, selectedDealer.getId(), acquisitionDate);
                    case "Pickup" -> new Pickup(id, category, make, price, selectedDealer.getId(), acquisitionDate);
                    case "SportsCar" -> new SportsCar(id, category, make, price, selectedDealer.getId(), acquisitionDate);
                    default -> throw new IllegalArgumentException("Invalid vehicle type selected.");
                };

                newVehicle.setRentedStatus(isRentable);

                Dealer dealer = DealerCatalog.getInstance().getDealerWithId(selectedDealer.getId());
                dealer.addVehicle(newVehicle);

                //Refresh observable list
                vehicleObservableList.setAll(dealer.getVehicleCatalog().values());


                wizardStage.close();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle successfully added!");

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error Adding Vehicle", e.getMessage());
            }
        });

        cancelButton.setOnAction(event -> wizardStage.close());

        wizardStage.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
