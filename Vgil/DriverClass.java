package Vgil;

public class DriverClass {
    public static void main(String[] args) {
        DealershipManagerImpl manager = DealershipManagerImpl.getInstance();
        manager.showAllDealers();

        // create a dealer
        testCreateDealer();
    }

    // Tests to run
    // 1. Create a dealer, enable and disable acquisitions
    private static void testCreateDealer() {
        Dealer dealer = new Dealer("12513");
        System.out.println("Dealer ID: " + dealer.getDealerID());
        dealer.enableAcquisition();
        dealer.enableAcquisition();
        dealer.disableAcquisition();
        dealer.disableAcquisition();
    }
}