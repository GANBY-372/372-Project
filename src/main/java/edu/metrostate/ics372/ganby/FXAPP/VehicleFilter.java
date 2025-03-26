package edu.metrostate.ics372.ganby.FXAPP;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Utility class providing filtering methods for vehicle data based on type or rental status.
 */
public class VehicleFilter {

    /**
     * Filters vehicles by their type (e.g., SUV, Sedan, Pickup) based on the dealer selection.
     *
     * @param type                 the vehicle type to filter (e.g., "SUV")
     * @param selectedDealer       the currently selected dealer
     * @param allDealersSelected  true if all dealers are selected
     * @param outputList          the observable list to populate with filtered vehicles
     */
    public static void filterByType(String type, Dealer selectedDealer, boolean allDealersSelected,
                                    ObservableList<Vehicle> outputList) {
        if (allDealersSelected) {
            outputList.setAll(
                    DealerCatalog.getInstance().getAllVehicles().stream()
                            .filter(v -> v.getType().equalsIgnoreCase(type))
                            .toList()
            );
        } else if (selectedDealer != null) {
            outputList.setAll(
                    selectedDealer.getVehicleCatalog().values().stream()
                            .filter(v -> v.getType().equalsIgnoreCase(type))
                            .toList()
            );
        }
    }

    /**
     * Filters and returns all rented-out vehicles based on dealer selection.
     *
     * @param selectedDealer       the currently selected dealer
     * @param allDealersSelected   true if all dealers are selected
     * @param outputList           the observable list to populate with rented vehicles
     */
    public static void filterByRented(Dealer selectedDealer, boolean allDealersSelected,
                                      ObservableList<Vehicle> outputList) {
        List<Vehicle> rentedVehicles;

        if (allDealersSelected) {
            rentedVehicles = DealerCatalog.getInstance().getAllVehicles().stream()
                    .filter(Vehicle::getIsRentedOut)
                    .toList();
        } else if (selectedDealer != null) {
            rentedVehicles = selectedDealer.getRentedOutVehicles();
        } else {
            return;
        }

        outputList.setAll(rentedVehicles);
    }
}
