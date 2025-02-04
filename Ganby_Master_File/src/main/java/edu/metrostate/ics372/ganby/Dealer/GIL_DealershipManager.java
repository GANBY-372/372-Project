package Vgil;

import java.util.List;

public interface DealershipManager {

    /**
     * Displays the list of current vehicles for each dealer and their status.
     * Or a message indicating that there are no dealers.
     */
    void showAllDealers();

    /**
     * Enables vehicle acquisitions for a specific dealer.
     *
     * @param dealerID The unique identifier for the dealer.
     */
    void enableDealerAcquisitions(String dealerID);

    /**
     * Disables vehicle acquisitions for a specific dealer.
     *
     * @param dealerID The unique identifier for the dealer.
     */
    void disableDealerAcquisitions(String dealerID);

    /**
     * Exports all vehicles from a specific dealer into a single JSON file.
     * Also outputs the information to the screen.
     *
     * @param dealershipID The unique identifier for the dealership.
     * @return The JSON file containing the inventory.
     */
    String exportDealershipInventory(String dealershipID);

    /**
     * Exports the inventories of all dealerships as a single string (not a JSON
     * file).
     *
     * @return A string representation of all dealership inventories.
     */
    String exportAllDealershipInventories();

    /**
     * Adds an incoming vehicle based on a provided JSON file.
     * Checks if the dealer is accepting vehicles before adding.
     * Throws an exception if the dealership is full and returns the vehicles that
     * were not added.
     *
     * @param vehicleJson The JSON file containing the vehicle information.
     * @return A message indicating the result of the operation.
     * @throws Exception If the dealership is full.
     */
    String addIncomingVehicle(String vehicleJson) throws Exception;

    /**
     * Displays the details of a specific vehicle.
     *
     * @param vehicle The vehicle object to display.
     */
    void showVehicle(Vehicle vehicle);
}
