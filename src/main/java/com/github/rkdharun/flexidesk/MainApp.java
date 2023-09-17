package com.github.rkdharun.flexidesk;

import com.github.rkdharun.flexidesk.controller.app.ApplicationController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApp extends Application {

  public static Stage stage;
  public static double initialY;
  public static double initialX;
  public static int currentThreadCounts = 0;
  public static ApplicationController applicationController;

  /**
   *
   * @param fxml fxml file to be loaded
   * @param title title of the stage
   * @throws IOException
   */

  static void setRoot(String fxml, String title) throws IOException {
    Scene scene = new Scene(loadFXML(fxml));
    scene.setFill(Color.TRANSPARENT);

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

    //Initialize the application controller
    applicationController = new ApplicationController();
    System.out.println("Applciation is starting  :: "+Thread.activeCount()+" "+Thread.currentThread().getStackTrace()[1]);

    //launches the application
    launch(args);
    System.out.println("Applciation is started  :: "+Thread.activeCount()+" "+Thread.currentThread().getStackTrace()[1]);


  }

}
