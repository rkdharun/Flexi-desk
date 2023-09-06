module com.github.rkdharun.flexi {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.media;
  requires java.logging;
  requires java.desktop;
  requires com.google.zxing;
  requires org.bouncycastle.pkix;
  requires org.bouncycastle.provider;

  opens com.github.rkdharun.flexidesk to javafx.fxml;


  exports com.github.rkdharun.flexidesk;
  opens com.github.rkdharun.flexidesk.controller.ui to javafx.fxml;
}
