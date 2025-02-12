# 372 Project - Vehicle Inventory Tracking System

## Overview
This program provides a tracking system for a company that owns multiple car dealerships. It allows admins to manage vehicle inventories, record incoming vehicles, and export data in JSON format.

## Using This Program
The `UserDriver` class serves as the user interface. It features a terminal-based menu with popup windows for importing and exporting JSON files.

### Menu Options
The program provides the following functionalities:
1. Import JSON vehicle inventory file.
2. Enable or disable vehicle acquisition.
3. Export dealer inventory to a JSON file.
4. List all vehicles in the catalog.
5. End the program.

## Assignment Requirements
1. Read a file that is in JSON format.
2. Support four different vehicle types: **SUV, Sedan, Pickup, and Sports Car**.
3. Read and store the **vehicle ID, manufacturer, model, acquisition date, and price** for each entry, associating them with the specified dealer ID.
4. Read and store additional metadata for each vehicle.
5. Support the following commands for each dealer:
   - Enable/disable vehicle acquisition.
   - Prevent adding vehicles to dealers with disabled acquisition.
   - Maintain records for dealers even if acquisition is disabled.
   - Export all vehicles from a dealer into a single JSON file.
   - Display the list of current vehicles for each dealer.

## Technical Details
- Java standard libraries and additional external libraries (such as Jackson for JSON processing) are utilized.
- External JAR files should be included in the source submission.
- Proper documentation, including a **class diagram** and a **sequence diagram** for adding a vehicle, is expected.

## Class Structure

### Dealer Management
- `Dealer`
  - `private final id : int`
  - `private isVehicleAcquisitionEnabled : boolean`
- `Dealers`
  - `private final Set<Dealer>`
- `DealerCatalog`
  - `private final id : int`
  - `private final Dealers`

### Vehicle Management
- `Vehicle`
  - `getId : String`
  - `getModel : String`
  - `getManufacturer : String`
  - `getPrice : double`
  - `getDealer : Dealer`
  - `getAcquisitionDate : LocalDateTime`
- `Vehicles`
  - `private final Set<Vehicle>`
- `VehicleCatalog`
  - `private static VehicleCatalog`
  - `private final Vehicles`
- `VehicleCategory`
  - `Pickup`
  - `Sedan`
  - `SportsCar`
  - `SUV`

### JSON Processing
- `JSONFileExporter`
- `JSONFileImporter`
- `JSONFileObjectBuilder`

### Main Classes
- `Main`
- `JSONIOTester`
- `UserDriver`

## Sample JSON Input Data
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
