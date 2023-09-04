module com.github.rkdharun.flexi {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.media;
    requires java.logging;
  requires java.desktop;
  requires com.google.zxing;

  opens com.github.rkdharun.flexidesk to javafx.fxml;
  opens com.github.rkdharun.flexidesk.controller to javafx.fxml;

  exports com.github.rkdharun.flexidesk;
}
