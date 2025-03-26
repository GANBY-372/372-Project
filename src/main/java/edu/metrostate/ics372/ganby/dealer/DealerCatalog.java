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
                        .filter(Dealer::getIsVehicleAcquisitionEnabled)
                        .toList()
        );
    }

    public ArrayList<Vehicle> getAllVehicles() {
        ArrayList<Vehicle> allVehicles = new ArrayList<>();
        for (Dealer dealer : dealerCatalog.values()) {
            allVehicles.addAll(dealer.getVehicleCatalog().values());
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
            System.out.println("Dealer does not exist. Dealer has been added.");
        }

        if (!dealer.getIsVehicleAcquisitionEnabled()) {
            System.out.println("Acquisition Disabled for dealer id #" + dealerId);
            return;
        }

        if (dealer.getVehicleCatalog().containsKey(vehicle.getVehicleId())) {
            System.out.println("Vehicle id #" + vehicle.getVehicleId() + " already exists in dealer's collection.");
            return;
        }

        dealer.addVehicle(vehicle);
        System.out.println("Vehicle id #" + vehicle.getVehicleId() + " added");
    }

    /**
     * Modify the price of a vehicle
     * @param vehicleId String
     * @param price double
     */
    public void modifyVehiclePrice(String vehicleId, double price) {
        for (Dealer dealer : dealerCatalog.values()) {
            if (dealer.getVehicleCatalog().containsKey(vehicleId)) {
                dealer.getVehicleCatalog().get(vehicleId).setPrice(price);
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
                .mapToInt(d -> d.getVehicleCatalog().size())
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
            for (Vehicle vehicle : dealer.getVehicleCatalog().values()) {
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
            for (Vehicle vehicle : dealer.getVehicleCatalog().values()) {
                if (vehicle.getType().equalsIgnoreCase(type)) {
                    result.add(vehicle);
                }
            }
        }
        return result;
    }
}
