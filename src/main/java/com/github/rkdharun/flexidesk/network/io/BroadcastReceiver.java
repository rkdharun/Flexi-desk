package com.github.rkdharun.flexidesk.network.io;


import java.io.IOException;
import java.net.*;

public class BroadcastReceiver {
  DatagramSocket socket;

  public DatagramPacket receiveBroadcast(int port) {


    DatagramPacket receivePacket = null;

    try {

      socket = new DatagramSocket(port, InetAddress.getByName("0.0.0.0"));
      socket.setBroadcast(true);
      socket.setReuseAddress(true);
      byte[] receiveData = new byte[1024];
      System.out.println("Receiver waiting for broadcast...");
      receivePacket = new DatagramPacket(receiveData, receiveData.length);
      socket.receive(receivePacket);
      String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
      System.out.println("Received Message  : " + message);
      return receivePacket;

    } catch (IOException e) {
      System.out.println("Closed Receiver ");
      return null;
    }

  }

  public void close() {
    if(socket != null) {
      socket.close();
      System.out.println("Address closed\n");
    }
  }
}


