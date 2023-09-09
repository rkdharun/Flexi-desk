package com.github.rkdharun.flexidesk.utilities;

import com.github.rkdharun.flexidesk.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class FXMLoader {


  /**
   * @param fxmlName name of the fxml file to be loaded
   * @return FXMLLoader object with the fxml file loaded
   */
  public FXMLLoader getPage(String fxmlName) {
    return new FXMLLoader(MainApp.class.getResource("/fxml/" + fxmlName + ".fxml"));
  }


}
