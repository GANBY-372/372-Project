package edu.metrostate.ics372.ganby.wizard;//package edu.metrostate.ics372.ganby.ganby.wizard;
//
//
//import edu.metrostate.ics372.ganby.ganby.dealer.Dealer;
//import edu.metrostate.ics372.ganby.ganby.vehicle.Pickup;
//import edu.metrostate.ics372.ganby.ganby.vehicle.Vehicle;
//import edu.metrostate.ics372.ganby.ganby.vehicle.VehicleCategory;
//import javafx.collections.FXCollections;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//
//import java.time.LocalDateTime;
//
//public class AddVehicleWizard {
//
//    private final Dealer selectedDealer;
//
//    public AddVehicleWizard(Dealer dealer) {
//        this.selectedDealer = dealer;
//    }
//
//    public void show() {
//        if (selectedDealer == null) {
//            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer first.");
//            return;
//        }
//
//        // Create a new stage for the wizard
//        Stage wizardStage = new Stage();
//        wizardStage.initModality(Modality.APPLICATION_MODAL);
//        wizardStage.setTitle("Add New Vehicle Wizard");
//
//        // Create input fields for the vehicle
//        Label idLabel = new Label("Vehicle ID:");
//        TextField idField = new TextField();
//
//        Label makeLabel = new Label("Make:");
//        TextField makeField = new TextField();
//
//        Label priceLabel = new Label("Price:");
//        TextField priceField = new TextField();
//
//        Label categoryLabel = new Label("Category:");
//        ComboBox<VehicleCategory> categoryComboBox = new ComboBox<>(FXCollections.observableArrayList(VehicleCategory.values()));
//
//        Label acquisitionDateLabel = new Label("Acquisition Date:");
//        DatePicker acquisitionDatePicker = new DatePicker();
//
//        Label rentableLabel = new Label("Is Rentable:");
//        CheckBox rentableCheckBox = new CheckBox();
//
//        // Save and cancel buttons
//        Button saveButton = new Button("Save");
//        Button cancelButton = new Button("Cancel");
//
//        // GridPane for layout
//        GridPane gridPane = new GridPane();
//        gridPane.setVgap(10);
//        gridPane.setHgap(10);
//
//        gridPane.addRow(0, idLabel, idField);
//        gridPane.addRow(1, makeLabel, makeField);
//        gridPane.addRow(2, priceLabel, priceField);
//        gridPane.addRow(3, categoryLabel, categoryComboBox);
//        gridPane.addRow(4, acquisitionDateLabel, acquisitionDatePicker);
//        gridPane.addRow(5, rentableLabel, rentableCheckBox);
//
//        // Buttons HBox
//        HBox buttonBox = new HBox(10, saveButton, cancelButton);
//
//        // VBox layout
//        VBox layout = new VBox(10, gridPane, buttonBox);
//
//        // Scene
//        Scene scene = new Scene(layout, 400, 300);
//        wizardStage.setScene(scene);
//
//        // Save button event logic
//        saveButton.setOnAction(event -> {
//            try {
//                // Read and validate inputs
//                String id = idField.getText();
//                String make = makeField.getText();
//                double price = Double.parseDouble(priceField.getText());
//                VehicleCategory category = categoryComboBox.getValue();
//                LocalDateTime acquisitionDate = acquisitionDatePicker.getValue().atStartOfDay();
//                boolean isRentable = rentableCheckBox.isSelected();
//
//                if (make.isBlank() || category == null) {
//                    throw new IllegalArgumentException("Make and Category fields cannot be empty.");
//                }
//
//                // Create a new Vehicle
//                Pickup newVehicle = new Pickup(id,"F150", make)
////                Vehicle newVehicle = new Vehicle.Builder()
////                    .id(id)
////                    .make(make)
////                    .price(price)
////                    .category(category)
////                    .LocalDateTime(acquisitionDate)
////                    .BooleanProperty(rentableCheckBox.selectedProperty())
////                    .dealer(selectedDealer)
////                    .build();
//
//                // Add new vehicle to the dealer
//                selectedDealer.getVehicleCatalog().put(newVehicle.getVehicleId(), newVehicle);
//
//                // Close the wizard
//                wizardStage.close();
//
//                showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle successfully added!");
//
//            } catch (Exception e) {
//                showAlert(Alert.AlertType.ERROR, "Error Adding Vehicle", e.getMessage());
//            }
//        });
//
//        // Close wizard on cancel
//        cancelButton.setOnAction(event -> wizardStage.close());
//
//        // Show the wizard
//        wizardStage.showAndWait();
//    }
//
//    private void showAlert(Alert.AlertType alertType, String title, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}