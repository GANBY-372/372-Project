package Vgil;

public class DriverClass {
    public static void main(String[] args) {
        DealershipManagerImpl manager = DealershipManagerImpl.getInstance();
        manager.showAllDealers();

        // create a dealer
        Dealer dealer = new Dealer("1234");

        // enable acquisitions
        dealer.enableAcquisition();

        // create vehicle
        Vehicle vehicle = new Vehicle();

        // add vehicle to dealer
        dealer.addIncomingVehicle(vehicle);

    }
}