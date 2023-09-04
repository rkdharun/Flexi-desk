package com.github.rkdharun.flexidesk.utilities;

import com.github.rkdharun.flexidesk.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class FXMLoader {


  public Pane getPage(String fxmlName){
      FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/" + fxmlName + ".fxml"));
    try {
      return fxmlLoader.load();
    } catch (IOException e) {
      System.out.println(e.getMessage()+"Cannot load the desired FXML" );
    }
    return null;
  }


}
