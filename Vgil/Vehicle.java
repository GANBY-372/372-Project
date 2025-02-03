package Vgil;

import java.util.Date;

public class Vehicle {

    private String dealershipId;
    private String vehicleType;
    private String vehicleManufacturer;
    private String vehicleModel;
    private String vehicleId;
    private double price;
    private Date acquisitionDate;

    // Default constructor
    public Vehicle() {
    }

    // Full constructor
    public Vehicle(String dealershipId, String vehicleType, String vehicleManufacturer, String vehicleModel,
            String vehicleId, double price, long acquisitionDateMillis) {
        this.dealershipId = dealershipId;
        this.vehicleType = vehicleType;
        this.vehicleManufacturer = vehicleManufacturer;
        this.vehicleModel = vehicleModel;
        this.vehicleId = vehicleId;
        this.price = price;
        this.acquisitionDate = new Date(acquisitionDateMillis);
    }

    // Getters and Setters
    public String getDealershipId() {
        return dealershipId;
    }

    public void setDealershipId(String dealershipId) {
        this.dealershipId = dealershipId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    public void setVehicleManufacturer(String vehicleManufacturer) {
        this.vehicleManufacturer = vehicleManufacturer;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(long acquisitionDateMillis) {
        this.acquisitionDate = new Date(acquisitionDateMillis);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "dealershipId='" + dealershipId + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", vehicleManufacturer='" + vehicleManufacturer + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", price=" + price +
                ", acquisitionDate=" + acquisitionDate +
                '}';
    }

    public static void main(String[] args) {
        // Example usage
        Vehicle vehicle = new Vehicle("12513", "suv", "Ford", "Explorer", "48934j", 20123, 1515354694451L);
        System.out.println(vehicle);
    }
}