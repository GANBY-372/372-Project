module edu.metrostate.ics372.ganby {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;

    opens edu.metrostate.ics372.ganby to javafx.fxml;
    exports edu.metrostate.ics372.ganby;
}