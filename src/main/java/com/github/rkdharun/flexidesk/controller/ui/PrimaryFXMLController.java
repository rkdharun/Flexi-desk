package com.github.rkdharun.flexidesk.controller.ui;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.utilities.FXMLoader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.github.rkdharun.flexidesk.MainApp.*;


public class PrimaryFXMLController implements Initializable {

  @FXML
  private VBox addressVbox;
  @FXML
  private ImageView img_qrCode;
  @FXML
  private Label lbl_Info;

  @FXML
  public Button btnCreate;

  @FXML
  public Button btnJoin;

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



  /**
   * Loads the qrPage.fxml file at the center in border pane
   */
  @FXML
  private void createQr() {
    MainApp.applicationController.join();

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

      //insert the loaded page in the main UI
      mainBorderPane.setCenter(qrUI);

      //set status to loading
      lbl_Info.setText("LOADING QR...");

      //set the QR code in the mainUI and update the status
      img_qrCode.setImage(MainApp.applicationController.br.getQR());

      lbl_Info.setText("SCAN TO CONNECT 0R USE : " + MainApp.applicationController.br.getBroadcastReceptionPort());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * Loads the clientPage.fxml file at the center in border pane
   */
  public void joinNetwork() {

    MainApp.applicationController.resetApplication();
    //Load client fxml page Loader
    clientPane = fl.getPage("clientPage");

    try {
      //Load the page
      clientUI = clientPane.load();

      //set in the center of the BorderPane (Main UI)
      mainBorderPane.setCenter(clientUI);

    } catch (IOException e) {
      e.printStackTrace();
    }

    btnCreate.setMinHeight(35);
    btnJoin.setMinHeight(40);
  }

  private void handle(MouseEvent me) {
    if (me.getButton() != MouseButton.MIDDLE) {

      stage.getScene().getWindow().setX(me.getScreenX() - initialX);
      stage.getScene().getWindow().setY(me.getScreenY() - initialY);
    }
  }

  private void handle2(MouseEvent me) {
    if (me.getButton() != MouseButton.MIDDLE) {
      initialY = me.getSceneY();
      initialX = me.getSceneX();
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    //FXMLoader for loading fxml pages
    fl = new FXMLoader();
    mainBorderPane.getTop().setOnMousePressed(this::handle2);
    mainBorderPane.getTop().setOnMouseDragged(this::handle);

  }



  //close function for the main Application
  public void closeApp(MouseEvent mouseEvent) {
    //TODO: handle files closing and stuff
    MainApp.applicationController.stopServer();
    MainApp.applicationController.stopClient();
    System.exit(0);
  }
}

