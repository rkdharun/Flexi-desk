module com.github.rkdharun.flexidesk {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires java.logging;

  requires org.bouncycastle.pkix;
  requires org.bouncycastle.provider;
  requires com.google.zxing;
  requires java.desktop;

  opens com.github.rkdharun.flexidesk to javafx.fxml;
  opens com.github.rkdharun.flexidesk.controller.ui to javafx.fxml;

  exports com.github.rkdharun.flexidesk;

}
