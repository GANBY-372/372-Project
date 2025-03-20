# 372 Project

## Readme

### Using This Program
The `UserDriver` class file serves as the user interface. This is a terminal-based interface with a popup window interface for importing and exporting JSON files.  
The menu screen shows numbered options for the user to type in the terminal.  

The program has the following options:  
- Import JSON vehicle inventory file.  
- Enable or disable vehicle acquisition.  
- Export dealer inventory to JSON file.  
- List all vehicles in the catalog.  
- End Program.  

### Assignment  
Build a tracking system for a company that owns multiple car dealerships. Admins will need to be able to record vehicles that arrive at the various dealers.  

## Requirements  
1. Read a file that is in JSON format.  
2. Support 4 different types of vehicles in the input file: **suv, sedan, pickup, and sports car**.  
3. Read and store the **vehicle ID, manufacturer, model, acquisition date, and price** for each entry and associate it with the specified dealer ID.  
4. Read and store the associated metadata for each vehicle.  
5. Support the following commands for each dealer:  
6. Only allow adding incoming vehicles to a dealer that has enabled receiving vehicles.  
7. Keep records for a dealer that has disabled receiving vehicles, but will not allow new incoming vehicles.  
8. Be able to export all vehicles from a dealer into a single JSON file.  
9. Show the list of current vehicles for each dealer.  

## Other  
- Usage of the Java standard libraries or other libraries as part of your program is expected.  
- Include external JAR files with your source when you submit it.  
- Documentation of the software is expected.  
- Include a class diagram & sequence diagram of the **add incoming vehicle** operation.  

## Class Files  

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
- `UserDriver`  

## Input Data Example - JSON File  
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
