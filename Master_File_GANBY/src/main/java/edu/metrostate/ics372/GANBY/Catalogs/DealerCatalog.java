package edu.metrostate.ics372.GANBY.Catalogs;

import edu.metrostate.ics372.GANBY.Dealer.*;
import edu.metrostate.ics372.GANBY.Vehicle.*;

import java.util.HashMap;


public class DealerCatalog {

    private static DealerCatalog instance;
    private HashMap<Integer, Dealer> dealerCollection;

    private DealerCatalog() {
        dealerCollection = new HashMap<>();
    }

    //Singleton implementation
    public static DealerCatalog getInstance() {
        if (instance == null) {
            instance = new DealerCatalog();
        }
        return instance;
    }



    public boolean addVehicle (Vehicle vehicle)  {

        if(vehicle == null){    //Doesn't add vehicle if it's null
            return false;
        }

        //Checks if dealer exists
        if(!dealerCollection.containsKey(vehicle.getDealerId())){
            //Makes a new dealer and adds it to hashmap
            dealerCollection.put(vehicle.getDealerId(),new Dealer(vehicle.getDealerId()));
            System.out.println("Dealer does not exist. Dealer has been added.");
            //Adds the vehicle to that dealer
            dealerCollection.get(vehicle.getDealerId()).addVehicle(vehicle);
            System.out.println("Vehicle added");
            return true;
        } else{
            //Checks acquisition status, if false then vehicle will not be added
            if(!dealerCollection.get(vehicle.getDealerId()).getVehicleAcquisitionEnabled()){
                System.out.println("Acquisition Disabled for this dealer");
                return false;
            }//adds vehicle to corresponding dealer
            dealerCollection.get(vehicle.getDealerId()).addVehicle(vehicle);
            System.out.println("Vehicle added");
            return true;
        }
    }

    //get dealerCollection HashMap
    public HashMap<Integer, Dealer> getDealerCollection() {
        return dealerCollection;
    }


//
//    public int size() {
//        return dealers.size();
//    }
//
//    public Set<Dealer> getDealers () {
//        return dealers;
//    }
//
//    public void addDealer (Dealer dealer) throws IllegalAccessException {
//        if (dealer == null)
//            throw new IllegalAccessException("Cannot add null dealer to collection");
//        System.out.println("adding " + dealer.toString() + " to the catalog");
//        this.dealers.add(dealer);
//    }
//
//    public void removeDealer (int dealerId) {
//        Dealer dealer = findDealerById(dealerId);
//        if (dealer != null) {
//            dealers.remove(dealer);
//        }
//    }
//
//    public Dealer findDealerById (int id) {
//        for (Dealer dealer : dealers) {
//            if (dealer.getId() == id) return dealer;
//        }
//        return null;
//    }
}
