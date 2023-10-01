package com.github.rkdharun.flexidesk.network.service;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.network.io.Receiver;
import javafx.stage.DirectoryChooser;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class ConnectionHandler implements Runnable {
  private final ObjectInputStream objectInputStream;
  public ConnectionHandler(SSLSocket socket) {

    try {
      this.objectInputStream = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * Waits for a header to be arrived and Handle the connection based on the header
   */
  public void handle() {

    try {


      byte[] header = new byte[4];

      while (objectInputStream.read(header, 0, 4) != -1) {

        System.out.println("Header received  :: " + new String(header, 0, 4).trim());
        String headerData = new String(header, 0, 4).trim();
        if (headerData.equalsIgnoreCase("file")) {
          Receiver.receiveFile(objectInputStream);
        }
        if (headerData.equalsIgnoreCase("clip")) {
          Receiver.receiveClip(objectInputStream);
        }
      }

      System.out.println("out of loop");
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException(e);


    } finally {
      System.out.println("::::::::::::::::::::::::Connection Handler Closed::::::::::::::::::::::::: " + Thread.activeCount());
      MainApp.applicationController.revertMainUI();
    }
  }

  /**
   *
   */
  @Override
  public void run() {
    handle();

  }
}
