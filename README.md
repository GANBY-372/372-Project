# 🚗 Vehicle Tracking System — Enhanced Edition

## 🔍 Overview

This Java-based Vehicle Tracking System has been upgraded to include new dealer support, persistent data management, advanced inventory operations, and a feature-rich JavaFX GUI. The enhancements were driven by user feedback and organizational growth, prioritizing usability, scalability, and maintainability. The updated system provides seamless management of vehicle inventories, supports both JSON and XML data formats, and ensures data consistency across sessions.

## 🗓 Development Timeline

| Sprint | Date         | Milestone                                     |
|--------|--------------|-----------------------------------------------|
| 1      | March 12, 2025 | Requirements, Use Cases, Sequence Diagrams   |
| 2      | March 26, 2025 | Full Implementation & Unit Testing           |

---

## ✨ New Features

### 1. **Vehicle Loan Tracking**
- Dealers can mark vehicles as *rented*.
- **Sports cars** are restricted from being rented due to liability concerns.
- The system tracks rental status but **does not handle actual loan agreements**.
- GUI buttons allow toggling rent status for selected vehicles.
  
### 2. **Persistent Data Storage**
- Dealer and vehicle data is automatically saved upon exit.
- Data is stored in JSON format under `src/main/resources/Auto_Save/`.

### 3. **XML Data Import**
- XML support added for onboarding external dealer data.
- Imported XML files must follow the defined structure (see below).
- Includes **dealer name parsing**, price normalization (dollars/pounds), and vehicle type validation.
- Invalid entries are skipped and reported.

### 4. **Inventory Transfers**
- Vehicles can be transferred between dealers through the GUI.
- Transfer wizard allows selecting destination dealer and viewing changes in real-time.

### 5. **Graphical User Interface (JavaFX)**
- Built with JavaFX for responsive UI.
- Supports:
  - Adding/removing dealers or vehicles
  - Editing dealer names
  - Enabling/disabling vehicle acquisition
  - Checkbox-based selection
  - Filtering by vehicle type or rent status
  - Exporting selected dealers
  - Wizard dialogs for guided input

### 6. **Unit Testing**
- Includes JUnit tests for core logic.
- Focused coverage on vehicle creation, rent toggling, transfers, and catalog operations.

---

## 🧱 System Components

### 🔹 Dealer Management
- `Dealer`: Represents a dealership.
- `DealerCatalog`: Singleton managing all dealers and inventory operations.

### 🔹 FXAPP (GUI Logic)
- `FXController`: Handles FXML-bound logic and UI state.
- `DealerActionHelper`: Modular helper for dealer-related operations.
- `VehicleActionHelper`: Handles vehicle logic like pricing, deletion, and selection.
- `DataIOHelper`: Manages JSON/XML import/export via GUI.

### 🔹 Vehicle Models
- `Vehicle`: Abstract base class.
- `Sedan`, `SUV`, `Pickup`, `SportsCar`: Concrete subclasses with specific behavior.
  - *SportsCar* enforces rental restrictions.

### 🔹 Data Handling
- `JSONFileImporter`: Imports dealers from structured JSON.
- `XMLFileImporter`: Parses XML based on the new dealer schema.
- `DataExporter`: Handles exporting full or partial dealer data.

### 🔹 Entry Point
- `FXDriver`: JavaFX application launcher.

---

## 📁 Example XML Import Structure

```xml
<Dealers>
  <Dealer id="485">
    <Name>Wacky Bob’s Automall</Name>
    <Vehicle type="suv" id="848432">
      <Price unit="pounds">17000</Price>
      <Make>Land Rover</Make>
      <Model>Range Rover</Model>
    </Vehicle>
    <Vehicle type="pickup" id="52523">
      <Price unit="dollars">22600</Price>
      <Make>Toyota</Make>
      <Model>Tundra</Model>
    </Vehicle>
    <Vehicle type="sedan" id="151e5dde">
      <Price unit="dollars">36600</Price>
      <Make>Genesis</Make>
      <Model>G70</Model>
    </Vehicle>
    <Vehicle type="sports car" id="ern222">
      <Price unit="dollars">22330</Price>
      <Make>Mazda</Make>
      <Model>Miata</Model>
    </Vehicle>
  </Dealer>
</Dealers>
```

*⚠️ Note: Sports cars will be imported but cannot be rented.*

---

## ✅ Running the Application

1. Build the project using Maven or your IDE.
2. Run the `FXDriver` class to launch the GUI.
3. Interact with dealers and vehicles via the interface.
4. Use import/export buttons to load or persist data.

---

## 🧪 Tests

This project is thoroughly tested using **JUnit 5** to ensure all critical components work as intended. Unit tests are structured around core modules including vehicle types, the dealer catalog, and system behaviors like acquisition toggling and vehicle transfers.

### ✅ Coverage Highlights

#### `DealerCatalogTest`
- Verifies:
  - Singleton instantiation
  - Dealer lookup and validation
  - Vehicle addition and price modification
  - Accurate dealer/vehicle counts
  - Dealer acquisition toggling
  - Vehicle retrieval by type and ID

#### `DealerTest`
- Validates:
  - Dealer ID, name, and acquisition status
  - Vehicle catalog integrity
  - Adding and finding vehicles
  - Dealer equality and hash code consistency

#### `VehicleTest`
- Tests:
  - Accessors and mutators (ID, price, dealer, etc.)
  - Validations (e.g., negative price throws exception)
  - Vehicle equality and string representation
  - Rented out logic and restrictions

#### Specific Vehicle Type Tests
- `SedanTest`, `PickupTest`, `SUVTest`, `SportsCarTest`
  - Ensure each subclass:
    - Returns correct type
    - Properly implements `equals()` and `toString()`
    - Handles rent status and restrictions (e.g., SportsCar cannot be rented)

### 🧪 Run the Tests

To run the tests, use your preferred IDE (like IntelliJ IDEA or Eclipse), or via Maven:

```bash
mvn test
```

Tests are located under `src/test/java/edu/metrostate/ics372/ganby`.

---

## 🛠️ Technologies Used
- Java 17+
- JavaFX 21
- ControlsFX & BootstrapFX for UI enhancement
- JUnit 5 for unit testing
- JSON-simple for file I/O
- XML (DOM) Parsing for data import

---
## 👨‍💻 Authors
- Developed by
-Gil Gaitan
-Abdirahman Hassan
-Nick Babashov
-Banji Lawal
-Yahya Guler


