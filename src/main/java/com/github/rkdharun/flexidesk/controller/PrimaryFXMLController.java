package com.github.rkdharun.flexidesk.controller;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.constants.AppConstant;



import java.net.URL;
import java.util.ResourceBundle;


import com.github.rkdharun.flexidesk.utilities.FXMLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;




public class PrimaryFXMLController implements Initializable {


  FXMLoader fl ;
  @FXML
  private BorderPane mainBorderPane;

  @FXML
  private ImageView img_qrCode;

  @FXML
  private Button btnCreate;

  @FXML
  private void createNetwork(){
      mainBorderPane.setCenter(fl.getPage("qrpage"));
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
 fl = new FXMLoader();
    // TODO
    System.out.println("HElo");
  }
}
