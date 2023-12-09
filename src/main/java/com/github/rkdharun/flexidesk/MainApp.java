package com.github.rkdharun.flexidesk;

import com.github.rkdharun.flexidesk.controller.app.ApplicationController;

import com.github.rkdharun.flexidesk.guiUtils.Message;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainApp extends Application {

  public static Stage stage;
  public static BorderPane mainUI;
  public static double initialY;
  public static double initialX;

  public static ApplicationController applicationController;
  public static ExecutorService executorService;


  /**
   *
   * @param fxml fxml file to be loaded
   * @param title title of the stage
   * @throws IOException
   */

  static void setRoot(String fxml, String title) throws IOException {
    mainUI = (BorderPane) loadFXML(fxml);
    Scene scene = new Scene(mainUI);
    scene.setFill(Color.TRANSPARENT);
    stage.setScene(scene);
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

      //Initialize the application controller
    applicationController = new ApplicationController();
    System.out.println("Active Threads  :: "+Thread.activeCount());

    //launches the application
    launch(args);




  }

}
