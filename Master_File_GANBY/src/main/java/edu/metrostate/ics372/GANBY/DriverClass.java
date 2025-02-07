package edu.metrostate.ics372.GANBY;

import edu.metrostate.ics372.GANBY.Catalogs.DealerCatalog;
import edu.metrostate.ics372.GANBY.Dealer.*;
import edu.metrostate.ics372.GANBY.Vehicle.*;
import edu.metrostate.ics372.GANBY.JSON.JSONFileExporter;

import java.time.LocalDateTime;

public class DriverClass {
    public static void main(String[] args) {

        //LocalDateTime eventTime = LocalDateTime.of(2025, 2, 10, 14, 30);
        Vehicle vehicle1 = new Vehicle(123, 500, "SUV",
                "Ford", "Bronco", 35000, LocalDateTime.now());

        Vehicle vehicle2 = new Vehicle(123, 2340, "SportsCar",
                "Lambo", "Aviation", 200000, LocalDateTime.now());

        Vehicle vehicle3 = new Vehicle(123, 4000, "Sedan", "Toyota", "Camry",
                38000, LocalDateTime.now());

        Vehicle vehicle4 = new Vehicle(456, 801, "Pickup", "Dodge", "Ram",
                52000, LocalDateTime.now());

        //Testing Adding Vehicles
        System.out.println("Adding vehicle1: ");
        DealerCatalog.getInstance().addVehicle(vehicle1);

        System.out.println("Adding vehicle1 again: ");
        DealerCatalog.getInstance().addVehicle(vehicle1);

        System.out.println("Adding vehicle2: ");
        DealerCatalog.getInstance().addVehicle(vehicle2);
        System.out.println("Adding vehicle4: ");
        DealerCatalog.getInstance().addVehicle(vehicle4);

        //Testing findVehicleById() method
        System.out.println("Finding vehicle of id 500: ");
        System.out.println(DealerCatalog.getInstance().getDealerCollection().
                get(vehicle1.getDealerId()).findVehicleById(vehicle1.getVehicleId()));

        //Test Get all vehicles for a given dealer
        System.out.println("Getting all vehicles from dealer of id 123: ");
        for (Integer key : DealerCatalog.getInstance().getDealerCollection().get(123).getVehicles().keySet()) {
            System.out.println(DealerCatalog.getInstance().getDealerCollection().get(123).getVehicles().get(key));
        }

        //Testing disabling Acquisition for dealer
        System.out.println("\nDealer Id 123 Acquisition status: " + DealerCatalog.getInstance().getDealerCollection().
                get(123).getVehicleAcquisitionEnabled());
        System.out.println("Disabling:");
        DealerCatalog.getInstance().getDealerCollection().get(123).disableVehicleAcquisition();
        System.out.println("Dealer Id 123 Acquisition status after disabled: " + DealerCatalog.getInstance().getDealerCollection().
                get(123).getVehicleAcquisitionEnabled());

        //Testing enabling Acquisition for dealer
        System.out.println("\nEnabling:");
        DealerCatalog.getInstance().getDealerCollection().get(123).enableVehicleAcquisition();
        System.out.println("Dealer Id 123 Acquisition status after enabled: " + DealerCatalog.getInstance().getDealerCollection().
                get(123).getVehicleAcquisitionEnabled());

        //Testing adding vehicle to a disable dealer
        System.out.println("Disabling Dealer Id 123 again:");
        DealerCatalog.getInstance().getDealerCollection().get(123).disableVehicleAcquisition();
        System.out.println("Adding vehicle to dealer of disabled acquisition status: ");
        DealerCatalog.getInstance().addVehicle(vehicle3);

        //Printing all vehicles from dealer 123 to make sure vehicle3 wasn't added
        System.out.println("Getting all vehicles from dealer of id 123: ");
        for (Integer key : DealerCatalog.getInstance().getDealerCollection().get(123).getVehicles().keySet()) {
            System.out.println(DealerCatalog.getInstance().getDealerCollection().get(123).getVehicles().get(key));
        }

        //Testing that all dealers can be printed out
        System.out.println("Printing all Dealers: ");
        for (Integer key : DealerCatalog.getInstance().getDealerCollection().keySet()) {
            System.out.println(DealerCatalog.getInstance().getDealerCollection().get(key));
        }

        // ** New Test for Exporting JSON File **
        System.out.println("\nTesting JSON File Export for Dealer Id 123...");
        JSONFileExporter jsonFileExporter = new JSONFileExporter();
        jsonFileExporter.exportToFile(123);
        System.out.println("JSON file has been exported for dealer 123.");
    }
}
