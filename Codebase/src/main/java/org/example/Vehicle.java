package org.example;

import java.util.Date;

public class Vehicle {
    private int vehicle_id;
    private String vehicle_type;
    private String vehicle_model;
    private String vehicle_manufacturer;
    private int price;
    private Date acquisition_date;
    private int dealership_id;

    //Constructor
    public Vehicle(int vehicle_id, String vehicle_type,  String vehicle_model,
                   String vehicle_manufacturer, int price,String dealership_id) {
        this.vehicle_id = vehicle_id;
        this.vehicle_type = vehicle_type;
        this.vehicle_model = vehicle_model;
        this.vehicle_manufacturer = vehicle_manufacturer;
        this.price = price;
        this.acquisition_date = new Date();
        this.dealership_id = Integer.valueOf(dealership_id);
    }

    //Setters & Getters
    public int getVehicle_id() {
        return vehicle_id;
    }
    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }
    public String getVehicle_type() {
        return vehicle_type;
    }
    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }
    public String getVehicle_model() {
        return vehicle_model;
    }
    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }
    public String getVehicle_manufacturer() {
        return vehicle_manufacturer;
    }
    public void setVehicle_manufacturer(String vehicle_manufacturer) {
        this.vehicle_manufacturer = vehicle_manufacturer;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public Date getAcquisition_date() {
        return acquisition_date;
    }
    public void setAcquisition_date(Date acquisition_date) {
        this.acquisition_date = acquisition_date;
    }
    public int getDealership_id() {
        return dealership_id;
    }
    public void setDealership_id(int dealership_id) {
        this.dealership_id = dealership_id;
    }

}

