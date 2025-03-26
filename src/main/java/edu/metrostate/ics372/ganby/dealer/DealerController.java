package edu.metrostate.ics372.ganby.dealer;

import edu.metrostate.ics372.ganby.DataProcessing.XMLFileImporter;
import edu.metrostate.ics372.ganby.contextmenu.TableRightClickDelete;
import edu.metrostate.ics372.ganby.DataProcessing.JSONFileImporter;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import edu.metrostate.ics372.ganby.wizard.AddVehicleWizard;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Controller for Dealer management screen. Supports dealer CRUD operations,
 * vehicle filtering, and inventory interaction. Works with updated JavaFX `Dealer` class.
 */
public class DealerController {

    public AnchorPane dealerDetailPane;
    public VBox vehicleListVBox;
    public Button addVehicleButton;
    public AnchorPane rootPane;

    @FXML public Label dealerNameLabel;
    @FXML public Label dealerIdLabel;
    @FXML public HBox radioButtonBox;
    @FXML public RadioButton isBuyingRadioButton;
    public Label isBuyingLabel;

    @FXML private TextField dealerIdTextField;
    @FXML private TextField dealerNameTextField;

    @FXML private TableView<Dealer> dealerTable;
    @FXML private TableColumn<Dealer, String> dealerIdColumn;
    @FXML private TableColumn<Dealer, String> dealerNameColumn;
    @FXML private TableColumn<Dealer, Boolean> isBuyingColumn;
    @FXML public TableColumn<Dealer, Integer> dealerInventoryColumn;

    @FXML public TableView<Vehicle> vehicleTable;
    @FXML public TableColumn<Vehicle, String> vehicleIdColumn;
    @FXML public TableColumn<String, String> vehicleCategoryColumn;
    @FXML public TableColumn<Vehicle, String> vehicleManufacturerColumn;
    @FXML public TableColumn<Vehicle, Double> vehiclePriceColumn;
    @FXML public TableColumn<Vehicle, LocalDateTime> acquisitionDateColumn;
    @FXML public TableColumn<Vehicle, Boolean> isRentableColumn;

    @FXML public ButtonBar navigationButtonBar;
    @FXML public Button addDealerButton;
    @FXML public Button exportJSONWizardButton;
    @FXML public Button importJSONWizardButton;
    public Button importXMLWizardButton;
    public Button rentalViewButton;
    public Button vehicleViewButton;
    public Button homeViewButton;

    @FXML public ButtonBar vehicleFilterButtonBar;
    @FXML public Button rentableFilterButton;
    @FXML public Button sportsCarFilterButton;
    @FXML public Button sedanFilterButton;
    @FXML public Button suvFilterButton;
    @FXML public Button truckFilterButton;

    private final ObservableList<Dealer> dealerObservableList = FXCollections.observableArrayList();
    private final ObservableList<Vehicle> vehicleObservableList = FXCollections.observableArrayList();

