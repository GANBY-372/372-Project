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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

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
    @FXML public Button exportJSONWizardButton;
    @FXML public Button importJSONWizardButton;
    @FXML public Button importXMLWizardButton;
    @FXML public Button rentedOutFilterButton;
    @FXML public Button sportsCarFilterButton;
    @FXML public Button sedanFilterButton;
    @FXML public Button suvFilterButton;
    @FXML public Button truckFilterButton;

    // --- Data Sources ---
    private final ObservableList<Dealer> dealerObservableList = FXCollections.observableArrayList();
    private final ObservableList<Vehicle> vehicleObservableList = FXCollections.observableArrayList();
    public Button importJsonButton;
    public Button importXmlButton;
    public Button exportDataButton;
    public TableColumn vehicleModelColumn;

    private boolean allDealersSelected = false;
    private boolean suppressSelectionListener = false;

    /**
     * Initializes the controller. Sets up bindings and listeners.
     */
    @FXML
    public void initialize() {
        dealerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Bind dealer table columns
        dealerIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        dealerNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        isBuyingColumn.setCellValueFactory(cellData -> cellData.getValue().isAcquisitionEnabledProperty());
        dealerInventoryColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getVehicleCatalog().size()));

        // Bind vehicle table columns
        vehicleDealerIdColumn.setCellValueFactory(new PropertyValueFactory<>("dealerId"));
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        vehicleManufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        acquisitionDateColumn.setCellValueFactory(new PropertyValueFactory<>("acquisitionDate"));
        isRentedOutColumn.setCellValueFactory(new PropertyValueFactory<>("isRentedOut"));

        loadData();

        // Listener: dealer selection change
        dealerTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Dealer>) change -> {
            if (suppressSelectionListener) return;
            if (allDealersSelected &&
                    dealerTable.getSelectionModel().getSelectedItems().size() < dealerObservableList.size()) {
                allDealersSelected = false;
                selectAllDealersButton.setText("Select All Dealers");
                addVehicleButton.setDisable(false);
                dealerIdTextField.clear();
                dealerNameTextField.clear();
            }
        });

        // Listener: display dealer's vehicles on click
        dealerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !allDealersSelected) {
                updateDealerDetailPane(newVal);
                populateVehicleList(newVal);
                addVehicleButton.setDisable(false);
            }
        });

        // Listener: change rent status button label
        vehicleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                toggleRentStatusButton.setText(newVal.getIsRentedOut() ? "Set as Available" : "Set as Rented");
            } else {
                toggleRentStatusButton.setText("Set Rent Status");
            }
        });

        // Enable right-click delete for dealers with no vehicles
        TableRightClickDelete.enableRightClickDelete(
                dealerTable,
                dealerObservableList,
                dealer -> dealer.getVehicleCatalog().isEmpty()
        );
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
        toggleAcquisitionButton.setText(dealer.getIsVehicleAcquisitionEnabled() ? "Disable Acquisition" : "Enable Acquisition");
    }

    /**
     * Shows the vehicles belonging to the selected dealer.
     * @param dealer the selected dealer
     */
    private void populateVehicleList(Dealer dealer) {
        vehicleObservableList.setAll(dealer.getVehicleCatalog().values());
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Shows an alert dialog.
     * @param type the type of alert
     * @param title dialog title
     * @param content dialog content
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    //VEHICLE FILTERS:
    /**
     * Filters and displays only Sedan vehicles based on current dealer selection.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterBySedan(ActionEvent e) {
        VehicleFilter.filterByType("Sedan", dealerTable.getSelectionModel().getSelectedItem(), allDealersSelected, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Filters and displays only SportsCar vehicles based on current dealer selection.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterBySportsCar(ActionEvent e) {
        VehicleFilter.filterByType("SportsCar", dealerTable.getSelectionModel().getSelectedItem(), allDealersSelected, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Filters and displays only Pickup vehicles based on current dealer selection.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterByPickup(ActionEvent e) {
        VehicleFilter.filterByType("Pickup", dealerTable.getSelectionModel().getSelectedItem(), allDealersSelected, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Filters and displays only SUV vehicles based on current dealer selection.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterBySUV(ActionEvent e) {
        VehicleFilter.filterByType("SUV", dealerTable.getSelectionModel().getSelectedItem(), allDealersSelected, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Filters and displays vehicles that are currently rented out.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterByRentedOut(ActionEvent e) {
        VehicleFilter.filterByRented(dealerTable.getSelectionModel().getSelectedItem(), allDealersSelected, vehicleObservableList);
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
    private void toggleDealerSelection(ActionEvent event) {
        TableView.TableViewSelectionModel<Dealer> selectionModel = dealerTable.getSelectionModel();

        if (!allDealersSelected) {
            // Select all dealers
            suppressSelectionListener = true;
            allDealersSelected = true;
            selectionModel.clearSelection();
            selectionModel.selectAll();
            suppressSelectionListener = false;

            dealerIdTextField.setText("ALL");
            dealerNameTextField.setText("All Dealers Selected");

            vehicleObservableList.setAll(DealerCatalog.getInstance().getAllVehicles());
            vehicleTable.setItems(vehicleObservableList);

            addVehicleButton.setDisable(true);
            selectAllDealersButton.setText("Unselect All Dealers");
        } else {
            // Unselect all
            allDealersSelected = false;
            suppressSelectionListener = true;
            selectionModel.clearSelection();
            suppressSelectionListener = false;

            dealerIdTextField.clear();
            dealerNameTextField.clear();

            vehicleObservableList.clear();
            vehicleTable.setItems(vehicleObservableList);

            addVehicleButton.setDisable(false);
            selectAllDealersButton.setText("Select All Dealers");
        }
    }

    /**
     * Enables or disables acquisition for selected dealer.
     */
    @FXML
    private void toggleAcquisition() {
        Dealer selectedDealer = dealerTable.getSelectionModel().getSelectedItem();
        if (selectedDealer != null) {
            boolean current = selectedDealer.getIsVehicleAcquisitionEnabled();
            if (current) {
                DealerCatalog.getInstance().disableDealerAcquisition(selectedDealer.getId());
            } else {
                DealerCatalog.getInstance().enableDealerAcquisition(selectedDealer.getId());
            }

            updateDealerDetailPane(selectedDealer);
            dealerTable.refresh();
        }
    }



    /**
     * Deletes the selected dealer.
     *
     * @param event the ActionEvent triggered by delete button
     */
    @FXML
    private void deleteSelectedDealer(ActionEvent event) {
        Dealer selected = dealerTable.getSelectionModel().getSelectedItem();
        DealerDeletionHelper.deleteDealer(selected, dealerObservableList, vehicleObservableList, dealerTable, vehicleTable);
    }


    //VEHICLES MODIFICATIONS

    /**
     * Deletes the selected vehicle from both the UI and catalog using helper.
     *
     * @param actionEvent the ActionEvent triggered by the delete button
     */
    @FXML
    public void deleteSelectedVehicle(ActionEvent actionEvent) {
        VehicleActionHelper.deleteSelectedVehicle(vehicleTable, dealerTable, vehicleObservableList);
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
     * Delegates the logic to the WizardHelper class.
     */
    @FXML
    private void openAddDealerWizard() {
        // Launch dealer creation wizard
        WizardHelper.openAddDealerWizard(dealerObservableList, (Stage) rootPane.getScene().getWindow());
    }

    /**
     * Opens the wizard to transfer selected vehicles to a different dealer.
     * Delegates the logic to the WizardHelper class.
     *
     * @param event the action event triggered by the Transfer button
     */
    @FXML
    private void openTransferVehicleWizard(ActionEvent event) {
        // Get currently selected dealer and vehicles
        Dealer currentDealer = dealerTable.getSelectionModel().getSelectedItem();
        List<Vehicle> selected = vehicleTable.getSelectionModel().getSelectedItems();

        // Launch transfer wizard
        WizardHelper.openTransferVehicleWizard(
                selected,
                currentDealer,
                vehicleObservableList,
                vehicleTable,
                dealerTable
        );
    }

    /**
     * Opens the wizard to add a new vehicle to the selected dealer.
     * Delegates the logic to the WizardHelper class and refreshes the vehicle list.
     *
     * @param event the action event triggered by the Add Vehicle button
     */
    @FXML
    public void openAddVehicleWizard(ActionEvent event) {
        // Get currently selected dealer
        Dealer selectedDealer = dealerTable.getSelectionModel().getSelectedItem();

        // Launch vehicle creation wizard
        WizardHelper.openAddVehicleWizard(selectedDealer, vehicleObservableList);

        // Refresh the table with new vehicle
        populateVehicleList(selectedDealer);
    }




    //IO METHODS
    /**
     * Handles the action of importing dealer and vehicle data from a JSON file.
     * This method opens a file chooser dialog, reads the JSON file, updates the dealer list and table.
     *
     * @param event the ActionEvent triggered by clicking the "Import JSON" button
     */
    @FXML
    private void importJSONFile(ActionEvent event) {
        // Get the current application window
        Stage stage = (Stage) rootPane.getScene().getWindow();

        // Delegate the JSON import to the DataIOHelper class
        DataIOHelper.importJSON(stage, dealerObservableList, dealerTable);
    }

    /**
     * Handles the action of importing dealer and vehicle data from an XML file.
     * This method opens a file chooser dialog, reads the XML file, and updates the dealer and vehicle tables.
     *
     * @param event the ActionEvent triggered by clicking the "Import XML" button
     */
    @FXML
    private void importXMLFile(ActionEvent event) {
        // Get the current application window
        Stage stage = (Stage) rootPane.getScene().getWindow();

        // Delegate the XML import to the DataIOHelper class
        DataIOHelper.importXML(stage, dealerObservableList, dealerTable, vehicleObservableList, vehicleTable);
    }

    /**
     * Handles the action of exporting the current dealer catalog to a file.
     * This may prompt the user to choose between export formats (e.g., JSON, XML).
     *
     * @param actionEvent the ActionEvent triggered by clicking the "Export Data" button
     */
    @FXML
    public void exportData(ActionEvent actionEvent) {
        // Get the current application window
        Stage stage = (Stage) rootPane.getScene().getWindow();

        // Delegate the export logic to the DataIOHelper class
        DataIOHelper.exportData(stage);
    }

}
