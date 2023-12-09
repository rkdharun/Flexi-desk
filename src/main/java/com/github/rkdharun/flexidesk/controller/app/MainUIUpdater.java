package com.github.rkdharun.flexidesk.controller.app;

import com.github.rkdharun.flexidesk.utilities.FXMLoader;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainUIUpdater {
  public static AnchorPane anchorPane;

  /**
   * calling this method and Updating UI when SSL handshake completed
   */
  public static void setChatUIOnConnection() {
    Platform.runLater(() -> {
      setChatUI();
    });
  }

  /**
   * loads the chat page and update the center borderpane
   */
  private static void setChatUI() {
    anchorPane.getChildren().clear();
    FXMLLoader f = FXMLoader.getPage("chatPage");
    try {
      AnchorPane ap = f.load();
      anchorPane.getChildren().addAll(ap.getChildren());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
