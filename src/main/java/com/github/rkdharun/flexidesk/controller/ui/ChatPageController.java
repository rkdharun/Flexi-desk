package com.github.rkdharun.flexidesk.controller.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ChatPageController {

  @FXML
  private Button btnSendFile;

  @FXML
  private TextField btnSendClip;

  @FXML
  private Button btnReceive;


  public static void sendFile() {
    Stage s = new Stage();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select File");
    fileChooser.showOpenDialog(s);
  }

  @FXML
  private void initialize() {
    //TODO
  }

  public static void main(String[] args) {
        sendFile();
  }
}
