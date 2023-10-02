package com.github.rkdharun.flexidesk.guiUtils;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is used to show the message in a new stage with a button to close the stage
 */
public class Message {

  /**
   * static show method to display a custom message or warning to the user
   * @param message custom message the user wants to display
   * @param title title of the stage or related heading to the message
   */
  public  static  void show(String message,String title){
    //create a new stage
    Stage stage = new Stage();

    //set the stage properties
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setTitle(title);

    //create a label and a button
    Label lbl = new Label(message);
    Button close = new Button();
    close.setText("OK");
    close.setOnAction(e-> stage.close());

    //add the label and button to a vbox and set the alignment
    VBox vBox = new VBox();
    vBox.getChildren().addAll(lbl,close);
    vBox.setAlignment(Pos.CENTER);

    //set the scene and show the stage
    Scene ss = new Scene(vBox);
    stage.setScene(ss);
    stage.showAndWait();

  }
}
