module edu.metrostate.ics372.ganby {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;

    exports edu.metrostate.ics372.ganby;
    exports edu.metrostate.ics372.ganby.dealer to javafx.fxml;  // Export the dealer package to javafx.fxml

    opens edu.metrostate.ics372.ganby to javafx.fxml;

}