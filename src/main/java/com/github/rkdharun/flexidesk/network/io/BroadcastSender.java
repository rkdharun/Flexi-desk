package com.github.rkdharun.flexidesk.network.io;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;


public class BroadcastSender {



  public DatagramSocket socket ;

  /**
   * Start broadcasting (starts a thread that send broadcast continously)
   * @param msg message to be broadcasted
   */
  public void startBroadcasting(String msg,int port) {
    try {
      socket = new DatagramSocket(0);
      broadcast(msg, port);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * (starts a thread that send broadcast continously)
   * @param broadcastMessage message that should be broadcasted
   * @param port  destination port of the broadcast
   * @throws IOException
   */
  public void broadcast(String broadcastMessage, int port) throws IOException {

    System.out.println("Broadcasting in port " + port);
    socket.setReuseAddress(true);
    socket.setBroadcast(true);

    byte[] buffer = broadcastMessage.getBytes();

      while (true) {

        try {
          for (var addr : listAllBroadcastAddresses()) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, addr, port);
            socket.send(packet);
          }
        } catch (IOException e) {
          e.printStackTrace();
          System.out.println("Error Message :: "+ e.getMessage());
          break;
        }
      }
  }

  private List<InetAddress> listAllBroadcastAddresses() throws SocketException {

    List<InetAddress> broadcastList = new ArrayList<>();
    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    while (interfaces.hasMoreElements()) {
      NetworkInterface networkInterface = interfaces.nextElement();

      if (networkInterface.isLoopback() || !networkInterface.isUp()) {
        continue;
      }
      // add if interface is not wildcard
      networkInterface.getInterfaceAddresses().stream()
        .map(InterfaceAddress::getBroadcast)
        .filter(Objects::nonNull)
        .filter(broadcast -> !broadcast.isAnyLocalAddress())
        .forEach(broadcastList::add);
    }
    return broadcastList;
  }

  public int getBroadcastPort() {
    return socket.getLocalPort();
  }


  public void stopBroadcasting() {
    socket.close();

  }
}
