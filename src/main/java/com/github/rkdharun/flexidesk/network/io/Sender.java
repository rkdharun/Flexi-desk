package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.network.packets.FilePacket;
import com.github.rkdharun.flexidesk.network.packets.FilePacketBuilder;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class Sender {

  private SSLSocket socket;
  private final ObjectOutputStream objectOutputStream;

  public Sender(SSLSocket socket) {
    try {
      this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void sendFile(File file, SSLSocket socket) {

    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    byte[] payload = new byte[1024 * 1024]; // buffer to read data from the stream

    int read = 0; // Inital read value of the file
    FilePacket filePacket = null;

    try {
      // objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      System.out.println("Sending file header");
      objectOutputStream.write("file".getBytes(), 0, 4);  // write the header to the output stream
      filePacket = new FilePacketBuilder().setFileNames(file.getName()).setFileLength(file.length()).setFileInputStream(fis).buildPacket();
      System.out.println("Sending file Payload");

      // write the file name to the output stream
      objectOutputStream.writeObject(filePacket);
    } catch (IOException e) {
      e.printStackTrace();
    }

    ProgressBar progressBar = new ProgressBar();

    progressBar.setProgress(0);
    System.out.println("File Receiving");
    var lambdaContext = new Object() {
      int total = 0;
    };

    String fileName = filePacket.getFileName();
    float tsize = filePacket.getFileLength();
    Platform.runLater(() -> {
      VBox chat = new VBox();
      HBox filename = new HBox();
      chat.setAlignment(Pos.BOTTOM_LEFT);
      Label l =  new Label(fileName);
      l.setWrapText(true);
      l.setTextFill(Paint.valueOf("white"));
      l.setPadding( new Insets(20.0,20.0,20.0,20.0));
      chat.getChildren().add(l);
      Label progress = new Label();
      progress.setTextFill(Paint.valueOf("white"));
      progress.setText(lambdaContext.total + "/"+ tsize);
      chat.getChildren().add(progress);


      MainApp.applicationController.chatView.getChildren().add(chat);


    });

    try {
      System.out.println("Written");
      Long fSize = filePacket.getFileLength();
      while ((read = fis.read(payload)) != -1) {

        objectOutputStream.write(payload, 0, read);
        lambdaContext.total += read;

        // System.out.println(total / (1024 * 1024) + "MB");
      }
      objectOutputStream.flush();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    System.out.println("File Written");
  }

  public static void sendClip(ObjectInputStream objectInputStream) {

  }
}
