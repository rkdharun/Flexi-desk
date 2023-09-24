package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.network.packets.FilePacket;
import com.github.rkdharun.flexidesk.network.packets.FilePacketBuilder;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class Sender {
  public void sendFile(File file, SSLSocket socket) {

    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    byte[] payload = new byte[1024 * 1024]; // buffer to read data from the stream

    int read = 0; // Inital read value of the file

    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    FilePacket filePacket = new FilePacketBuilder().setFileNames(file.getName()).setFileLength(file.length()).setFileInputStream(fis).buildPacket();
    // write the file name to the output stream
    try {
      oos.writeObject(filePacket);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    int total = 0;
    try {
      System.out.println("Written");
      while ((read = fis.read(payload)) != -1) {
        oos.write(payload, 0, read);
        total += read;
        System.out.println(total / (1024 * 1024) + "MB");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    System.out.println("File Written");
  }

  public static void sendClip(ObjectInputStream objectInputStream){

  }
}
