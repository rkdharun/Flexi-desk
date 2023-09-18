package com.github.rkdharun.flexidesk.controller.ui;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.gui.Message;
import com.github.rkdharun.flexidesk.gui.Progressbar;
import com.github.rkdharun.flexidesk.utilities.FXMLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;

import java.io.IOException;

public class ClientPageFXMLController {
  @FXML
  private Button btnJoin;
  @FXML
  private TextField txtPortNumber;

  @FXML
  private AnchorPane anchorPane;


  /**
   * Retreives the port for broadcast listening and calls the join function in Application controller
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


  private void showProgress() {
    anchorPane.getChildren().clear();
    ProgressIndicator pi = new ProgressIndicator();
    pi.setLayoutX(230);
    pi.setLayoutY(200);
    anchorPane.getChildren().addAll(pi);
  }

}
