package edu.metrostate.ics372.GANBY.Catalogs;

import edu.metrostate.ics372.GANBY.Dealer.*;
import edu.metrostate.ics372.GANBY.Vehicle.*;
import edu.metrostate.ics372.GANBY.JSON.*;



public class DealerCatalog {

    private static DealerCatalog instance;
    private final Dealers dealers; //Set<Dealer> dealers;

    private DealerCatalog() {
        this.dealers = new Dealers(); //dealers = new HashSet<>();
    }

    public static DealerCatalog getInstance() {
        if (instance == null) {
            instance = new DealerCatalog();
        }
        return instance;
    }

    public Dealers getDealers() {
        return dealers;
    }

//    public static DealerCatalog getInstance () {
//        if (instance == null) {
//            return new DealerCatalog();
//        }
//        return instance;
//    }
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
