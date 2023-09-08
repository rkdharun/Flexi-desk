package com.github.rkdharun.flexidesk.network.io;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadcastReceiver {


  public int receiveBroadcast(int port) {
    try {

      DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName("0.0.0.0"));

      socket.setBroadcast(true);

      byte[] receiveData = new byte[1024];

      System.out.println("Receiver waiting for broadcast...");
      while (true) {
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("Received: " + message);
        System.out.println("ADDRESS AND PORT :" + receivePacket.getAddress() + " on port  : " + receivePacket.getPort());


      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 1;
  }
}


