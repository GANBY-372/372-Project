/**
 * Dealers.java
 * @author B, G
 * This class is a collection of dealers.
 */

package edu.metrostate.ics372.ganby.dealer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Dealers {

    // Set to store unique dealers
    private final Set<Dealer> dealers;

    // Constructor, initializes the set
    public Dealers () {
        this.dealers = new HashSet<>();
    }

    // Return number of dealers in the collection
    public int size() {
        return dealers.size();
    }

    // Return the set of dealers
    public Set<Dealer> getDealers () {
        return dealers;
    }

    // Return an iterator for the set of dealers
    // How to use:
    // Dealers dealers = new Dealers();
    // Iterator<Dealer> dealerIterator = dealers.iterator();
    // while (dealerIterator.hasNext()) {
    //     Dealer dealer = dealerIterator.next();
    //     System.out.println(dealer.toString());
    // }
    public Iterator<Dealer> iterator() { return dealers.iterator();}

    // Add dealer to collection, if not already in the collection
    public void addDealer (Dealer dealer) throws IllegalAccessException {
        if (dealer == null)
            throw new IllegalArgumentException("Cannot add null dealer to collection");
        if (findDealerById(dealer.getId()) != null)
            return; //throw new IllegalAccessException("Dealer is already in the collection");
        System.out.println("adding " + dealer.toString() + " to the collection");
        this.dealers.add(dealer);
        System.out.println("added " + dealer.toString() + " collection size: " + dealers.size());
    }

    // Remove dealer from collection with id
    public void deleteDealer (int dealerId) {
        Dealer dealer = findDealerById(dealerId);
        // If dealer is not found, throw exception
        if (dealer == null)
            throw new IllegalArgumentException("Dealer not found. Cannot remove dealer which does not exist");
        else
            dealers.remove(dealer);
    }

    // Find dealer in collection by id, if not found return null
    public Dealer findDealerById (int id) {
        for (Dealer dealer : dealers) {
            if (dealer.getId() == id) return dealer;
        }
        return null;
    }

    // Return a string representation
    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder().append("Dealers:\n");
        for (Dealer dealer : dealers) {
            sb.append(dealer.toString()).append("\n");
        }
        return sb.toString();
    }
}