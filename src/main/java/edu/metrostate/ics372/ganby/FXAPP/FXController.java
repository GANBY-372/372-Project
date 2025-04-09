/**
 * Controller class for managing dealers and their vehicles in the JavaFX GUI.
 * Handles CRUD operations, filtering, import/export, and table population.
 */

package edu.metrostate.ics372.ganby.FXAPP;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FXController {

    // --- FXML UI Elements ---
    @FXML public AnchorPane rootPane;
    @FXML public AnchorPane dealerDetailPane;
    @FXML public VBox vehicleListVBox;
    @FXML public TableView<Dealer> dealerTable;
    @FXML public TableView<Vehicle> vehicleTable;
    @FXML public TextField dealerIdTextField;
    @FXML public TextField dealerNameTextField;
    @FXML public Label dealerIdLabel;
    @FXML public Label dealerNameLabel;
    @FXML public TableColumn<Dealer, String> dealerIdColumn;
    @FXML public TableColumn<Dealer, String> dealerNameColumn;
    @FXML public TableColumn<Dealer, Boolean> isBuyingColumn;
    @FXML public TableColumn<Dealer, Integer> dealerInventoryColumn;
    @FXML public TableColumn<Vehicle, String> vehicleDealerIdColumn;
    @FXML public TableColumn<Vehicle, String> vehicleIdColumn;
    @FXML public TableColumn<Vehicle, String> vehicleManufacturerColumn;
    @FXML public TableColumn<Vehicle, String> vehicleTypeColumn;
    @FXML public TableColumn<Vehicle, Double> vehiclePriceColumn;
    @FXML public TableColumn<Vehicle, LocalDateTime> acquisitionDateColumn;
    @FXML public TableColumn<Vehicle, Boolean> isRentedOutColumn;
    @FXML public TableColumn<Vehicle, Boolean> vehicleSelectColumn;
    @FXML public Button addDealerButton;
    @FXML public Button addVehicleButton;
    @FXML public Button deleteDealerButton;
    @FXML public Button deleteVehicleButton;
    @FXML public Button toggleRentStatusButton;
    @FXML public Button modifyPriceButton;
    @FXML public Button transferVehicleButton;
    @FXML public Button selectAllDealersButton;
    @FXML public Button toggleAcquisitionButton;
    @FXML public Button allVehiclesButton;
    @FXML public Button rentedOutFilterButton;
    @FXML public Button sportsCarFilterButton;
    @FXML public Button sedanFilterButton;
    @FXML public Button suvFilterButton;
    @FXML public Button truckFilterButton;
    @FXML private MenuItem importJsonMenuItem;
    @FXML private MenuItem importXmlMenuItem;
    @FXML private MenuItem exportJsonMenuItem;
    @FXML private MenuItem exportXmlMenuItem;
    @FXML public TableColumn<Vehicle, String> vehicleModelColumn;
    @FXML public TableColumn<Dealer, Boolean> dealerSelectColumn;
    private final ObservableList<Dealer> dealerObservableList = FXCollections.observableArrayList();
    private final ObservableList<Vehicle> vehicleObservableList = FXCollections.observableArrayList();
    public Button editDealerNameButton;
    private boolean allDealersSelected = false;


    @FXML
    public void initialize() {
        dealerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        vehicleTable.setEditable(true); // Enable editing for checkboxes
        dealerSelectColumn.setEditable(true);
        dealerTable.setEditable(true);

        selectAllDealersButton.setOnAction(this::toggleSelectAllDealers);

        // Dealer table bindings
        dealerSelectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        dealerSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(dealerSelectColumn));
        dealerIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        dealerNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        isBuyingColumn.setCellValueFactory(cellData -> cellData.getValue().isAcquisitionEnabledProperty());
        dealerInventoryColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getVehicleCatalog().size()));

        // Vehicle table bindings
        vehicleDealerIdColumn.setCellValueFactory(new PropertyValueFactory<>("dealerId"));
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        vehicleManufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        acquisitionDateColumn.setCellValueFactory(new PropertyValueFactory<>("acquisitionDate"));
        isRentedOutColumn.setCellValueFactory(new PropertyValueFactory<>("isRentedOut"));

        // ✅ Checkbox column setup for vehicle selection
        vehicleSelectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        vehicleSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(vehicleSelectColumn));
        vehicleSelectColumn.setEditable(true);

        vehicleTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        vehicleTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Vehicle>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Vehicle v : change.getAddedSubList()) {
                        v.setSelected(true);
                    }
                }
            }
        });

        loadData();

        // Dealer selection listener for when you select/de-select dealer checkboxes
        dealerTable.getItems().forEach(dealer -> {
            dealer.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    // When dealer is selected, populate vehicle list
                    updateDealerDetailPane(dealer);
                    populateVehicleList(); // Populate the vehicles of the selected dealers
                } else {
                    // When dealer is deselected, update vehicle list accordingly
                    populateVehicleList(); // Update list without the deselected dealer's vehicles
                }
            });
        });

        // Update dealer details on selection
        dealerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !allDealersSelected) {
                newVal.setSelected(true);
                updateDealerDetailPane(newVal);

                // Collect the selected dealers (whether individual or all selected)
                List<Dealer> selectedDealers = dealerTable.getItems().stream()
                        .filter(Dealer::isSelected)
                        .collect(Collectors.toList());

                // Populate vehicle list with the selected dealers
                populateVehicleList(selectedDealers);

                addVehicleButton.setDisable(false);
            } else if (newVal == null || dealerTable.getSelectionModel().getSelectedItems().isEmpty()) {
                // No dealer selected
                vehicleObservableList.clear(); // Clear the vehicle list when no dealer is selected
                vehicleTable.setItems(vehicleObservableList); // Update the vehicle table to show no vehicles
                addVehicleButton.setDisable(true); // Disable the add vehicle button if no dealer is selected
            }
        });

        // Update rent status button label
        vehicleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                toggleRentStatusButton.setText("Change Rent Status");
            } else {
                toggleRentStatusButton.setText("Set Rent Status");
            }
        });
    }




    /**
     * Populates vehicle list for selected dealers.
     */
    private void populateVehicleList() {
        // Clear previous data
        vehicleObservableList.clear();

        // Loop through dealers to populate their vehicles
        dealerTable.getItems().stream()
                .filter(Dealer::isSelected)  // Only selected dealers
                .forEach(dealer -> vehicleObservableList.addAll(dealer.getVehicleCatalog().values()));

        // Update the vehicle table with the new data
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Populates vehicle list for selected dealers.
     * @param selectedDealers the list of selected dealers
     */
    private void populateVehicleList(List<Dealer> selectedDealers) {
        vehicleObservableList.clear(); // Ensure clean slate

        // Loop through each selected dealer and add their vehicles to the list
        for (Dealer dealer : selectedDealers) {
            if (dealer != null && dealer.getVehicleCatalog() != null) {
                vehicleObservableList.addAll(dealer.getVehicleCatalog().values());
            }
        }

        // Update the vehicle table with the new data
        vehicleTable.setItems(vehicleObservableList);
    }


    /**
     * Populates dealer table from DealerCatalog.
     */
    public void loadData() {
        dealerObservableList.setAll(DealerCatalog.getInstance().getDealers());
        dealerTable.setItems(dealerObservableList);
    }

    /**
     * Updates the dealer info pane.
     * @param dealer the dealer to show in the UI
     */
    private void updateDealerDetailPane(Dealer dealer) {
        dealerIdTextField.setText(dealer.getId());
        dealerNameTextField.setText(dealer.getName());
        toggleAcquisitionButton.setText("Change Acquisition Status");
    }

    /**
     * Shows the vehicles belonging to the selected dealer.
     * @param dealer the selected dealer
     */
    private void populateVehicleList(Dealer dealer) {
        vehicleObservableList.clear(); // <--- Ensure clean slate
        vehicleObservableList.setAll(dealer.getVehicleCatalog().values());
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Shows an alert dialog. Static because multiple classes use this method.
     * @param type the type of alert
     * @param title dialog title
     * @param message dialog content
     */
    public static void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null); // Ensure no blank header
            alert.setContentText((message == null || message.trim().isEmpty()) ? "Operation completed successfully." : message);
            alert.showAndWait();
        });
    }

    //VEHICLE FILTERS:
    /**
     * Filters and displays only Sedan vehicles based on current dealer selection.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterBySedan(ActionEvent e) {
        VehicleActionHelper.filterByType("Sedan", dealerTable.getSelectionModel().getSelectedItem(), allDealersSelected, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Filters and displays only SportsCar vehicles based on current dealer selection.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterBySportsCar(ActionEvent e) {
        VehicleActionHelper.filterByType("SportsCar", dealerTable.getSelectionModel().getSelectedItem(), allDealersSelected, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Filters and displays only Pickup vehicles based on current dealer selection.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterByPickup(ActionEvent e) {
        VehicleActionHelper.filterByType("Pickup", dealerTable.getSelectionModel().getSelectedItem(), allDealersSelected, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Filters and displays only SUV vehicles based on current dealer selection.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterBySUV(ActionEvent e) {
        VehicleActionHelper.filterByType("SUV", dealerTable.getSelectionModel().getSelectedItem(), allDealersSelected, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Filters and displays vehicles that are currently rented out.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterByRentedOut(ActionEvent e) {
        VehicleActionHelper.filterByRented(dealerTable.getSelectionModel().getSelectedItem(), allDealersSelected, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Shows all vehicles for selected dealer or across all dealers.
     *
     * @param event the ActionEvent triggered by button
     */
    @FXML
    public void showAllVehicles(ActionEvent event) {
        Dealer selectedDealer = dealerTable.getSelectionModel().getSelectedItem();

        if (allDealersSelected) {
            vehicleObservableList.setAll(DealerCatalog.getInstance().getAllVehicles());
        } else if (selectedDealer != null) {
            vehicleObservableList.setAll(selectedDealer.getVehicleCatalog().values());
        } else {
            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to view their vehicles.");
            return;
        }

        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Toggles between selecting all dealers and unselecting them.
     * Also shows or clears all vehicles.
     *
     * @param event the ActionEvent triggered by the toggle button
     */
    @FXML
    public void toggleSelectAllDealers(ActionEvent event) {
        DealerActionHelper.toggleSelectAllDealers(
                dealerTable,
                vehicleTable,
                dealerObservableList,
                vehicleObservableList,
                dealerIdTextField,
                dealerNameTextField,
                selectAllDealersButton,
                addVehicleButton
        );
    }
    /**
     * Enables or disables acquisition for selected dealer.
     */
    @FXML
    private void toggleAcquisitionStatus() {
        DealerActionHelper.toggleAcquisitionStatus(dealerTable);
    }

    /**
     * Deletes the selected dealer.
     *
     * @param event the ActionEvent triggered by delete button
     */
    @FXML
    private void deleteDealers(ActionEvent event) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();

        DealerActionHelper.deleteDealers(
                selectedDealers,
                dealerObservableList,
                vehicleObservableList,
                dealerTable,
                vehicleTable
        );
    }


    //VEHICLES MODIFICATIONS
    /**
     * Deletes the selected vehicle from both the UI and catalog using helper.
     *
     * @param actionEvent the ActionEvent triggered by the delete button
     */
    @FXML
    public void deleteSelectedVehicle(ActionEvent actionEvent) {
        VehicleActionHelper.deleteSelectedVehicles(vehicleTable, dealerTable, vehicleObservableList);
    }

    /**
     * Opens dialog to modify price of selected vehicle using helper logic.
     *
     * @param actionEvent the ActionEvent triggered by the modify button
     */
    @FXML
    public void modifyVehiclePrice(ActionEvent actionEvent) {
        VehicleActionHelper.modifyVehiclePrice(vehicleTable);
    }

    /**
     * Toggles rent status of the selected vehicle and updates the label accordingly.
     *
     * @param event the ActionEvent triggered by the toggle button
     */
    @FXML
    public void toggleRentStatus(ActionEvent event) {
        VehicleActionHelper.toggleRentStatus(vehicleTable, toggleRentStatusButton);
    }

    /**
     * Selects or unselects all vehicles in the table based on current state.
     * Delegates logic to VehicleActionHelper
     *
     * @param actionEvent the ActionEvent triggered by the toggle button
     */
    @FXML
    public void toggleSelectAllVehicles(ActionEvent actionEvent) {
        VehicleActionHelper.toggleSelectAllVehicles(vehicleTable);
    }

    //ADDING DEALER AND VEHICLE AND TRANSFERRING VEHICLES
    /**
     * Opens the wizard to add a new dealer.
     * Delegates the logic to the DealerActionHelper class.
     */
    @FXML
    private void openAddDealerWizard() {
        // Launch dealer creation wizard
        DealerActionHelper.openAddDealerWizard(dealerObservableList, (Stage) rootPane.getScene().getWindow());
    }

    /**
     * Opens the wizard to transfer selected vehicles to a different dealer.
     * Delegates the logic to the VehicleActionHelper class.
     *
     * @param event the action event triggered by the Transfer button
     */
    @FXML
    private void openTransferVehicleWizard(ActionEvent event) {
        // Get currently selected dealer and vehicles
        Dealer currentDealer = dealerTable.getSelectionModel().getSelectedItem();
        List<Vehicle> selected = vehicleTable.getSelectionModel().getSelectedItems();

        // Launch transfer wizard
        VehicleActionHelper.openTransferVehicleWizard(
                selected,
                currentDealer,
                vehicleObservableList,
                vehicleTable,
                dealerTable
        );
    }

    /**
     * Opens the wizard to add a new vehicle to the selected dealer.
     * Delegates the logic to the VehicleActionHelper class and refreshes the vehicle list.
     *
     * @param event the action event triggered by the Add Vehicle button
     */
    @FXML
    public void openAddVehicleWizard(ActionEvent event) {
        // Get currently selected dealer
        Dealer selectedDealer = dealerTable.getSelectionModel().getSelectedItem();

        // Launch vehicle creation wizard
        VehicleActionHelper.openAddVehicleWizard(selectedDealer, vehicleObservableList);

        // Refresh the table with new vehicle
        populateVehicleList(selectedDealer);
    }

    /**
     * Opens a dialog to edit the selected dealer's name.
     *
     * @param event the ActionEvent triggered by the button click
     */

    @FXML
    private void editDealerName(ActionEvent event) {
        DealerActionHelper.renameDealer(dealerTable, dealerNameTextField);
    }



    @FXML
    private void handleImport(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();

        if (event.getSource() == importJsonMenuItem) {
            DataIOHelper.importJSON(stage, dealerObservableList, dealerTable);
        } else if (event.getSource() == importXmlMenuItem) {
            DataIOHelper.importXML(stage, dealerObservableList, dealerTable, vehicleObservableList, vehicleTable);
        }
    }

    @FXML
    private void handleExport(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();

        if (event.getSource() == exportJsonMenuItem) {
            DataIOHelper.exportJSON(stage, dealerTable);
        } else if (event.getSource() == exportXmlMenuItem) {
            DataIOHelper.exportXML(stage, dealerTable);
        }
    }
}
