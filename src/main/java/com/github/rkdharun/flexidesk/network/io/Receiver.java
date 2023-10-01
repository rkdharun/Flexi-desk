package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.gui.ConfirmationDialog;
import com.github.rkdharun.flexidesk.network.packets.FilePacket;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

public class Receiver {

  public ObjectInputStream objectInputStream;
  public Receiver(SSLSocket socket) {
    try {
      this.objectInputStream = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void receiveFile(ObjectInputStream objectInputStream) {
    try {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Select Directory to save file");
      AtomicReference<File> selectedDirectory = null;
      System.out.println("File packet Receiving");
      FilePacket fp;
      fp = (FilePacket) objectInputStream.readObject();
      System.out.println("File packet received");
      System.out.println(fp.getFileName());
      System.out.println(fp.getFileLength());

      boolean isAccepted = ConfirmationDialog.show("Do you want to Receive the file " + fp.getFileName() + " ?", "Request");

      if (isAccepted) {
        Platform.runLater(
          () -> {
            Stage s = new Stage();
            selectedDirectory.set(directoryChooser.showDialog(s));
            System.out.println(selectedDirectory.get().getAbsolutePath());
            try {
              MainApp.applicationController.sender.objectOutputStream.write("acc".getBytes());
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
        );

      }
      else {
        MainApp.applicationController.sender.objectOutputStream.write("rej".getBytes());
        return;
      }



      byte[] fileData = new byte[1024 * 1024];
      File tempFile = new File(selectedDirectory.get().getAbsolutePath() + "/" + fp.getFileName().trim());
      tempFile.createNewFile();
      FileOutputStream fos = new FileOutputStream(tempFile);

      Long toRead = fp.getFileLength();
      int read = 0;
      Long totalReceived = 0l;
      byte[] payload = new byte[1024 * 1024];

      System.out.println("File Receiving");
      while (totalReceived < toRead && (read = objectInputStream.read(payload)) != -1) {
        fos.write(payload, 0, read);
        totalReceived += read;
        System.out.println(totalReceived + "--MB---" + toRead);
      }
      System.out.println("File Received");
      fos.close();
      System.out.println("File Received");

    } catch (ClassNotFoundException | IOException e) {
      throw new RuntimeException(e);

    }

    System.out.println("File Received Exiting function");
  }

  public static void receiveClip(ObjectInputStream objectInputStream) {
  }
}
