package com.github.rkdharun.flexidesk.guiUtils;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Progressbar {
  private static Stage stage;
  public  static  void show(String message,String title){
    //create a new stage
    stage = new Stage();

    //set the stage properties
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setTitle(title);

    //create a label and a button
    Label lbl = new Label(message);

    ProgressIndicator pi = new ProgressIndicator();


    //add the label and button to a vbox and set the alignment
    VBox vBox = new VBox();
    vBox.getChildren().addAll(lbl,pi);
    vBox.setAlignment(Pos.CENTER);

    //set the scene and show the stage
    Scene ss = new Scene(vBox);
    stage.setScene(ss);
    stage.showAndWait();

  }

  public static void close(){
    stage.close();
  }
}
