package com.github.rkdharun.flexidesk.controller.ui;

import com.github.rkdharun.flexidesk.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ChatPageController {

  @FXML
  private Button btnSendFile;

  @FXML
  private Button btnSendClip;

  @FXML
  private Button btnReceive;


  @FXML
  private ImageView icon_close;
  public  void sendFile() {
    Stage s = new Stage();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select File");
    fileChooser.showOpenDialog(s);
  }

  public  void closeSession(){
    MainApp.applicationController.resetApplication();
    MainApp.applicationController.revertMainUI();
  }
  @FXML
  private void initialize() {
    //TODO
  }

}
