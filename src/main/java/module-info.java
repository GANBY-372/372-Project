module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}