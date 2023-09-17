package com.github.rkdharun.flexidesk.controller.ui;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.gui.Message;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ClientPageFXMLController {
  public Button btnJoin;
  public TextField txtPortNumber;

  /**
   * Retreives the port for broadcast listening and calls the join function in Application controller
   */
  public void joinNetwork() {
    //check for empty string
    if(txtPortNumber.getText().equalsIgnoreCase("")){
      Message.show("Please enter a valid port number","warning");
    }
    else{
      int port = Integer.parseInt(txtPortNumber.getText().trim());
      MainApp.applicationController.createServer(port); //server creation does not interfere with the UI
    }
  }
}
