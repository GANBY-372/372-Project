package edu.metrostate.ics372.ganby.catalog;

import edu.metrostate.ics372.ganby.dealer.Dealers;

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
}