package edu.metrostate.ics372.GANBY.Dealer;

import edu.metrostate.ics372.GANBY.Catalogs.*;
import edu.metrostate.ics372.GANBY.Vehicle.*;
import edu.metrostate.ics372.GANBY.JSON.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Dealers {

    private final Set<Dealer> dealers;

    public Dealers () {
        this.dealers = new HashSet<>();
    }

    public int size() {
        return dealers.size();
    }

    public Set<Dealer> getDealers () {
        return dealers;
    }

    public Iterator<Dealer> iterator() { return dealers.iterator();}

    public void addDealer (Dealer dealer) throws IllegalAccessException {
        if (dealer == null)
            throw new IllegalArgumentException("Cannot add null dealer to collection");
        if (findDealerById(dealer.getId()) != null)
            return; //throw new IllegalAccessException("Dealer is already in the collection");
        System.out.println("adding " + dealer.toString() + " to the collection");
        this.dealers.add(dealer);
        System.out.println("added " + dealer.toString() + " collection size: " + dealers.size());
    }

    public void deleteDealer (int dealerId) {
        Dealer dealer = findDealerById(dealerId);
        if (dealer == null)
            throw new IllegalArgumentException("Dealer not found. Cannot remove dealer which does not exist");
        else
            dealers.remove(dealer);
    }

    public Dealer findDealerById (int id) {
        for (Dealer dealer : dealers) {
            if (dealer.getId() == id) return dealer;
        }
        return null;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder().append("Dealers:\n");
        for (Dealer dealer : dealers) {
            sb.append(dealer.toString()).append("\n");
        }
        return sb.toString();
    }
}
