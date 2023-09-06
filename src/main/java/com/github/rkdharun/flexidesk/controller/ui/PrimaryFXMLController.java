package com.github.rkdharun.flexidesk.controller.ui;

import com.github.rkdharun.flexidesk.MainApp;

import com.github.rkdharun.flexidesk.utilities.FXMLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;


public class PrimaryFXMLController implements Initializable {

  @FXML
  public VBox addressVbox;
  public ImageView img_qrCode;

  public Label lbl_Info;

  @FXML
  private Button btnCreate;

  @FXML
  private Button btnJoin;

  //packgae function for loading FXML Loader

  FXMLoader fl;
  FXMLLoader qrPane;
  @FXML
  Pane qrUI;
  @FXML
  private BorderPane mainBorderPane;

  @FXML
  private void createNetwork() {
    try {
      //get the QR page r
      qrPane = fl.getPage("qrpage");

      // use the PrimaryFXMLController as its controller
      qrPane.setController(this);

      //retrive the Pane (node)
      qrUI = qrPane.load();

      //set status to loading
      lbl_Info.setText("LOADING QR...");

      //insert the loaded page in the main UI
      mainBorderPane.setCenter(qrUI);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    //create the server
    MainApp.applicationController.createServer();

    //set the QR code in the mainUI and update the status
    img_qrCode.setImage(MainApp.applicationController.getServer().getQR());
    lbl_Info.setText("Created network at port : "+MainApp.applicationController.getServer().getPort());
  }

  public void joinNetwork() {
    System.out.println("Hi biro");
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    fl = new FXMLoader();
    btnCreate.setEffect(new Blend(BlendMode.DARKEN));
    btnJoin.setEffect(new Blend(BlendMode.DARKEN));
  }

  public void closeApp(MouseEvent mouseEvent) {
    System.exit(0);
  }
}

