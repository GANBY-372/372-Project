package Vgil;

public class DealershipManagerImpl implements DealershipManager {
    // Singleton instance, private constructor
    private static DealershipManagerImpl instance;

    // Private constructor to prevent instantiation
    private DealershipManagerImpl() {
        System.out.println("Dealership Manager initialized.");

    }

    // Public method to provide access to the singleton instance
    public static DealershipManagerImpl getInstance() {
        if (instance == null) {
            instance = new DealershipManagerImpl();
        }
        return instance;
    }

    @Override
    public void showAllDealers() {
        // Implementation here
    }

    @Override
    public void enableDealerAcquisitions(String dealerId) {
        // Implementation here
    }

    @Override
    public void disableDealerAcquisitions(String dealerId) {
        // Implementation here
    }

    @Override
    public void exportDealershipInventory(String dealerId) {
        // Implementation here
    }

    @Override
    public void exportAllDealershipInventories() {
        // Implementation here
    }

    @Override
    public void addIncomingVehicle(String vehicleJson) throws Exception {
        // Implementation here
    }

    @Override
    public void showVehicle(Vehicle vehicle) {
        // Implementation here
    }
}