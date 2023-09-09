package com.github.rkdharun.flexidesk.controller.ui;

import com.github.rkdharun.flexidesk.MainApp;

import com.github.rkdharun.flexidesk.utilities.FXMLoader;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;


public class PrimaryFXMLController implements Initializable {

  @FXML
  private VBox addressVbox;
  @FXML
  private ImageView img_qrCode;
  @FXML
  private Label lbl_Info;

  @FXML
  private Button btnCreate;

  @FXML
  private Button btnJoin;

  //packgae  loading FXML Loader
  FXMLoader fl;

  FXMLLoader qrPane;

  FXMLLoader clientPane;
  @FXML
  Pane qrUI;

  @FXML
  Pane clientUI;

  @FXML
  private BorderPane mainBorderPane;

  @FXML
  private void createNetwork() {
    //create the server
    MainApp.applicationController.createServer(); //server creation does not interfere with the UI

    //just adds a selection effect for the button
    btnJoin.setMinHeight(35);
    btnCreate.setMinHeight(40);

    //get the page
    qrPane = fl.getPage("qrpage");

    // use the PrimaryFXMLController as its controller
    qrPane.setController(this);

    try {
      //retrive the Pane (node)
      qrUI = qrPane.load();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    //insert the loaded page in the main UI
    mainBorderPane.setCenter(qrUI);

    //set status to loading
    lbl_Info.setText("LOADING QR...");

    //set the QR code in the mainUI and update the status
    img_qrCode.setImage(MainApp.applicationController.getServer().getQR());
    lbl_Info.setText("Created network at port : " + MainApp.applicationController.getServer().getPort());

  }

  /**
   * Loads the clientPage.fxml file at the center in border pane
   */
  public void joinNetwork() {
    MainApp.applicationController.stopServer();
    clientPane = fl.getPage("clientPage");
    try {
      clientUI =clientPane.load();
      mainBorderPane.setCenter(clientUI);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    btnCreate.setMinHeight(35);
    btnJoin.setMinHeight(40);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    //FXMLoader for loading fxml pages
    fl = new FXMLoader();
  }

  public void closeApp(MouseEvent mouseEvent) {
    MainApp.applicationController.stopServer();
    MainApp.applicationController.stopClient();
    System.exit(0);
  }
}

