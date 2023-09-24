package com.github.rkdharun.flexidesk.network.service;


import com.github.rkdharun.flexidesk.utilities.QRGenerator;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class BroadcastReceiver {
  DatagramSocket socket ;

  /**
   * create a DatagramSocket socket at any allocated port
   */
  public BroadcastReceiver() {
    try {
      socket = new DatagramSocket(5555);

    } catch (SocketException e) {
      e.printStackTrace();
    }
  }

  /**
   * starts receiving the broadcast
   * @return receivePacket the datagram-packet received from the broadcast
   */
  public DatagramPacket receiveBroadcast() {
    DatagramPacket receivePacket = null;

    try {
      //set broadcast to true for receiving broadcast
      socket.setBroadcast(true);

      //allow reuse of the address
      socket.setReuseAddress(true);

      //create a byte array to store the received data
      byte[] receiveData = new byte[1024];
      System.out.println("Receiver waiting for broadcast at port ::"+socket.getLocalPort());

      //create a datagram packet to store the received data
      receivePacket = new DatagramPacket(receiveData, receiveData.length);

      //receive the broadcast
      socket.receive(receivePacket);

      //to remove
      String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
      System.out.println("Received Message  : " + message);

      //return the received packet
      return receivePacket;

    } catch (IOException e) {
      System.out.println("Returning from broadcast receiver");
      e.printStackTrace();
      return null;
    }

  }

  /**
   *
   * @return the port number of the socket in which broadcastReceiver is receiving
   */
  public int getBroadcastReceptionPort(){
    return socket.getLocalPort();
  }

  /**
   *
   * @return  QR code of the port number of the socket in which broadcastReceiver is receiving in client
   */
  public javafx.scene.image.Image getQR() {
    int i = 0;
    try {
      String message = String.valueOf(socket.getLocalPort());
      System.out.println(message);
      return QRGenerator.createQRImage(message, 800);
    } catch (WriterException | IOException e) {
       e.printStackTrace();
    }
    System.out.println("Returning NUll for broadcast port");
    return null;
  }

  /**
   * closes the socket and stops receiving broadcast
   */
  public void close() {
    if(socket != null) {
      //close the Datagram socket
      socket.close();
      System.out.println("broadcast receiver socket closed\n");
    }
  }
}


