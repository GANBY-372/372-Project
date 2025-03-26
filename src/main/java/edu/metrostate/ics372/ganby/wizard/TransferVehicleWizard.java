package edu.metrostate.ics372.ganby.wizard;


import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class TransferVehicleWizard {

    private final Vehicle vehicle; // The vehicle being transferred
    private final Dealer currentDealer; // Dealer who currently owns the vehicle

    public TransferVehicleWizard(Vehicle vehicle, Dealer currentDealer) {
        this.vehicle = vehicle;
        this.currentDealer = currentDealer;
    }

    public void show() {
        // Create the Wizard Stage
        Stage wizardStage = new Stage();
        wizardStage.initModality(Modality.APPLICATION_MODAL);
        wizardStage.setTitle("Transfer Vehicle Wizard");

        // UI Components
        Label vehicleLabel = new Label("Vehicle:");
        Label vehicleDetailLabel = new Label(vehicle.toString() != null ? vehicle.toString() : "No Vehicle Details Available");

        Label dealerLabel = new Label("Transfer To Dealer:");
        ObservableList<Dealer> dealersBuying = FXCollections.observableArrayList(DealerCatalog.getInstance().getDealersWhoAreBuying());
        ComboBox<Dealer> dealerComboBox = new ComboBox<>(dealersBuying); // Populate ComboBox with dealers willing to buy
        dealerComboBox.setPromptText("Select Dealer");

        // Configure the ComboBox to show dealer names
        dealerComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Dealer dealer) {
                // Display the dealer name in the ComboBox dropdown
                return dealer != null ? dealer.getName() : "";
            }

            @Override
            public Dealer fromString(String name) {
                // Not needed for ComboBox, but required by StringConverter
                return DealerCatalog.getInstance().getDealerWithName(name);
            }
        });

        Button transferButton = new Button("Transfer");
        Button cancelButton = new Button("Cancel");

        // Layout Setup
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                vehicleLabel,
                vehicleDetailLabel,
                dealerLabel,
                dealerComboBox,
                new HBox(10, transferButton, cancelButton)
        );
        layout.setSpacing(15);
        layout.setStyle("-fx-padding: 15; -fx-alignment: center; -fx-font-family: Arial;");

        Scene scene = new Scene(layout, 400, 300);
        wizardStage.setScene(scene);

        // Event Handlers
        transferButton.setOnAction(event -> {
            Dealer receivingDealer = dealerComboBox.getValue();

            // Validation
            if (receivingDealer == null) {
                showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to transfer the vehicle.");
                return;
            }

            // Perform vehicle transfer
            boolean transferSuccessful = currentDealer.transferVehicle(vehicle, receivingDealer);

            if (transferSuccessful) {
                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Transfer Successful",
                        "Vehicle transferred successfully to dealer: " + receivingDealer.getName()
                );
                wizardStage.close();
            } else {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Transfer Failed",
                        "Failed to transfer the vehicle. Please check the dealer and vehicle details."
                );
            }
        });

        cancelButton.setOnAction(event -> wizardStage.close());

        // Show Wizard
        wizardStage.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}