/**
 * DealerCatalog.java
 * This is a singleton class. Maintains a collection of Dealer objects.
 * Used to manage the collection of dealers in the system during runtime.
 * Singleton pattern ensures only one instance of the class is created.
 */

package edu.metrostate.ics372.ganby.dealer;

import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class DealerCatalog {

    private static DealerCatalog instance;
    private final ObservableList<Dealer> dealerList;
    private final HashMap<String, Dealer> dealerCatalog;

    /**
     * Private constructor to prevent instantiation
     */
    private DealerCatalog() {
        dealerCatalog = new HashMap<>();
        dealerList = FXCollections.observableArrayList();
    }

    /**
     * Method to get the instance of the DealerCatalog
     * @return instance of DealerCatalog
     */
    public static DealerCatalog getInstance() {
        if (instance == null) {
            instance = new DealerCatalog();
        }
        return instance;
    }

    /**
     * Get all dealers as an observable list
     * @return ObservableList of dealers
     */
    public ObservableList<Dealer> getDealers() {
        return dealerList;
    }

    /**
     * Get the internal map of dealers
     * @return dealerCatalog HashMap
     */
    public HashMap<String, Dealer> getDealerCatalog() {
        return dealerCatalog;
    }

    /**
     * Get a dealer by ID
     * @param id String
     * @return Dealer or null
     */
    public Dealer getDealerWithId(String id) {
        return dealerCatalog.get(id);
    }

    /**
     * Get a dealer by name
     * @param name String
     * @return Dealer or null
     */
    public Dealer getDealerWithName(String name) {
        if (name == null) return null;
        return dealerCatalog.values().stream()
                .filter(d -> d.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all dealers who are actively acquiring vehicles
     * @return ObservableList of dealers
     */
    public ObservableList<Dealer> getDealersWhoAreBuying() {
        return FXCollections.observableArrayList(
                dealerList.stream()
                        .filter(Dealer::isVehicleAcquisitionEnabled)
                        .toList()
        );
    }

    // TODO: Add comments to code that give the basics of what the code is doing, Add Java docs to each method
    public ArrayList<Vehicle> getAllVehicles() {
        ArrayList<Vehicle> allVehicles = new ArrayList<>();
        for (Dealer dealer : dealerCatalog.values()) {
            allVehicles.addAll(dealer.vehicleCatalog.values());
        }
        return allVehicles;
    }

    /**
     * Add a dealer to the catalog
     * @param dealer Dealer
     */
    public void addDealer(Dealer dealer) {
        if (dealer != null && !dealerCatalog.containsKey(dealer.getId())) {
            dealerCatalog.put(dealer.getId(), dealer);
            dealerList.add(dealer);
        }
    }

    /**
     * Add a vehicle and auto-create the dealer if necessary
     * @param vehicle Vehicle
     */
    public void addVehicle(Vehicle vehicle) {
        if (vehicle == null) return;

        String dealerId = vehicle.getDealerId();
        Dealer dealer = dealerCatalog.get(dealerId);

        if (dealer == null) {
            dealer = new Dealer(dealerId);
            addDealer(dealer);
        }

        if (!dealer.isVehicleAcquisitionEnabled()) {
            return;
        }

        if (dealer.vehicleCatalog.containsKey(vehicle.getVehicleId())) {
            return;
        }

        dealer.addVehicle(vehicle);
    }

    /**
     * Add a vehicle and auto-create the dealer if necessary. This method does not check if dealer acquisition is enabled
     * because it will be used by file importers.
     * @param vehicle Vehicle
     */
    public void addVehicleFromFile(Vehicle vehicle) {
        if (vehicle == null) return;

        String dealerId = vehicle.getDealerId();
        Dealer dealer = dealerCatalog.get(dealerId);

        if (dealer == null) {
            dealer = new Dealer(dealerId);
            addDealer(dealer);
        }

        if (dealer.vehicleCatalog.containsKey(vehicle.getVehicleId())) {
            return;
        }

        dealer.addVehicle(vehicle);
    }

    /**
     * Modify the price of a vehicle
     * @param vehicleId String
     * @param price double
     */
    public void modifyVehiclePrice(String vehicleId, double price) {
        for (Dealer dealer : dealerCatalog.values()) {
            if (dealer.vehicleCatalog.containsKey(vehicleId)) {
                dealer.vehicleCatalog.get(vehicleId).setPrice(price);
            }
        }
    }

    /**
     * Get total dealer count
     * @return int
     */
    public int amountOfAllDealers() {
        return dealerCatalog.size();
    }

    /**
     * Get total vehicle count
     * @return int
     */
    public int amountOfAllVehicles() {
        return dealerCatalog.values().stream()
                .mapToInt(d -> d.vehicleCatalog.size())
                .sum();
    }

    /**
     * Enable dealer acquisition by ID
     * @param id dealer id
     */
    public void enableDealerAcquisition(String id) {
        Dealer dealer = dealerCatalog.get(id);
        if (dealer != null) {
            dealer.enableVehicleAcquisition(id);
        }
    }

    /**
     * Disable dealer acquisition by ID
     * @param id dealer id
     */
    public void disableDealerAcquisition(String id) {
        Dealer dealer = dealerCatalog.get(id);
        if (dealer != null) {
            dealer.disableVehicleAcquisition(id);
        }
    }

    /**
     * Get all unique vehicle types in the system
     * @return ArrayList<String>
     */
    public ArrayList<String> getTypes() {
        Set<String> types = new HashSet<>();
        for (Dealer dealer : dealerCatalog.values()) {
            for (Vehicle vehicle : dealer.vehicleCatalog.values()) {
                types.add(vehicle.getType());
            }
        }
        return new ArrayList<>(types);
    }

    /**
     * Get all vehicles by a specified type
     * @param type String
     * @return ArrayList<Vehicle>
     */
    public ArrayList<Vehicle> getVehiclesByType(String type) {
        ArrayList<Vehicle> result = new ArrayList<>();
        for (Dealer dealer : dealerCatalog.values()) {
            for (Vehicle vehicle : dealer.vehicleCatalog.values()) {
                if (vehicle.getType().equalsIgnoreCase(type)) {
                    result.add(vehicle);
                }
            }
        }
        return result;
    }

    // TODO: Rename more specifically, what does it update? complete the javadoc, add comments to code
    public void updateDealer(Dealer updatedDealer) {
        // Find the dealer in the catalog and update the name
        for (Dealer dealer : getDealers()) {
            if (dealer.getId().equals(updatedDealer.getId())) {
                dealer.setName(updatedDealer.getName());
                break;
            }
        }
    }

    /**
     * Transfer Vehicles from one dealer to another
     * @param vehiclesToTransfer list of vehicles to transfer
     * @param newDealerId The id of the dealer to transfer vehicles to
     */
    public void transferInventory(ArrayList<Vehicle> vehiclesToTransfer, String newDealerId){
        for(Vehicle vehicle : vehiclesToTransfer){
            vehicle.setDealerId(newDealerId);
        }

        this.getDealerCatalog().get(newDealerId).addVehicles(vehiclesToTransfer);
    }

    /**
     * Clear the dealer catalog
     */
    public void clear() {
        dealerCatalog.clear();
    }
}
