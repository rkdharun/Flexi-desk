package com.github.rkdharun.flexidesk.controller.ui;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.utilities.FXMLoader;
import com.github.rkdharun.flexidesk.utilities.QRGenerator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;


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
  private void joinNet() {
    MainApp.applicationController.join();

    //just adds a selection effect for the button
    btnJoin.setMinHeight(35);
    btnCreate.setMinHeight(40);

    //get the pageqrPane = FXMLoader.getPage("qrPage");


    // use the PrimaryFXMLController as its controller
    qrPane.setController(this);


    //retrive the Pane (node)
    System.out.println("Inside try to load");
    try {
      qrUI = qrPane.load();
    } catch (IOException e) {
      System.out.println("Cannot Load");
    }
    System.out.println("Loading Bro");

    //insert the loaded page in the main UI
    mainBorderPane.setCenter(qrUI);
    System.out.println("Set panniyachu");
    //set status to loading
    lbl_Info.setText("LOADING QR...");

    //set the QR code in the mainUI and update the status
    img_qrCode.setImage(QRGenerator.getQR(String.valueOf(MainApp.applicationController.br.getBroadcastReceptionPort())));

    lbl_Info.setText("SCAN TO CONNECT 0R USE : " + MainApp.applicationController.br.getBroadcastReceptionPort());

    //update ui on receiving a connection
    Thread temp1 = new Thread(() -> setChatUI());
    temp1.setName("ChatUISetting thread");
    temp1.start();


  }


  /**
   * For changing the BorderPane center to chat UI
   */
  public void setChatUI(){


    Set<Thread> threadSet = Thread.getAllStackTraces().keySet();


    System.out.println("Active Threads:: "+ threadSet.size());

    System.out.println("Waiting For Client Response");
    try {
      MainApp.applicationController.clientJoinThread.join();
    } catch (InterruptedException e) {
      System.out.println(e.getMessage());
    }

    if (MainApp.applicationController.isClientConnected()){
        Platform.runLater(() -> {
          try {
            AnchorPane ap = FXMLoader.getPage("chatPage").load();
            mainBorderPane.setCenter(ap);
          } catch (IOException e) {
            System.out.println(e.getMessage());
          }
        });
      }

  }

  /**
   * Loads the clientPage.fxml file at the center in border pane
   */
  public void createQr() {

    System.out.println("Joining to the network");

    //closes all running connections
    MainApp.applicationController.resetApplication();

    //Load client fxml page Loader
    clientPane = FXMLoader.getPage("clientPage");

    try {

      //Load the page
      clientUI = clientPane.load();
      System.out.println("loaded Client UI");

      //set in the center of the BorderPane (Main UI)
      mainBorderPane.setCenter(clientUI);

    } catch (IOException e) {
      e.printStackTrace();
    }

    btnCreate.setMinHeight(35);
    btnJoin.setMinHeight(40);
    System.out.println("Joined");
  }




  @Override
  public void initialize(URL url, ResourceBundle rb) {

  }


  /**
   * For closing the main window
   * @param mouseEvent
   */
  public void closeApp(MouseEvent mouseEvent) {
    //TODO: handle files closing and stuff
    MainApp.applicationController.stopServer();
    MainApp.applicationController.stopClient();
    System.exit(0);
  }
}

