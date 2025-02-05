package edu.metrostate.ics372.GANBY;

import edu.metrostate.ics372.GANBY.JSON.*;
import edu.metrostate.ics372.GANBY.Catalogs.*;
import edu.metrostate.ics372.GANBY.Dealer.*;
import edu.metrostate.ics372.GANBY.Vehicle.*;

import java.time.LocalDateTime;


public class DriverClass {
    public static void main(String[] args) {
        Dealer dealer1 = new Dealer(123);
        dealer1.setVehicleAcquisitionEnabled();
        LocalDateTime eventTime = LocalDateTime.of(2025, 2, 10, 14, 30);

        SUV Bronco = new SUV("1", "Bronco", "Ford", 45000, dealer1, eventTime);

        int id = dealer1.getId();
        System.out.println(id);
        System.out.println(Bronco);



    }
}
