# Vehicle Tracking System - Updated Version

## Project Overview
Following the success of the initial phase of the vehicle tracking application, the project has been expanded to include new features requested by the organization. These enhancements aim to improve functionality, usability, and reliability while ensuring smooth integration with new dealers. The development process will follow a structured approach, starting with defining use cases, requirements, and sequence diagrams, followed by implementation and testing.

### Development Timeline
- **Sprint 1 (March 12, 2025):** Use cases, requirements, sequence diagrams
- **Sprint 2 (March 26, 2025):** Full implementation and unit tests

## New Features & Requirements
1. **Vehicle Loan Tracking**
  - Three new dealers have joined the platform.
  - Some dealers rent out cars temporarily and need to track which cars are on loan.
  - Sports cars are not allowed to be rented due to excessive joyriding.
  - The actual loaning process is managed by separate software (not part of this implementation).

2. **Persistent Data Storage**
  - The system should retain dealer and vehicle data between program restarts.

3. **XML Data Import**
  - New dealers may use an XML format to store vehicle data.
  - The system should support importing vehicle data from XML files.
  - Dealer names should be included and editable in imported records.
  - The external XML data source may have inconsistencies or errors, so data validation is necessary.

4. **Inventory Transfers**
  - Dealers should be able to transfer vehicles between one another.

5. **Graphical User Interface (GUI)**
  - The system should include a user-friendly GUI for easier interaction.

6. **Unit Testing**
  - Comprehensive unit tests should be implemented to validate code reliability and functionality.

## System Components
### Dealer Management
- `Dealer`
- `DealerCatalog`

### FXAPP
- `DataOHelper`
- `DealerActionHelper`
- 'FXController`'

### Vehicle Management
- `Vehicle`
- `Vehicles`
- `VehicleCatalog`
- `VehicleCategory` (Pickup, Sedan, SportsCar, SUV)

### Data Handling
- `JSONFileExporter`
- `JSONFileImporter`
- `JSONFileObjectBuilder`

### Main Classes
- `UserDriver`

## Notes
- Use Java standard libraries where applicable.
- Includes external JAR files in the submission.

## Example of new XML Data Structure for Vehicle Import
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


}
