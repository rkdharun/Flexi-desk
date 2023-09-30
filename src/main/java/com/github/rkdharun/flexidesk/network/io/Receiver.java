package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.network.packets.FilePacket;
import javafx.stage.DirectoryChooser;

import java.io.*;

public class Receiver {

  public static void receiveFile(ObjectInputStream objectInputStream){
    try {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Select Directory to save file");
      File selectedDirectory = new File("C:/Users/dharu/Desktop/");

//             Platform.runLater(
//                    () -> {
//                      Stage s = new Stage();
//                      selectedDirectory = directoryChooser.showDialog(s);
//                      System.out.println(selectedDirectory.getAbsolutePath());
//                    }
//            );

      System.out.println("File packet Receiving");
      FilePacket fp;
      fp = (FilePacket) objectInputStream.readObject();
      System.out.println("File packet received");
      System.out.println(fp.getFileName());
      System.out.println(fp.getFileLength());


      byte[] fileData = new byte[1024 * 1024];
      File tempFile = new File(selectedDirectory.getAbsolutePath() + "/" + fp.getFileName().trim());
      tempFile.createNewFile();
      FileOutputStream fos = new FileOutputStream(tempFile);

      Long toRead = fp.getFileLength();
      int read = 0;
      byte[] payload = new byte[1024 * 1024];
      while (toRead > 0 && (read = objectInputStream.read(payload)) != -1) {
        fos.write(payload, 0, read);
        toRead -= read;
        System.out.println(toRead / (1024 * 1024) + "MB");
      }
        System.out.println("File Received");
        fos.close();
        System.out.println("File Received");

    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
  }

  public static void receiveClip(ObjectInputStream objectInputStream) {
  }
}
