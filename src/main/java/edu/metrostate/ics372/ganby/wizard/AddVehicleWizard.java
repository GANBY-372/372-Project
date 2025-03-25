/*

package edu.metrostate.ics372.ganby.wizard;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.Pickup;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class AddVehicleWizard {

    private final Dealer selectedDealer;

    public AddVehicleWizard(Dealer dealer) {
        this.selectedDealer = dealer;
    }

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
        ComboBox<VehicleCategory> categoryComboBox = new ComboBox<>(FXCollections.observableArrayList(VehicleCategory.values()));

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
                String id = idField.getText();
                String make = makeField.getText();
                double price = Double.parseDouble(priceField.getText());
                String type = categoryComboBox.getValue();
                LocalDateTime acquisitionDate = acquisitionDatePicker.getValue().atStartOfDay();
                boolean isRentable = rentableCheckBox.isSelected();

                if (make.isBlank() || type == null) {
                    throw new IllegalArgumentException("Make and Category fields cannot be empty.");
                }

                Pickup newVehicle = new Pickup(id,"F150", make);

                selectedDealer.getVehicleCatalog().put(newVehicle.getVehicleId(), newVehicle);

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


 */