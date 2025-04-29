    module edu.metrostate.ics372.ganby {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;
    requires java.desktop;
    requires java.logging;
    requires kotlin.stdlib;

    opens edu.metrostate.ics372.ganby.vehicle to javafx.base;
    opens edu.metrostate.ics372.ganby to javafx.fxml;
    opens edu.metrostate.ics372.ganby.dealer to javafx.fxml;
    exports edu.metrostate.ics372.ganby;
    exports edu.metrostate.ics372.ganby.vehicle;
    exports edu.metrostate.ics372.ganby.dealer;
    exports edu.metrostate.ics372.ganby.FXAPP;
    opens edu.metrostate.ics372.ganby.FXAPP to javafx.fxml;
    exports edu.metrostate.ics372.ganby.dataprocessing;
    opens edu.metrostate.ics372.ganby.dataprocessing to javafx.fxml;

}