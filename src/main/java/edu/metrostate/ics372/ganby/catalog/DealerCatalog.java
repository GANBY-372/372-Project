/**
 * DealerCatalog.java
 * @author B, G
 * This is a singleton class. Maintains a collection of Dealer objects.
 * Used to manage the collection of dealers in the system during runtime.
 * Singleton pattern ensures only one instance of the class is created.
 */

package edu.metrostate.ics372.ganby.catalog;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.Dealers;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;

public class DealerCatalog {

    // Private instance
    private static DealerCatalog instance;
    private final Dealers dealers; // Set<Dealer> as defined in Dealers class

    // Private constructor
    private DealerCatalog() {
        this.dealers = new Dealers(); //dealers = new HashSet<>() catalog of dealers;
    }

    // Public static method to access instance
    public static DealerCatalog getInstance() {
        if (instance == null) {
            instance = new DealerCatalog();
        }
        return instance;
    }

    // Public method to get dealers
    public Dealers getDealers() {
        return dealers;
    }

    //Asked ChatGPT 4.0 to make a clean and formatted output for vehicles
    //This method get all vehicles of a given dealer
    public void getAllVehiclesOfDealer(int id){
        System.out.println("-------------------------------------------------------------");
        System.out.printf("| %-10s | %-15s | %-10s | %-10s | %-15s |\n",
                "Type", "Model", "Manufacturer", "Price", "Acquisition Date");
        System.out.println("-------------------------------------------------------------");
        for (Vehicle v : this.getDealers().findDealerById(id).getVehicles()) {
            System.out.printf("| %-10s | %-15s | %-10s | $%-9.2f | %-15s |\n",
                    v.getClass().getSimpleName(), // Sedan, SUV, etc.
                    v.getModel(),
                    v.getManufacturer(),
                    v.getPrice(),
                    v.getAcquisitionDate());
        }
        System.out.println("-------------------------------------------------------------");
    }

    //Method to print out list of all Dealers
    public void printAllDealers(){
        System.out.println("-------------------------------------------");
        System.out.printf("| %-10s | %-20s |%n", "Dealer ID", "Acquisition Enabled");
        System.out.println("-------------------------------------------");
        for (Dealer dealer : this.getDealers().getDealers()) {
            System.out.println(dealer);
        }
        System.out.println("-------------------------------------------");
    }

    public void enableDealerAcquisition(int id){
        if(this.getDealers().findDealerById(id) == null){
            System.out.println("Dealer does not exist.");
        } else {
            this.getDealers().findDealerById(id).enableVehicleAcquisition(id);
        }
    }

    public void disableDealerAcquisition(int id){
        if(this.getDealers().findDealerById(id) == null){
            System.out.println("Dealer does not exist.");
        } else {
            this.getDealers().findDealerById(id).disableVehicleAcquisition();
        }
    }

    //Asked CHATGPT 4.0 to write this method for me. It formatted it and figured out the logic.
    //Prints all Vehicles in the Catalog sorted by Dealer
    public void printAllVehiclesById() {
        System.out.println("Vehicles by Dealer:");

        for (Dealer dealer : this.getDealers().getDealers()) { //don't worry bout it, it works 😭
            System.out.printf("%nDealer ID #%d:%n", dealer.getId()); // Print Dealer ID above the table

            System.out.println("-------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-15s | %-12s | %-10s | %-20s |%n",
                    "Type", "Model", "Manufacturer", "Price", "Acquisition Date");
            System.out.println("-------------------------------------------------------------------------------");

            for (Vehicle vehicle : dealer.getVehicles()) {
                System.out.printf("| %-10s | %-15s | %-12s | $%-9.2f | %-20s |%n",
                        vehicle.getClass().getSimpleName(),  // Type
                        vehicle.getModel(),                  // Model
                        vehicle.getManufacturer(),           // Manufacturer
                        vehicle.getPrice(),                  // Price (Floating-Point)
                        vehicle.getAcquisitionDate());       // Acquisition Date
            }

            System.out.println("-------------------------------------------------------------------------------");
        }
    }
}