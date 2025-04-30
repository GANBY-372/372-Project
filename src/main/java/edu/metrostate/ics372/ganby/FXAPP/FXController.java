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
    public Button setAsAvailableButton;
    public Button setAsRentedButton;
    public Button toggleSelectAllVehiclesButton;
    @FXML private MenuItem importJsonMenuItem;
    @FXML private MenuItem importXmlMenuItem;
    @FXML private MenuItem exportJsonMenuItem;
    @FXML private MenuItem exportXmlMenuItem;
    @FXML public TableColumn<Vehicle, String> vehicleModelColumn;
    @FXML public TableColumn<Dealer, Boolean> dealerSelectColumn;
    private final ObservableList<Dealer> dealerObservableList = FXCollections.observableArrayList();
    private final ObservableList<Vehicle> vehicleObservableList = FXCollections.observableArrayList();
    public Button editDealerNameButton;

    @FXML
    public void initialize() {
        // Setup selection and editing behavior
        dealerTable.setEditable(true);
        dealerSelectColumn.setEditable(true);
        dealerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dealerTable.getSelectionModel().setCellSelectionEnabled(false);

        vehicleTable.setEditable(true);
        vehicleSelectColumn.setEditable(true);
        vehicleTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        vehicleTable.getSelectionModel().setCellSelectionEnabled(false);

        selectAllDealersButton.setOnAction(this::toggleSelectAllDealers);

        // Dealer table bindings
        dealerSelectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        dealerSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(dealerSelectColumn));
        dealerIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        dealerNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        isBuyingColumn.setCellValueFactory(cellData -> cellData.getValue().isAcquisitionEnabledProperty());
        dealerInventoryColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().vehicleCatalog.size()));

        // Vehicle table bindings
        vehicleDealerIdColumn.setCellValueFactory(new PropertyValueFactory<>("dealerId"));
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        vehicleManufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        acquisitionDateColumn.setCellValueFactory(new PropertyValueFactory<>("acquisitionDate"));
        isRentedOutColumn.setCellValueFactory(new PropertyValueFactory<>("isRentedOut"));
        vehicleSelectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        vehicleSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(vehicleSelectColumn));

        // Load data from catalog
        loadData();

        // Initial listener on existing dealers
        dealerObservableList.forEach(dealer -> {
            dealer.selectedProperty().addListener((obs, oldVal, newVal) -> {
                populateVehicleList();
                updateDealerDetailPaneIfSingleSelected();
            });
        });

        // Listener for newly added dealers
        dealerObservableList.addListener((ListChangeListener<Dealer>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Dealer dealer : change.getAddedSubList()) {
                        dealer.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            populateVehicleList();
                            updateDealerDetailPaneIfSingleSelected();
                        });
                    }
                }
            }
        });

        // Disable row selection visuals
        disableRowSelection(dealerTable);
        disableRowSelection(vehicleTable);
    }

    /**
     * Updates the dealer info panel only if one dealer is selected.
     */
    private void updateDealerDetailPaneIfSingleSelected() {
        List<Dealer> selectedDealers = dealerObservableList.stream()
                .filter(Dealer::isSelected)
                .toList();
        if (selectedDealers.size() == 1) {
            updateDealerDetailPane(selectedDealers.getFirst());
        } else {
            dealerIdTextField.clear();
            dealerNameTextField.clear();
        }
    }


    /**
     * Prevents row selection and click-based highlighting. Enables checkbox-only selection.
     *
     * @param table the TableView to make row-clicks inert on
     */
    private <T> void disableRowSelection(TableView<T> table) {
        table.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                // Delay to avoid selection collision with internal JavaFX state
                Platform.runLater(() -> table.getSelectionModel().clearSelection());
                event.consume();
            });
            row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    table.getSelectionModel().clearSelection();
                }
            });
            return row;
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
                .forEach(dealer -> vehicleObservableList.addAll(dealer.vehicleCatalog.values()));

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
            if (dealer != null) {
                vehicleObservableList.addAll(dealer.vehicleCatalog.values());
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
        vehicleObservableList.setAll(dealer.vehicleCatalog.values());
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
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();
        VehicleActionHelper.filterByType("Sedan", selectedDealers, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Filters and displays only SportsCar vehicles based on current dealer selection.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterBySportsCar(ActionEvent e) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();
        VehicleActionHelper.filterByType("SportsCar", selectedDealers, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Filters and displays only Pickup vehicles based on current dealer selection.
     *
     * @param e the ActionEvent triggered by the filter button
     */
    @FXML
    public void filterByPickup(ActionEvent e) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();
        VehicleActionHelper.filterByType("Pickup", selectedDealers, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    @FXML
    public void filterBySUV(ActionEvent e) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();
        VehicleActionHelper.filterByType("SUV", selectedDealers, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    @FXML
    public void filterByRentedOut(ActionEvent e) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();
        VehicleActionHelper.filterByRented(selectedDealers, vehicleObservableList);
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Shows all vehicles for selected dealer or across all dealers.
     *
     * @param event the ActionEvent triggered by button
     */
    @FXML
    public void showAllVehicles(ActionEvent event) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();

        if (selectedDealers.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select at least one dealer to view vehicles.");
            return;
        }

        VehicleActionHelper.showAllVehiclesFromSelectedDealers(selectedDealers, vehicleObservableList);
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

    @FXML
    private void setAsRented(ActionEvent event) {
        VehicleActionHelper.setAsRented(vehicleTable);
    }

    @FXML
    private void setAsAvailable(ActionEvent event) {
        VehicleActionHelper.setAsAvailable(vehicleTable);
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
        // Get dealers with checkboxes selected
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .toList();

        if (selectedDealers.isEmpty()) {
            FXController.showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to add a vehicle.");
            return;
        }

        if (selectedDealers.size() > 1) {
            FXController.showAlert(Alert.AlertType.WARNING, "Multiple Dealers Selected", "Please select only one dealer to add a vehicle.");
            return;
        }

        Dealer selectedDealer = selectedDealers.getFirst();
        VehicleActionHelper.openAddVehicleWizard(selectedDealer, vehicleObservableList);
        populateVehicleList(selectedDealer);  // Refresh after adding
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


    /**
     * Imports dealer and vehicle data from a JSON or XML file.
     *
     * @param event the ActionEvent triggered by the import menu item
     */
    @FXML
    private void handleImport(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();

        if (event.getSource() == importJsonMenuItem) {
            DataIOHelper.importJSON(stage, dealerObservableList, dealerTable);
        } else if (event.getSource() == importXmlMenuItem) {
            DataIOHelper.importXML(stage, dealerObservableList, dealerTable, vehicleObservableList, vehicleTable);
        }
    }

    /**
     * Exports dealer and vehicle data to a JSON or XML file.
     *
     * @param event the ActionEvent triggered by the export menu item
     */
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
