package Vgil;

public class DriverClass {
    public static void main(String[] args) {
        DealershipManagerImpl manager = DealershipManagerImpl.getInstance();
        manager.showAllDealers();

    }
}