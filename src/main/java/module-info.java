module com.example.javachatfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.javachatfx to javafx.fxml;
    exports com.example.javachatfx;
}