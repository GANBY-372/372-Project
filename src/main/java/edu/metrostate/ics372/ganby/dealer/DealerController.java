package edu.metrostate.ics372.ganby.dealer;


import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.contextmenu.TableRightClickDelete;
import edu.metrostate.ics372.ganby.DataProcessing.JSONFileImporter;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class DealerController {

    public AnchorPane dealerDetailPane;

    public VBox vehicleListVBox;
    public Button addVehicleButton;
    public AnchorPane rootPane;

    // Labels
    @FXML
    public Label dealerNameLabel;

    @FXML
    public Label dealerIdLabel;

    @FXML
    public HBox radioButtonBox;

    @FXML
    public RadioButton isBuyingRadioButton;
    public Label isBuyingLabel;


    @FXML
    private TextField dealerIdTextField;

    @FXML
    private TextField dealerNameTextField;




    // DealerTable Components
    @FXML
    private TableView<Dealer> dealerTable;

    // DealerTable Columns
    @FXML
    private TableColumn<Dealer, String> dealerIdColumn;

    @FXML
    private TableColumn<Dealer, String> dealerNameColumn;

    @FXML
    private TableColumn<Dealer, Boolean> isBuyingColumn;

    @FXML
    public TableColumn<Dealer, Integer> dealerInventoryColumn;


    // Dealer's Vehicle TableView and Its' Columns
    @FXML
    public TableView<Vehicle> vehicleTable;

    @FXML
    public TableColumn<Vehicle, String> vehicleIdColumn;

    @FXML
    public TableColumn<String, String> vehicleCategoryColumn;

    @FXML
    public TableColumn<Vehicle, String> vehicleMakeColumn;

    @FXML
    public TableColumn<Vehicle, Double> vehiclePriceColumn;

    @FXML
    public TableColumn<Vehicle, LocalDateTime> acquisitionDateColumn;

    @FXML
    public TableColumn<Vehicle, Boolean> isRentableColumn;


    // Navigation Bar and Its' Components

    @FXML
    public ButtonBar navigationButtonBar;

    @FXML
    public Button addDealerButton;

    @FXML
    public Button exportJSONWizardButton;

    @FXML
    public Button importJSONWizardButton;
    public Button importXMLWizardButton;
    public Button rentalViewButton;
    public Button vehicleViewButton;
    public Button homeViewButton;

    @FXML
    public ButtonBar vehicleFilterButtonBar;

    @FXML
    public Button rentableFilterButton;

    @FXML
    public Button sportsCarFilterButton;

    @FXML
    public Button sedanFilterButton;

    @FXML
    public Button suvFilterButton;

    @FXML
    public Button truckFilterButton;



    private final ObservableList<Dealer> dealerObservableList = FXCollections.observableArrayList();
    private final ObservableList<Vehicle> vehicleObservableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize Table Column Bindings
        System.out.println("Dealer Table: initialization with " +  DealerCatalog.getInstance().getDealerCatalog().values() + " dealers");


        dealerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dealerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        isBuyingColumn.setCellValueFactory(new PropertyValueFactory<>("isBuying"));

        dealerInventoryColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getVehicleCatalog().size())
        );

        loadData();

        // Listener for table row selection
        dealerTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Dealer> observable, Dealer oldValue, Dealer newValue) {
                if (newValue != null) {
                    updateDealerDetailPane(newValue);
                    populateVehicleList(newValue); // Populate vehicle list when a dealer is selected
                }
            }
        });
        TableRightClickDelete.enableRightClickDelete(dealerTable, dealerObservableList, dealer -> dealer.getVehicleCatalog().isEmpty());

//        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//        vehicleCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
//        vehicleMakeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
//        vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
//        acquisitionDateColumn.setCellValueFactory(new PropertyValueFactory<>("acquisitionDate"));
//        isRentableColumn.setCellValueFactory(new PropertyValueFactory<>("isRentable"));
////
//        vehicleTable.setItems(vehicleObservableList); // Set observable list

//        VehicleContextMenu contextMenu = new VehicleContextMenu(vehicleTable);
//        contextMenu.addContextMenu();

