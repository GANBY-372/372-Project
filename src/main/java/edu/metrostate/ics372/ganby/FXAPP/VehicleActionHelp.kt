package edu.metrostate.ics372.ganby.FXAPP

import edu.metrostate.ics372.ganby.dealer.Dealer
import edu.metrostate.ics372.ganby.dealer.DealerCatalog
import edu.metrostate.ics372.ganby.vehicle.Vehicle
import edu.metrostate.ics372.ganby.vehicle.VehicleBuilder.buildVehicle
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import java.time.LocalDateTime

/**
 * Utility class to perform various vehicle-related operations such as delete, modify price,
 * toggle rent status, and toggle selection in the vehicle table.
 */
object VehicleActionHelper {

    fun deleteSelectedVehicles(
        vehicleTable: TableView<Vehicle>,
        dealerTable: TableView<Dealer?>,
        vehicleObservableList: ObservableList<Vehicle?>
    ) {
        val toDelete = vehicleTable.items.filter { obj: Vehicle -> obj.isSelected() }

        if (toDelete.isEmpty()) {
            FXController.showAlert(
                AlertType.WARNING,
                "No Vehicles Selected",
                "Please select vehicle(s) using the checkbox to delete."
            )
            return
        }

        val confirm = Alert(AlertType.CONFIRMATION)
        confirm.title = "Confirm Deletion"
        confirm.headerText = "Delete " + toDelete.size + " vehicle(s)?"
        confirm.contentText = "Are you sure you want to permanently delete the selected vehicles?"

        confirm.showAndWait().ifPresent { response: ButtonType ->
            if (response == ButtonType.OK) {
                for (vehicle in toDelete) {
                    val dealer = DealerCatalog.getInstance().getDealerWithId(vehicle.getDealerId())
                    dealer?.vehicleCatalog?.remove(vehicle.vehicleId)
                    vehicleObservableList.remove(vehicle)
                }

                vehicleTable.refresh()
                dealerTable.refresh()
                FXController.showAlert(AlertType.INFORMATION, "Deleted", "Selected vehicles deleted successfully.")
            }
        }
    }

    fun modifyVehiclePrice(vehicleTable: TableView<Vehicle?>) {
        val selectedVehicle = vehicleTable.selectionModel.selectedItem
        if (selectedVehicle == null) {
            FXController.showAlert(AlertType.WARNING, "No Vehicle Selected", "Please select a vehicle to modify.")
            return
        }

        val dialog = TextInputDialog(selectedVehicle.getPrice().toString())
        dialog.title = "Modify Price"
        dialog.headerText = "Update vehicle price"
        dialog.contentText = "New price:"

        dialog.showAndWait().ifPresent { input: String ->
            try {
                val newPrice = input.toDouble()
                if (newPrice < 0) throw NumberFormatException("Price must be non-negative.")
                selectedVehicle.setPrice(newPrice)
                vehicleTable.refresh()
                FXController.showAlert(AlertType.INFORMATION, "Updated", "Price updated to $$newPrice")
            } catch (e: NumberFormatException) {
                FXController.showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid price.")
            }
        }
    }

    fun toggleRentStatus(vehicleTable: TableView<Vehicle>, toggleButton: Button) {
        val selectedVehicles = vehicleTable.items.filter { obj: Vehicle -> obj.isSelected() }

        if (selectedVehicles.isEmpty()) {
            FXController.showAlert(
                AlertType.WARNING,
                "No Vehicle Selected",
                "Please select vehicle(s) to change rent status."
            )
            return
        }

        var allRentedOut = true
        var hasNonSportsCar = false

        // Determine if all non-sportscars are rented out
        for (vehicle in selectedVehicles) {
            val type = vehicle.type.trim { it <= ' ' }.replace("\\s+".toRegex(), "")
            val isSportsCar = type.equals("SportsCar", ignoreCase = true)

            if (!isSportsCar && !vehicle.getIsRentedOut()) {
                allRentedOut = false
            }

            if (!isSportsCar) {
                hasNonSportsCar = true
            }
        }

        // Determine the new status we want to set
        val newRentStatus = !allRentedOut // true = rent out, false = make available

        if (!hasNonSportsCar && newRentStatus) {
            FXController.showAlert(
                AlertType.WARNING, "Action Not Allowed",
                "All selected vehicles are SportsCars, which cannot be rented."
            )
            return
        }

        // Update rent status
        for (vehicle in selectedVehicles) {
            val type = vehicle.type.trim { it <= ' ' }.replace("\\s+".toRegex(), "")
            val isSportsCar = type.equals("SportsCar", ignoreCase = true)

            if (isSportsCar && newRentStatus) {
                // Only show warning if we're trying to rent out a SportsCar
                FXController.showAlert(
                    AlertType.WARNING,
                    "Action Not Allowed For Vehicle Id #" + vehicle.vehicleId,
                    "SportsCars cannot be rented."
                )
                continue
            }

            vehicle.setRentedOut(newRentStatus)
        }

        vehicleTable.refresh()
        toggleButton.text = if (newRentStatus) "Set All as Available" else "Set All as Rented"
    }

