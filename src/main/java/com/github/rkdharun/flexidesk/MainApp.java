package com.github.rkdharun.flexidesk;

import com.github.rkdharun.flexidesk.controller.app.ApplicationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

public class MainApp extends Application {
  private static Stage stage;
  static double initialY;
  static double initialX;
  public static int currentThreadCounts = 0;
  public static ApplicationController applicationController;

  ///from main branc
  public static DatagramSocket broadcastSocket;

  /**
   * handles the mouse drag event on the stage to move the stage
   * @param me
   */
  private static void handle(MouseEvent me) {
    if (me.getButton() != MouseButton.MIDDLE) {

      stage.getScene().getWindow().setX(me.getScreenX() - initialX);
      stage.getScene().getWindow().setY(me.getScreenY() - initialY);
    }
  }

  /**
   * handles the mouse press event on the stage to find the position
   * @param me
   */
  private static void handle2(MouseEvent me) {
    if (me.getButton() != MouseButton.MIDDLE) {
      initialY = me.getSceneY();
      initialX = me.getSceneX();
    }
  }

  /**
   *
   * @param fxml fxml file to be loaded
   * @param title title of the stage
   * @throws IOException
   */

  static void setRoot(String fxml, String title) throws IOException {
    Scene scene = new Scene(loadFXML(fxml));
    scene.setFill(Color.TRANSPARENT);

    scene.setOnMouseDragged(MainApp::handle);
    scene.setOnMousePressed(MainApp::handle2);

    stage.setScene(scene);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.setTitle(title);
    stage.show();
  }

  /**
   * Loads the FXML File
   * @param fxml
   * @return returns the Parent object
   * @throws IOException
   */
  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/" + fxml + ".fxml"));
    return fxmlLoader.load();
  }

  /**
   * Starts the application
   * @param s stage object
   * @throws IOException
   */
  @Override
  public void start(@SuppressWarnings("exports") Stage s) throws IOException {
    stage = s;
    setRoot("primary", "Flexi");
  }

  public static void main(String[] args) {

    applicationController = new ApplicationController();
    try {
      //initializing broadcast socket here to fix the QR generation delay
      broadcastSocket = new DatagramSocket(0);
    } catch (SocketException e) {
      throw new RuntimeException(e);
    }

    //launches the application
    launch(args);

  }

}
