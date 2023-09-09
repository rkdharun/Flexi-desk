package com.github.rkdharun.flexidesk.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Message {

  public  static  void show(String message,String title){
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setTitle(title);

    Label lbl = new Label(message);
    Button close = new Button();
    close.setText("OK");
    close.setOnAction(e-> stage.close());
    VBox vBox = new VBox();
    vBox.getChildren().addAll(lbl,close);
    vBox.setAlignment(Pos.CENTER);
    Scene ss = new Scene(vBox);
    stage.setScene(ss);
    stage.showAndWait();

  }
}
