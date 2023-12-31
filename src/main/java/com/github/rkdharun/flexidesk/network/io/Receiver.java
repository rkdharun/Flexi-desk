package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.network.packets.FilePacket;
import javafx.application.Platform;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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

      SimpleFloatProperty step = new SimpleFloatProperty(0);
      step.addListener((observable, oldValue, newValue) -> {
        progressBar.setProgress(newValue.floatValue());
      });
      System.out.println("File Receiving");
      Long finalTotalReceived = ref.totalReceived;
      Platform.runLater(()->{
        VBox chat = new VBox();
        chat.setAlignment(Pos.BOTTOM_LEFT);
        Label msg = new Label(fp.getFileName());
        msg.setWrapText(true);
        msg.setTextFill(Paint.valueOf("white"));
        progressBar.setVisible(true);
        chat.getChildren().addAll(msg,progressBar);
        MainApp.applicationController.chatView.getChildren().add(chat);


      });
      while (ref.totalReceived < toRead && (read = objectInputStream.read(payload)) != -1) {
        fos.write(payload, 0, read);
        ref.totalReceived += read;
        step.set((float) (ref.totalReceived / (float) toRead));
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
