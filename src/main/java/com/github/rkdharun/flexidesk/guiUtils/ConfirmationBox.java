package com.github.rkdharun.flexidesk.guiUtils;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.event.*;
import javafx.geometry.*;

public class ConfirmationBox {
  static Stage stage;
  static boolean btnYesClicked;

  public static boolean show(String message, String title,
                             String textYes, String textNo) {
    btnYesClicked = false;
    stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setTitle(title);
    stage.setMinWidth(250);
    Label lbl = new Label();
    lbl.setText(message);
    Button btnYes = new Button();
    btnYes.setText(textYes);
    btnYes.setOnAction(e -> btnYes_Clicked());
    Button btnNo = new Button();
    btnNo.setText(textNo);
    btnNo.setOnAction(e -> btnNo_Clicked());
    HBox paneBtn = new HBox(20);
    paneBtn.getChildren().addAll(btnYes, btnNo);
    VBox pane = new VBox(20);
    pane.getChildren().addAll(lbl, paneBtn);
    pane.setAlignment(Pos.CENTER);
    Scene scene = new Scene(pane);
    stage.setScene(scene);
    stage.showAndWait();
    return btnYesClicked;
  }

  private static void btnYes_Clicked() {
    stage.close();
    btnYesClicked = true;
  }

  private static void btnNo_Clicked() {
    stage.close();
    btnYesClicked = false;
  }
}
