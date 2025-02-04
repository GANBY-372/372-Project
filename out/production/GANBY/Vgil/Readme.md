# GANBY 372 Project Documentation

## Assignment

**Assignment:** Build a tracking system for a company that owns multiple car dealerships. Admins will need to be able to record vehicles that arrive at the various dealers.

### Requirements

1. The software shall read a file that is in JSON format.
2. Support 4 different types of vehicles in the input file: SUV, sedan, pickup, and sports car.
3. The software shall read and store the vehicle ID, manufacturer, model, acquisition date, and price for each entry and associate it with the specified dealer ID.
4. The software shall read and store the associated metadata for each vehicle.
5. The software shall support the following commands for each dealer: add incoming vehicle, enable dealer vehicle acquisition, and disable dealer vehicle acquisition.
6. The software shall only allow adding incoming vehicles to a dealer that has enabled receiving vehicles.
7. The software shall keep records for a dealer that has disabled receiving vehicles, but will not allow new incoming vehicles.
8. The software shall be able to export all vehicles from a dealer into a single JSON file.
9. The software shall show the list of current vehicles for each dealer.

**Note:** Usage of the Java standard libraries or other libraries as part of your program is expected. Include external jar files with your source when you submit it. Documentation of the software is expected. Include a class diagram & sequence diagram of the add incoming vehicle operation.

**Question:** Does vehicle ID uniqueness need to be validated with the entire vehicle database?

## Program Architecture Overview

Organize the code using a layered architecture:

- **Data Layer:** Manages reading and writing data.
- **Service Layer:** Implements business logic, managing dealerships and vehicles.
- **Presentation Layer:** Handles user interactions and commands.

## Interface

### Methods

- `showAllDealers()`: Displays the list of current vehicles for each dealer and status.
- `enableDealerAcquisitions(dealerID)`: Enables dealer vehicle acquisition.
- `disableDealerAcquisitions(dealerID)`: Disables dealer vehicle acquisition.
- `exportDealershipInventory(dealershipID)`: Exports all vehicles from a dealer into a single JSON file.
- `exportAllDealershipInventories()`: Returns all inventories as a string.
- `addIncomingVehicle(JSON file)`: Adds incoming vehicle(s) from a JSON file.
- `showVehicle(Vehicle vehicle)`: Displays information for a single vehicle.

### Use Cases

- **showAllDealers()**
    - **Description:** Display all the dealerships in the records.
    - **Use Case:** Manager wants to check if a dealership is able to take more vehicles.

- **enableDealerAcquisitions(dealerID)**
    - **Description:** Sets the dealer status to “Acquisitions Enabled”.
    - **Use Case:** Enable acquisitions for a dealership.

- **disableDealerAcquisitions(dealerID)**
    - **Description:** Sets the dealer status to “Acquisitions Disabled”.
    - **Use Case:** Disable acquisitions for a dealership.

- **exportDealershipInventory(dealershipID)**
    - **Description:** Returns a JSON file and screen output of a dealership’s inventory.
    - **Use Case:** Manager needs to access the inventory to add to a report.

- **addIncomingVehicle(JSON file)**
    - **Description:** Adds incoming vehicle(s) from a JSON file.
    - **Use Case:** Add vehicles to a dealership’s inventory.

- **showVehicle(Vehicle vehicle)**
    - **Description:** Displays information for a single vehicle.

## Business / Logic

### Methods

- **addIncomingVehicle(JSONvehicle file)**
    - **Description:** Adds vehicles from a JSON file if the dealership is accepting acquisitions.
    - **Process:**
        - Check if the dealer is accepting vehicles.
        - Read JSON file and add vehicles to the dealership records.
        - Return results and handle errors.

- **disableDealerAcquisitions(dealerID) / enableDealerAcquisitions(dealerID)**
    - **Description:** Updates the dealer status.
    - **Process:**
        - Send POST to data layer, update the status.
        - Receive answer: ID updated or error message.

- **exportDealershipInventory(dealershipID)**
    - **Description:** Exports all vehicles from a dealer into a single JSON file.
    - **Process:**
        - Query the data layer.
        - Return JSON file and screen output.

- **exportAllDealershipInventories()**
    - **Description:** Returns all inventories as a string.
    - **Process:**
        - Query the data layer.
        - Return list of dealerships with vehicles nested.

- **showAllDealers()**
    - **Description:** Displays all dealerships.
    - **Process:**
        - Query data layer.
        - Return list of dealer IDs.

### Utility Methods

- **JSONtoJavaVehicle(JSON File)**
    - **Description:** Converts a JSON snippet of a vehicle into a Vehicle object.

- **checkIfDealerIsAccepting(JSON File)**
    - **Description:** Checks if the dealership is accepting new vehicles.

## Data

### Vehicle Input Example

```json
{
    "dealership_id": "12513",
    "vehicle_type": "sedan",
    "vehicle_manufacturer": "Tesla",
    "vehicle_model": "Model 3",
    "vehicle_id": "83883",
    "price": 50444,
    "acquisition_date": 1515354694451
}
```

### Questions

