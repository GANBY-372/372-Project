/**
 * DealerCatalog.java
 * @author Banji, Gil
 * This is a singleton class. Maintains a collection of Dealer objects.
 * Used to manage the collection of dealers in the system during runtime.
 * Singleton pattern ensures only one instance of the class is created.
 */
package edu.metrostate.ics372.ganby.catalog;

import edu.metrostate.ics372.ganby.dealer.Dealers;

public class DealerCatalog {

    // Private instance
    private static DealerCatalog instance;
    private final Dealers dealers; // Set<Dealer> as defined in Dealers class

    // Private constructor
    private DealerCatalog() {
        this.dealers = new Dealers(); //dealers = new HashSet<>() catalog of dealers;
    }

    // Public static method to access instance
    public static DealerCatalog getInstance() {
        if (instance == null) {
            instance = new DealerCatalog();
        }
        return instance;
    }

    // Public method to get dealers
    public Dealers getDealers() {
        return dealers;
    }
}