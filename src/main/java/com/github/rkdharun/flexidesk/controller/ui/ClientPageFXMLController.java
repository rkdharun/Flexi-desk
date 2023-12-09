package com.github.rkdharun.flexidesk.controller.ui;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.controller.app.MainUIUpdater;
import com.github.rkdharun.flexidesk.guiUtils.Message;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ClientPageFXMLController {

  @FXML
  private TextField txtPortNumber;

  @FXML
  private AnchorPane anchorPane;


  /**
   * Retreives the port for broadcast listening and calls the createServer function in Application controller
   * and updates the progress
   */
  public void joinNetwork() {
    //check for empty string
    if (txtPortNumber.getText().equalsIgnoreCase("")) {
      //Message.show("Please enter a valid port number", "warning");
    } else {
      int port = Integer.parseInt(txtPortNumber.getText().trim());
      MainUIUpdater.anchorPane = anchorPane;
      MainApp.applicationController.createServer(port);//server creation does not interfere with the UI
      showProgress();

    }
  }


  /**
   * shows a progress Indicator in center of BorderPane mainBorderPane
   */
  private void showProgress() {
    anchorPane.getChildren().clear();
    ProgressIndicator pi = new ProgressIndicator();
    pi.setLayoutX(230);
    pi.setLayoutY(200);
    anchorPane.getChildren().addAll(pi);
  }

}
