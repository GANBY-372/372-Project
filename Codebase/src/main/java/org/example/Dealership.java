package org.example;

import java.util.ArrayList;

public class Dealership{
    private int dealerID;
    private String dealerName;
    private boolean isAcquisitionEnabled;
    public ArrayList<Vehicle> vehicleList = new ArrayList<>();

    //Constructors
    public Dealership(int dealerID, String dealerName, boolean isAcquisitionEnabled){
        this.dealerID = dealerID;
        this.dealerName = dealerName;
        this.isAcquisitionEnabled = isAcquisitionEnabled;
    }

    //Setter & Getters
    public int getDealerID(){
        return dealerID;
    }
    public String getDealerName(){
        return dealerName;
    }
    public boolean getIsAcquisitionEnabled(){
        return isAcquisitionEnabled;
    }
    public void setDealerID(int dealerID){
        this.dealerID = dealerID;
    }
    public void setDealerName(String dealerName){
        this.dealerName = dealerName;
    }
    public void setIsAcquisitionEnabled(boolean isAcquisitionEnabled){
        this.isAcquisitionEnabled = isAcquisitionEnabled;
    }






}

