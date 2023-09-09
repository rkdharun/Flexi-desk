package com.github.rkdharun.flexidesk.controller.ui;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.gui.Message;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;

public class ClientPageFXMLController {
  public Button btnJoin;

  public TextField txtPortNumber;


  //retreives the port for broadcast listening and calls the join function in Application controller
  public void joinNetwork() {
    if(txtPortNumber.getText().equalsIgnoreCase("")){
      Message.show("Please enter a valid port number","warning");
    }
    else{
      int port = Integer.parseInt(txtPortNumber.getText().trim());
      new Thread(new Runnable() {
        @Override
        public void run() {
          MainApp.applicationController.join(port);
        }
      }).start();
    }
  }
}
