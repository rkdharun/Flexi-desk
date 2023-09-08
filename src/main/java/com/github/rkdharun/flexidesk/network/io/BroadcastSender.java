package com.github.rkdharun.flexidesk.network.io;


import com.github.rkdharun.flexidesk.MainApp;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;


public class BroadcastSender {

  public boolean startBroadcast = false;

  public DatagramSocket socket = MainApp.broadcastSocket;

  public void startBroadcasting(String msg) {
    try {
      startBroadcast = true;
      broadcast(msg, InetAddress.getByName("255.255.255.255"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void broadcast(String broadcastMessage, InetAddress address) throws IOException {

    System.out.println("Broadcasting in port " + socket.getLocalPort());
    socket.setReuseAddress(true);
    socket.setBroadcast(true);

    byte[] buffer = broadcastMessage.getBytes();

    new Thread(() -> {
      while (startBroadcast) {

        try {
          for (var addr : listAllBroadcastAddresses()) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, addr, 8888);
            socket.send(packet);
          }
          Thread.currentThread().join(1500);
        } catch (IOException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }).start();
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
    startBroadcast = false;
  }
}
