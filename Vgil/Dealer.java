package Vgil;

import java.util.ArrayList;
import java.util.List;
// import java.io.FileWriter;
// import JSON Object maker

public class Dealer {
    private String dealerID;
    private boolean isAcquisitionEnabled;
    private List<Vehicle> vehicles;

    public Dealer(String dealerID) {
        this.dealerID = dealerID;
        this.isAcquisitionEnabled = false;
        this.vehicles = new ArrayList<>();
        System.out.println("Dealer created with ID: " + dealerID);
    }

    public void addIncomingVehicle(Vehicle vehicle) {
        if (isAcquisitionEnabled) {
            vehicles.add(vehicle);
            System.out.println("Vehicle added to inventory");
        }
    }

    public void enableAcquisition() {
        if (isAcquisitionEnabled) {
            System.out.println("Acquisition is already enabled");
        } else {
            isAcquisitionEnabled = true;
            System.out.println("Acquisition enabled");
        }
    }

    public void disableAcquisition() {
        if (!isAcquisitionEnabled) {
            System.out.println("Acquisition is already disabled");
        } else {
            isAcquisitionEnabled = false;
            System.out.println("Acquisition disabled");
        }
    }

    public void showVehicles() {
        if (vehicles.size() == 0) {
            System.out.println("No vehicles in inventory");
        } else {
            for (Vehicle vehicle : vehicles) {
                System.out.println(vehicle);
            }
        }
    }

    // More methods are needed for the assignment

    // Getters and setters?
}
