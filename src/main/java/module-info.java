module org.example.demo {
        requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires json.simple;
    requires static lombok;

  /*  requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
*/
    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}