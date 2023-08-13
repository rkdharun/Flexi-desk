module com.github.rkdharun.flexi {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.media;

  opens com.github.rkdharun.flexidesk to javafx.fxml;
  opens com.github.rkdharun.flexidesk.controller to javafx.fxml;

  exports com.github.rkdharun.flexidesk;
}
