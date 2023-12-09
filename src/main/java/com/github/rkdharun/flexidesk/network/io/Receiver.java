package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.guiUtils.ProgressIndicatorBox;
import com.github.rkdharun.flexidesk.network.packets.FilePacket;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import javax.swing.*;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Receiver {
  public ObjectInputStream objectInputStream;

  public Receiver(ObjectInputStream objectInputStream) {
    this.objectInputStream = objectInputStream;
  }

  public static void receiveFile(ObjectInputStream objectInputStream) {
    try {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Select Directory to save file");
      final File[] selectedDirectory = {null};
      AtomicBoolean isFinished = new AtomicBoolean(false);
      Platform.runLater(
        () -> {
          Stage s = new Stage();
          selectedDirectory[0] = directoryChooser.showDialog(s);
          System.out.println(selectedDirectory[0].getAbsolutePath());
          isFinished.set(true);
        }
      );

      while (!isFinished.get()){
      }

      System.out.println("File packet Receiving");
      FilePacket fp;
      fp = (FilePacket) objectInputStream.readObject();


      System.out.println("File packet received");
      System.out.println(fp.getFileName());
      System.out.println(fp.getFileLength());


      byte[] fileData = new byte[1024 * 1024];
      File tempFile = new File(selectedDirectory[0].getAbsolutePath() + "/" + fp.getFileName().trim());
      tempFile.createNewFile();
      FileOutputStream fos = new FileOutputStream(tempFile);

      Long toRead = fp.getFileLength();
      int read = 0;
      Long totalReceived = 0L;
      byte[] payload = new byte[1024 * 1024];
      ProgressBar progressBar = new ProgressBar();

      progressBar.setProgress(0);
      System.out.println("File Receiving");
      Long finalTotalReceived = totalReceived;
      Platform.runLater(()->{
        MainApp.applicationController.chatView.getChildren().add(new Label(fp.getFileName()));
        Stage s = new Stage();
        s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle("Receiving File");
        s.setMinWidth(250);
        HBox hbox = new HBox();
        hbox.getChildren().add(progressBar);
        Scene sc = new Scene(hbox, 200, 200);
        // set the scene
        s.setScene(sc);
        s.showAndWait();

      });
      while (totalReceived < toRead && (read = objectInputStream.read(payload)) != -1) {
        fos.write(payload, 0, read);
        totalReceived += read;

        progressBar.setProgress( (double) totalReceived /toRead);
        //System.out.println(totalReceived + "--MB---" + toRead);
      }
      System.out.println("File Received");
      fos.close();
      System.out.println("File Received");

    } catch (ClassNotFoundException | IOException e) {
      System.out.println(e.getMessage());

    }

    System.out.println("File Received Exiting function");
  }

  public static void receiveClip(ObjectInputStream objectInputStream) {
  }


}
