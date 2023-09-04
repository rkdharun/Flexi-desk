package com.github.rkdharun.flexidesk;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


import com.github.rkdharun.flexidesk.utilities.QRGenerator;

public class MainApp extends Application {
  private static Stage stage;
  static double initialY;
  static double initialX;

  public static int APPLICATION_MODE;


  @Override
  public void start(@SuppressWarnings("exports") Stage s) throws IOException {
    stage = s;
    setRoot("primary", "Flexi");
  }

  static void setRoot(String fxml) throws IOException {
    setRoot(fxml, stage.getTitle());
  }

  static void setRoot(String fxml, String title) throws IOException {
    Scene scene = new Scene(loadFXML(fxml));
    scene.setFill(Color.TRANSPARENT);
    scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent me) {
        if (me.getButton() != MouseButton.MIDDLE) {

          stage.getScene().getWindow().setX(me.getScreenX() - initialX);
          stage.getScene().getWindow().setY(me.getScreenY() - initialY);
        }
      }
    });
    scene.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent me) {
        if (me.getButton() != MouseButton.MIDDLE) {
          initialY = me.getSceneY();
          initialX = me.getSceneX();
        }
      }
    });
    stage.setScene(scene);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.setTitle(title);
    stage.show();
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/" + fxml + ".fxml"));
    return fxmlLoader.load();
  }

  public static void main(String[] args) {
    launch(args);

  }

}
