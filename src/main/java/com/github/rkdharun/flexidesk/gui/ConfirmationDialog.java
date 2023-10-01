package com.github.rkdharun.flexidesk.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is used to show the message in a new stage with a button to close the stage
 */
public class ConfirmationDialog {

  /**
   * static show method to display a custom message or warning to the user
   * @param message custom message the user wants to display
   * @param title title of the stage or related heading to the message
   */
  public  static  boolean show(String message,String title){
    //create a new stage
    Stage stage = new Stage();

    AtomicBoolean userIp = new AtomicBoolean(false);
    //set the stage properties
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setTitle(title);

    //create a label and a button
    Label lbl = new Label(message);
    Button yes =  new Button("accept");
    yes.setOnAction(e->{
      userIp.set(true);
      stage.close();
    });
    Button cancel = new Button();
    cancel.setText("OK");
    cancel.setOnAction(e->{
      userIp.set(false);
      stage.close();
    });

    HBox hBox = new HBox();
    hBox.getChildren().addAll(lbl,cancel);
    hBox.setAlignment(Pos.CENTER);
    //add the label and button to a vbox and set the alignment
    VBox vbox = new VBox();
    vbox.getChildren().addAll(lbl,hBox);


    //set the scene and show the stage
    Scene ss = new Scene(vbox);
    stage.setScene(ss);
    stage.showAndWait();
    return userIp.get();
  }
}