    /**
     * Initializes controller and sets up table bindings.
     */
    @FXML
    public void initialize() {
        // Dealer table bindings
        dealerIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        dealerNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        isBuyingColumn.setCellValueFactory(cellData -> cellData.getValue().isAcquisitionEnabledProperty());
        dealerInventoryColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getVehicleCatalog().size()));

        // ✅ Vehicle table bindings
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        vehicleManufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        vehicleCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        acquisitionDateColumn.setCellValueFactory(new PropertyValueFactory<>("acquisitionDate"));
        isRentableColumn.setCellValueFactory(new PropertyValueFactory<>("isRentedOut"));

        // Load data into dealer table
        loadData();

        // Listen for selected dealer
        dealerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateDealerDetailPane(newVal);
                populateVehicleList(newVal);
            }
        });

        // Enable right-click delete only for dealers without vehicles
        TableRightClickDelete.enableRightClickDelete(dealerTable, dealerObservableList, dealer -> dealer.getVehicleCatalog().isEmpty());
    }


    /**
     * Loads dealers from catalog to table.
     */
    private void loadData() {
        dealerObservableList.setAll(DealerCatalog.getInstance().getDealers());
        dealerTable.setItems(dealerObservableList);
    }

    /**
     * Populates input pane with selected dealer's info.
     */
    private void updateDealerDetailPane(Dealer dealer) {
        dealerIdTextField.setText(dealer.getId());
        dealerNameTextField.setText(dealer.getName());
    }

    /**
     * Shows selected dealer's vehicles in table.
     */
    private void populateVehicleList(Dealer dealer) {
        vehicleObservableList.setAll(dealer.getVehicleCatalog().values());
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Opens Add Dealer wizard modal.
     */
    @FXML
    private void openAddDealerWizard() {
        Stage wizardStage = new Stage();
        wizardStage.initModality(Modality.APPLICATION_MODAL);
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
                showAlert(Alert.AlertType.INFORMATION, "Success", "Dealer added successfully!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        });

        cancelButton.setOnAction(e -> wizardStage.close());
        wizardStage.showAndWait();
    }

    /**
     * Displays an alert popup.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Imports dealers and vehicles from JSON.
     */
    @FXML
    private void importJSONFile(ActionEvent event) {
        try {
            Stage primaryStage = new Stage();
            JSONFileImporter importer = new JSONFileImporter(primaryStage);
            importer.processJSON();
            dealerObservableList.setAll(DealerCatalog.getInstance().getDealers());
            dealerTable.setItems(dealerObservableList);
            showAlert(Alert.AlertType.INFORMATION, "Import Successful", "JSON file successfully imported!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Import Failed", "Error importing JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void importXMLFile(ActionEvent event) {
        try {
            Stage primaryStage = new Stage();
            XMLFileImporter xmlImporter = new XMLFileImporter(primaryStage);
            Document doc = xmlImporter.getXmlDocument();
            if (doc != null) {
                xmlImporter.processXML(doc);

                // Refresh UI
                dealerObservableList.setAll(DealerCatalog.getInstance().getDealers());
                dealerTable.setItems(dealerObservableList);
                vehicleObservableList.clear();
                vehicleTable.setItems(vehicleObservableList);

                showAlert(Alert.AlertType.INFORMATION, "Import Successful", "XML file successfully imported!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Import Failed", "Error importing XML file:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void exportData(ActionEvent actionEvent) {
        System.out.println("Exporting...");
    }

    // Vehicle filters below:

    @FXML
    public void filterBySedan(ActionEvent e) {
        filterVehicles("Sedan");
    }

    @FXML
    public void filterBySportsCar(ActionEvent e) {
        filterVehicles("SportsCar");
    }

    @FXML
    public void filterByPickup(ActionEvent e) {
        filterVehicles("Pickup");
    }

    @FXML
    public void filterBySUV(ActionEvent e) {
        filterVehicles("SUV");
    }

    private void filterVehicles(String type) {
        Dealer selectedDealer = dealerTable.getSelectionModel().getSelectedItem();
        if (selectedDealer == null) {
            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to filter vehicles.");
            return;
        }
        vehicleObservableList.setAll(DealerCatalog.getInstance().getVehiclesByType(type));
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Displays vehicles marked as rented out for selected dealer.
     */
    @FXML
    public void getRentables(ActionEvent e) {
        Dealer dealer = dealerTable.getSelectionModel().getSelectedItem();
        if (dealer == null) {
            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to filter vehicles.");
            return;
        }
        vehicleObservableList.setAll(dealer.getRentedOutVehicles());
        vehicleTable.setItems(vehicleObservableList);
    }

    /**
     * Opens the Add Vehicle wizard.
     */
    @FXML
    private void openAddVehicleWizard() {
        Dealer selectedDealer = dealerTable.getSelectionModel().getSelectedItem();

        if (selectedDealer == null) {
            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to add a vehicle.");
            return;
        }

        // Pass vehicleObservableList to the wizard
        AddVehicleWizard addVehicleWizard = new AddVehicleWizard(selectedDealer, vehicleObservableList);
        addVehicleWizard.show();

        // Refresh the table after wizard closes
        populateVehicleList(selectedDealer);
    }

    /**
     * Placeholder for transfer vehicle functionality.
     */
    @FXML
    private void openTransferVehicleWizard() {
        // Implementation coming soon
    }


}
