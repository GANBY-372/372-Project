package edu.metrostate.ics372.ganby.contextmenu;


import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import edu.metrostate.ics372.ganby.wizard.TransferVehicleWizard;
import javafx.scene.control.*;

public class VehicleContextMenu {

    private final TableView<Vehicle> vehicleTable;

    public VehicleContextMenu(TableView<Vehicle> vehicleTable) {
        this.vehicleTable = vehicleTable;
    }

    public void addContextMenu() {
        // Create the context menu
        ContextMenu contextMenu = new ContextMenu();

        // Add "Transfer Vehicle" menu item to the context menu
        MenuItem transferVehicleItem = new MenuItem("Transfer Vehicle");
        transferVehicleItem.setOnAction(event -> {
            Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();

            // Check if a vehicle is selected
            if (selectedVehicle == null) {
                showAlert("No Vehicle Selected", "Please select a vehicle to transfer.");
                return;
            }

            // Get the current dealer of the vehicle
            Dealer currentDealer = DealerCatalog.getInstance().getDealerWithId(selectedVehicle.getDealerId());
            if (currentDealer == null) {
                showAlert("Error", "The selected vehicle does not belong to any dealer!");
                return;
            }

            // Open the Transfer Vehicle Wizard
            TransferVehicleWizard wizard = new TransferVehicleWizard(selectedVehicle, currentDealer);
            wizard.show();
        });

        // Add the menu item to the context menu
        contextMenu.getItems().add(transferVehicleItem);

        // Attach the context menu to each row of the vehicle table
        vehicleTable.setRowFactory(tableView -> {
            TableRow<Vehicle> row = new TableRow<>();

            // Bind the context menu to non-empty rows
            row.contextMenuProperty().bind(
                javafx.beans.binding.Bindings.when(row.emptyProperty())
                    .then((ContextMenu) null)  // No context menu for empty rows
                    .otherwise(contextMenu)   // Context menu for valid rows
            );

            return row;
        });

        System.out.println("VehicleContextMenu added to vehicleTable.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}