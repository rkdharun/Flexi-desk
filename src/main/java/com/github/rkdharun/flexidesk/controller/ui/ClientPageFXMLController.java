package com.github.rkdharun.flexidesk.controller.ui;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.gui.Message;
import com.github.rkdharun.flexidesk.utilities.FXMLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

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
      Message.show("Please enter a valid port number", "warning");
    } else {
      int port = Integer.parseInt(txtPortNumber.getText().trim());
      MainApp.applicationController.createServer(port); //server creation does not interfere with the UI
      showProgress();
      new Thread(() -> {
        setChatUIOnConnection();
      }).start();
    }
  }

  /**
   * waits for the server creation thread for accepting a client and update the UI accordingly
   */
  private void setChatUIOnConnection() {
    try {
      MainApp.applicationController.serverStartThread.join();
      Platform.runLater(() -> {
        setChatUI();
      });
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * loads the chat page and update the center borderpane
   */
  private void setChatUI() {
    anchorPane.getChildren().clear();
    FXMLLoader f = FXMLoader.getPage("chatPage");
    try {
      AnchorPane ap =  f.load();
      anchorPane.getChildren().addAll(ap.getChildren());
    } catch (IOException e) {
      throw new RuntimeException(e);
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