    fun toggleSelectAllVehicles(vehicleTable: TableView<Vehicle>) {
        val allSelected = vehicleTable.items.all { obj: Vehicle -> obj.isSelected() }

        // Toggle each vehicle's checkbox value
        for (vehicle in vehicleTable.items) {
            vehicle.setSelected(!allSelected)
        }

        // Force refresh of the TableView to reflect changes
        vehicleTable.refresh()
    }

    fun openAddVehicleWizard(selectedDealer: Dealer?, vehicleObservableList: ObservableList<Vehicle?>) {
        if (selectedDealer == null) {
            FXController.showAlert(AlertType.WARNING, "No Dealer Selected", "Please select a dealer to add a vehicle.")
            return
        }

        val wizardStage = Stage()
        wizardStage.initModality(Modality.APPLICATION_MODAL)
        wizardStage.title = "Add New Vehicle Wizard"

        // Create form labels and input fields
        val idLabel = Label("Vehicle ID:")
        val idField = TextField()

        val manufacturerLabel = Label("Manufacturer:")
        val manufacturerField = TextField()

        val modelLabel = Label("Model:")
        val modelField = TextField()

        val priceLabel = Label("Price:")
        val priceField = TextField()

        val categoryLabel = Label("Category:")
        val categoryComboBox = ComboBox(FXCollections.observableArrayList("SUV", "Sedan", "Pickup", "SportsCar"))

        val acquisitionDateLabel = Label("Acquisition Date:")
        val acquisitionDatePicker = DatePicker()

        val rentableLabel = Label("Is Rented Out:")
        val rentableCheckBox = CheckBox()

        val saveButton = Button("Save")
        val cancelButton = Button("Cancel")

        // Layout setup
        val gridPane = GridPane()
        gridPane.vgap = 10.0
        gridPane.hgap = 10.0
        gridPane.addRow(0, idLabel, idField)
        gridPane.addRow(1, manufacturerLabel, manufacturerField)
        gridPane.addRow(2, modelLabel, modelField)
        gridPane.addRow(3, priceLabel, priceField)
        gridPane.addRow(4, categoryLabel, categoryComboBox)
        gridPane.addRow(5, acquisitionDateLabel, acquisitionDatePicker)
        gridPane.addRow(6, rentableLabel, rentableCheckBox)

        val buttonBox = HBox(10.0, saveButton, cancelButton)
        val layout = VBox(10.0, gridPane, buttonBox)
        layout.padding = Insets(10.0)

        val scene = Scene(layout, 400.0, 350.0)
        wizardStage.scene = scene

        // Save action
        saveButton.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
            try {
                val id = idField.text.trim { it <= ' ' }
                val manufacturer = manufacturerField.text.trim { it <= ' ' }
                val model = modelField.text.trim { it <= ' ' }
                val category = categoryComboBox.value
                val priceText = priceField.text.trim { it <= ' ' }
                val acquisitionDate = if (acquisitionDatePicker.value != null) acquisitionDatePicker.value.atStartOfDay() else null
                val isRentable = rentableCheckBox.isSelected

                // Validate input
                require(!(id.isEmpty() || manufacturer.isEmpty() || model.isEmpty() || category == null || acquisitionDate == null)) { "All fields must be filled." }

                val price: Double
                try {
                    price = priceText.toDouble()
                    if (price < 0) throw NumberFormatException()
                } catch (e: NumberFormatException) {
                    throw IllegalArgumentException("Price must be a valid non-negative number.")
                }

                // Use VehicleBuilder to create the vehicle
                val type = null
                val newVehicle = buildVehicle(
                    id, model, manufacturer, price,
                    selectedDealer.getId(), category, acquisitionDate, isRentable, type
                )

                requireNotNull(newVehicle) { "Invalid vehicle type selected." }

                // Add vehicle to catalog
                val dealer = DealerCatalog.getInstance().getDealerWithId(selectedDealer.getId())
                dealer.addVehicle(newVehicle)

                // Refresh UI list
                vehicleObservableList.setAll(dealer.vehicleCatalog.values)

                // Show success alert with vehicle details
                val msg = "%s %s (%s) was added to Dealer #%s".formatted(
                    manufacturer, model, category, selectedDealer.getId()
                )
                FXController.showAlert(AlertType.INFORMATION, "Success", msg)

                wizardStage.close()
            } catch (e: Exception) {
                FXController.showAlert(AlertType.ERROR, "Error Adding Vehicle", e.message)
            }
        }

        // Cancel action
        cancelButton.onAction = EventHandler { event: ActionEvent? -> wizardStage.close() }

        wizardStage.showAndWait()
    }
}
