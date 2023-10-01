package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.network.packets.FilePacket;
import com.github.rkdharun.flexidesk.network.packets.FilePacketBuilder;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class Sender {

  private SSLSocket socket;
  public ObjectOutputStream objectOutputStream;

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


    try {
      // objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      System.out.println("Sending file header");
      objectOutputStream.write("file".getBytes(), 0, 4);  // write the header to the output stream
      FilePacket filePacket = new FilePacketBuilder().setFileNames(file.getName()).setFileLength(file.length()).setFileInputStream(fis).buildPacket();
      System.out.println("Sending file Payload");

      // write the file name to the output stream
      objectOutputStream.writeObject(filePacket);
    } catch (IOException e) {
      e.printStackTrace();
    }

    //wait for confirmation of acceptance
    byte[] buff = new byte[3];
    try {
      MainApp.applicationController.receiver.objectInputStream.read(buff, 0, 3);
      if (new String(buff).trim().equalsIgnoreCase("rej")) {
        return;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


    int total = 0;
    try {
      System.out.println("Written");
      while ((read = fis.read(payload)) != -1) {

        objectOutputStream.write(payload, 0, read);

        total += read;
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