- How will all the dealers be stored? In an ArrayList?
- How will all the vehicles be stored? Also in an ArrayList?
- Where will both of these ArrayLists be stored? Should they be stored in a separate class?
- Should each dealership object contain an ArrayList of Vehicles?
- How will we output data to the interface? Through JSON or System.out?
- Will there be a static class that has all the needed JSON elements and operations?

## Classes and Attributes

### Dealership Class

- **Attributes:**
    - `dealerID` (string): Unique identifier for the dealership.
    - `name` (string): Optional name for the dealership.
    - `location` (string): Optional location metadata.
    - `isAcquisitionEnabled` (boolean): Indicates whether the dealership can receive new vehicles.
    - `vehicles` (List<Vehicle>): List of associated vehicles.
- **Methods:**
    - `addIncomingVehicle(Vehicle vehicle)`: Adds a vehicle if acquisition is enabled.
    - `enableAcquisition()`: Enables receiving vehicles.
    - `disableAcquisition()`: Disables receiving vehicles.
    - `exportVehiclesToJSON()`: Exports all vehicles to a JSON file.

### Vehicle Class

- **Attributes:**
    - `vehicleID` (string): Unique identifier for the vehicle.
    - `manufacturer` (string): Manufacturer name.
    - `model` (string): Model name.
    - `acquisitionDate` (Date): Date the vehicle was acquired.
    - `price` (decimal): Price of the vehicle.
    - `type` (enum): One of SUV, Sedan, Pickup, SportsCar.
    - `metadata` (Map<String, String>): Additional metadata about the vehicle.
- **Methods:**
    - Constructor and accessors for initializing and retrieving vehicle attributes.

### Command Handling

- **Methods:**
    - `addIncomingVehicle(Dealership dealership, Vehicle vehicle)`: Checks if acquisition is enabled before adding.
    - `enableDealerAcquisition(Dealership dealership)`: Enables acquisition for the given dealership.
    - `disableDealerAcquisition(Dealership dealership)`: Disables acquisition but retains vehicle records.
    - `exportVehicles(Dealership dealership)`: Exports the vehicles of the specified dealership to a JSON file.

## Data Example

```json
{
    "Car_inventory": [
        {
            "dealership_id": "12513",
            "vehicle_type": "suv",
            "vehicle_manufacturer": "Ford",
            "vehicle_model": "Explorer",
            "vehicle_id": "48934",
            "price": 20123,
            "acquisition_date": 1515354694451
        },
        {
            "dealership_id": "12513",
            "vehicle_type": "sedan",
            "vehicle_manufacturer": "Tesla",
            "vehicle_model": "Model 3",
            "vehicle_id": "83883",
            "price": 50444,
            "acquisition_date": 1515354694451
        },
        {
            "dealership_id": "12513",
            "vehicle_type": "pickup",
            "vehicle_manufacturer": "Chevy",
            "vehicle_model": "Silverado",
            "vehicle_id": "89343883",
            "price": 70444,
            "acquisition_date": 1515354694451
        },
        {
            "dealership_id": "77338",
            "vehicle_type": "sports car",
            "vehicle_manufacturer": "Toyota",
            "vehicle_model": "Supra",
            "vehicle_id": "229393",
            "price": 49889,
            "acquisition_date": 1515354694451
        }
    ]
}
```

## Tools and Libraries

- **Java Standard Libraries:** Collections, file I/O.
- **External Libraries:**
    - JSON parsing: Jackson or Gson.
    - Date management: `java.time.LocalDate` for acquisition dates.

## User Interface

This layer handles user interaction. It may consist of a command-line interface (CLI) or a graphical user interface (GUI). It calls methods from the service layer to execute user commands. GUI not required for Project 1.

### Responsibilities

- Display information to the user (e.g., list of vehicles, status of dealerships).
- Collect user inputs (e.g., commands and data to add vehicles).
- Delegate tasks to the service layer.

### Example Classes

- **CommandLineInterface:** A class that handles command-line inputs and outputs.
- **Main:** The entry point of the application that initializes components and starts the program.

## Sequence Diagram Example

For the `addIncomingVehicle` operation, the sequence diagram will include all three layers:

1. **User Input:** The user invokes the operation (e.g., entering a command to add a vehicle).
2. **Presentation Layer:** The command (e.g., `addIncomingVehicle(dealerID, vehicle)`) is sent to the service layer.
3. **Service Layer:**
     - Fetches the relevant dealership using the DataRepository.
     - Validates if acquisition is enabled.
     - Adds the vehicle to the dealership.
     - Persists the updated dealership back to the data layer.
4. **Data Layer:** Reads the dealership data from the JSON file, updates it, and saves it back.

### Sequence Diagram Steps

1. User inputs the command `AddIncomingVehicle` with parameters (e.g., dealerID, vehicle details).
2. `CommandLineInterface` calls `DealershipService.addIncomingVehicle()`.
3. `DealershipService` calls `DataRepository.getDealershipById(dealerID)` to retrieve the dealership.
4. `DealershipService` validates acquisition rules and updates the dealership.
5. `DealershipService` calls `DataRepository.saveDealership()` to persist the updated data.
6. `JsonDataHandler` handles writing the updated data back to the JSON file.
