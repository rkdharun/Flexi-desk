module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens flexi to javafx.fxml;
    opens flexi.controller to javafx.fxml;
    exports flexi;
  
}