//        addDealerButton.setOnAction(event -> openAddDealerWizard());
    }

    private void loadData() {
        System.out.println("Loading Data...");
        dealerObservableList.addAll(DealerCatalog.getInstance().getDealerCatalog().values());
        for (Dealer dealer : dealerObservableList) {
            System.out.println(dealer.getDealerName());
        }
        dealerTable.setItems(dealerObservableList);
    }

    private void updateDealerDetailPane(Dealer selectedDealer) {
        // Clear existing content in dealerDetailPane
        dealerIdTextField.clear();
        dealerNameTextField.clear();

        // Add new content to dealerDetailPane
        dealerIdTextField.setText(String.valueOf(selectedDealer.getDealerId()));
        dealerNameTextField.setText(selectedDealer.getDealerName());
    }

    private void populateVehicleList(Dealer selectedDealer) {
        // Clear any existing vehicles in the observable list
        vehicleObservableList.clear();

        // Add the vehicles for the selected dealer
        for (Vehicle vehicle : selectedDealer.getVehicleCatalog().values()) {
            System.out.println(vehicle.toString());
        }
        vehicleObservableList.addAll(selectedDealer.getVehicleCatalog().values());
        vehicleTable.setItems(vehicleObservableList);
    }

    @FXML
    private void openAddDealerWizard() {
        Stage wizardStage = new Stage();
        wizardStage.initModality(Modality.APPLICATION_MODAL);
        wizardStage.setTitle("Add New Dealer Wizard");

        Label nameLabel = new Label("Dealer Name:");
        TextField nameField = new TextField();
        Label idLabel = new Label("Dealer ID:");
        TextField idField = new TextField(DealerCatalog.getInstance().getDealerCatalog().size() + 1 + "");

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        GridPane gridPane = new GridPane();
        gridPane.addRow(0, idLabel, idField);
        gridPane.addRow(1, nameLabel, nameField);

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        VBox layout = new VBox(10, gridPane, buttonBox);

        Scene scene = new Scene(layout, 400, 200);
        wizardStage.setScene(scene);

        saveButton.setOnAction(event -> {
            try {
                String dealerId = idField.getText();
                String dealerName = nameField.getText();
                if (dealerName.isBlank()) throw new IllegalArgumentException("Dealer Name cannot be empty.");

                Dealer newDealer = new Dealer(dealerId, dealerName);
                dealerObservableList.add(newDealer);
                wizardStage.close();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        });

        cancelButton.setOnAction(event -> wizardStage.close());
        wizardStage.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void importJSONFile(ActionEvent event) {
        try {
            // Create a new stage for the FileChooser
            Stage primaryStage = new Stage();

            // Instantiate the JSONFileImporter and open the FileChooser
            JSONFileImporter jsonFileImporter = new JSONFileImporter(primaryStage);

            // Process the JSON file (parse vehicles and dealers)
            jsonFileImporter.processJSON();

            // Reload the dealer table with updated catalog
            dealerObservableList.setAll(DealerCatalog.getInstance().getDealerCatalog().values());
            dealerTable.setItems(dealerObservableList);

            // Show success message to the user
            showAlert(Alert.AlertType.INFORMATION, "Import Successful", "JSON file successfully imported!");

        } catch (Exception e) {
            // Show error message on failure
            showAlert(Alert.AlertType.ERROR, "Import Failed", "Error importing JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void filterBySportsCar (ActionEvent actionEvent) {
//        vehicleObservableList.clear();
//
//        // Get the selected dealer from the dealer table
//        Dealer dealer = dealerTable.getSelectionModel().getSelectedItem();
//
//        if (dealer == null) {
//            showAlert(
//                Alert.AlertType.WARNING,
//                "No Dealer Selected",
//                "Please select a dealer to filter vehicles."
//            );
//            return;
//        }
//
//        // Get the vehicles of the selected category
//        vehicleObservableList.addAll(dealer.getVehiclesByCategory(VehicleCategory.SPORTS_CAR));
//
//        // Set the table to display the filtered list
//        vehicleTable.setItems(vehicleObservableList);
    }

    @FXML
    public void filterBySUV (ActionEvent actionEvent) {
//        vehicleObservableList.clear();
//
//        // Get the selected dealer from the dealer table
//        Dealer dealer = dealerTable.getSelectionModel().getSelectedItem();
//
//        if (dealer == null) {
//            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to filter vehicles.");
//            return;
//        }
//
//        // Get the vehicles of the selected category
//        vehicleObservableList.addAll(dealer.getVehiclesByCategory(VehicleCategory.SUV));
//
//        // Set the table to display the filtered list
//        vehicleTable.setItems(vehicleObservableList);
    }

    @FXML
    public void filterByTruck (ActionEvent actionEvent) {
//        vehicleObservableList.clear();
//
//        // Get the selected dealer from the dealer table
//        Dealer dealer = dealerTable.getSelectionModel().getSelectedItem();
//
//        if (dealer == null) {
//            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to filter vehicles.");
//            return;
//        }
//
//        // Get the vehicles of the selected category
//        vehicleObservableList.addAll(dealer.getVehiclesByCategory(VehicleCategory.TRUCK));
//
//        // Set the table to display the filtered list
//        vehicleTable.setItems(vehicleObservableList);
    }

    @FXML
    public void filterBySedan (ActionEvent actionEvent) {
//        vehicleObservableList.clear();
//
//        // Get the selected dealer from the dealer table
//        Dealer dealer = dealerTable.getSelectionModel().getSelectedItem();
//
//        if (dealer == null) {
//            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to filter vehicles.");
//            return;
//        }
//
//        // Get the vehicles of the selected category
//        vehicleObservableList.addAll(dealer.getVehiclesByCategory(VehicleCategory.SEDAN));
//
//        // Set the table to display the filtered list
//        vehicleTable.setItems(vehicleObservableList);
    }

    @FXML
    public void getRentables (ActionEvent actionEvent) {
//        vehicleObservableList.clear();
//
//        // Get the selected dealer from the dealer table
//        Dealer dealer = dealerTable.getSelectionModel().getSelectedItem();
//
//        if (dealer == null) {
//            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to filter vehicles.");
//            return;
//        }
//
//        // Get the vehicles of the selected category
//        vehicleObservableList.addAll(dealer.getRentableVehicles());
//
//        // Set the table to display the filtered list
//        vehicleTable.setItems(vehicleObservableList);
    }
    //
    @FXML
    private void openAddVehicleWizard() {
//        // Get the selected dealer
//        Dealer selectedDealer = dealerTable.getSelectionModel().getSelectedItem();
//
//        // If no dealer is selected, show a warning
//        if (selectedDealer == null) {
//            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer to add a vehicle.");
//            return;
//        }
//
//        // Invoke the Add Vehicle Wizard
//        AddVehicleWizard addVehicleWizard = new AddVehicleWizard(selectedDealer);
//        addVehicleWizard.show();
//
//        // Refresh the vehicle table (if applicable)
//        vehicleObservableList.setAll(selectedDealer.getVehicles());
    }

    @FXML
    private void openTransferVehicleWizard() {
//        // Get the selected vehicle
//        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
//
//        if (selectedVehicle == null) {
//            showAlert(Alert.AlertType.WARNING, "No Vehicle Selected", "Please select a vehicle to transfer.");
//            return;
//        }
//
//        // Get the current dealer of the selected vehicle
//        Dealer currentDealer = selectedVehicle.getDealer();
//        if (currentDealer == null) {
//            showAlert(Alert.AlertType.ERROR, "Error", "Selected vehicle does not belong to any dealer!");
//            return;
//        }
//
//        // Launch the Transfer Wizard
//        TransferVehicleWizard transferVehicleWizard = new TransferVehicleWizard(selectedVehicle, currentDealer);
//        transferVehicleWizard.show();
//
//        // Refresh the vehicle list for the current dealer to reflect changes
//        vehicleObservableList.setAll(currentDealer.getVehicles());
    }
}