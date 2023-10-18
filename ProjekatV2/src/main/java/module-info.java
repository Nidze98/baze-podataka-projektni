module com.example.projekatv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.example.projekatv2 to javafx.fxml;
    exports com.example.projekatv2;
}