package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.guiUtils.ProgressIndicatorBox;
import com.github.rkdharun.flexidesk.network.packets.FilePacket;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import javax.swing.*;

import java.awt.*;
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
      var ref = new Object() {
        Long totalReceived = 0L;
      };
      byte[] payload = new byte[1024 * 1024];
      ProgressBar progressBar = new ProgressBar();

      progressBar.setProgress(0);
      System.out.println("File Receiving");
      Long finalTotalReceived = ref.totalReceived;
      Platform.runLater(()->{
        VBox chat = new VBox();
        chat.setAlignment(Pos.BOTTOM_LEFT);
        Label msg = new Label(fp.getFileName());
        msg.setWrapText(true);
        msg.setTextFill(Paint.valueOf("white"));
        Label progress = new Label();
        progress.setTextFill(Paint.valueOf("white"));
        progress.setText(ref.totalReceived + "/"+ toRead);
        progress.setPadding( new Insets(20.0,20.0,20.0,20.0));
        chat.getChildren().add(msg);
        chat.getChildren().add(progress);

        MainApp.applicationController.chatView.getChildren().add(msg);


      });
      while (ref.totalReceived < toRead && (read = objectInputStream.read(payload)) != -1) {
        fos.write(payload, 0, read);
        ref.totalReceived += read;

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
