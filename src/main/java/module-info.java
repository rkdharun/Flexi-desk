module com.github.rkdharun.flexi {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.media;
  requires java.logging;
  requires java.desktop;

  requires org.bouncycastle.pkix;
  requires org.bouncycastle.provider;
  requires com.google.zxing;

  opens com.github.rkdharun.flexidesk to javafx.fxml;


  exports com.github.rkdharun.flexidesk;
  opens com.github.rkdharun.flexidesk.controller.ui to javafx.fxml;
}
