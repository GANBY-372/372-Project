public interface Command {
   // public String commandName; can't have attributes since this is interface, but the UML has an attribute

    public abstract void addIncomingVehicle(Dealership D, Vehicle V);
    public abstract void enableDealerVehicleAcquisition(Dealership D);
    public abstract void disableDealerVehicleAcquisition(Dealership D);
    //public abstract JSON exportVehicles(Dealership D); JSON type error, need to import some libraries
}
