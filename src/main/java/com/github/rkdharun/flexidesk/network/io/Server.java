package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.config.SSLConfiguration;
import com.github.rkdharun.flexidesk.controller.app.ApplicationController;
import com.github.rkdharun.flexidesk.controller.app.MainUIUpdater;
import com.github.rkdharun.flexidesk.network.service.BroadcastSender;
import com.github.rkdharun.flexidesk.network.service.ConnectionHandler;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.ObjectInputStream;


public class Server {

  private int port;
  public SSLServerSocket sslServerSocket = null;
  private final SSLConfiguration sslConfiguration;
  private BroadcastSender bs;

  private SSLSocket currentSslSocket = null;

  /**
   * creates a new server object and initiates a new ssl configuration
   */
  public Server() {
    sslConfiguration = new SSLConfiguration();
  }

  /**
   * set default certificate for the server
   */
  public void initConfiguration() {
    sslConfiguration.setDefaultCertificate();
  }


  /**
   * creates a serverSocket and wait for connection while sending a broadcast with server port to the specified broadcastPort so,
   * that a client can perform an udp discovery and
   * @implNote   Should be called inside the thread to avoid UI interruption
   * @param broadcastPort port number in which the broadcast to be sent ,i.e. the destination
   */
  public void start(int broadcastPort) {
      try {

        //create a new broadcast sender
        bs = new BroadcastSender();


        SSLServerSocketFactory sslServerSocketFactory = sslConfiguration.getSslServerSocketFactory();

        //create a server socket
        sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(0);


        this.setPort(sslServerSocket.getLocalPort());
        System.out.println("Server Started at port " + getPort());

        //set client authentication to true so that the server can verify the client with ssl certificate
        sslServerSocket.setNeedClientAuth(true);

        //start accepting connections from client
        acceptConnections(broadcastPort);

      } catch (Exception e) {
        e.printStackTrace();

      }



  }

  /**
   *   accepts connection from client runs in a separate thread,
   *   the server sends a broadcast to the specified port and waits for the client to receive the broadcast and initiate a TCP connection
   *   with the server by using the information provided in the broadcast message (ip and port)
   * @param broadcastPort port number in which the broadcast to be sent ,i.e. the destination
   *
   */

  public void acceptConnections(int broadcastPort) {
      try {
        System.out.println("Active threads are :" + Thread.activeCount());
        //start broadcasting

        new Thread(() -> bs.startBroadcast(String.valueOf(sslServerSocket.getLocalPort()), broadcastPort)).start();

        System.out.println("Waiting for connections");
        System.out.println("Active threads are :" + Thread.activeCount());
        //accept connection from client
        currentSslSocket = (SSLSocket) sslServerSocket.accept();
        currentSslSocket.addHandshakeCompletedListener(handshakeCompletedEvent -> MainUIUpdater.setChatUIOnConnection());


        MainApp.applicationController.setActiveSocket(currentSslSocket);
        ApplicationController.sender = new Sender(currentSslSocket);
        ApplicationController.receiver = new Receiver(new ObjectInputStream(currentSslSocket.getInputStream()));
        //stop broadcasting to avoid further connections
        bs.stopBroadcasting();

        //create a new thread to handle the client connection
        new Thread(new ConnectionHandler(currentSslSocket)).start();


        System.out.println("Active threads are :" + Thread.activeCount());
        System.out.println("Connection AAccepted");

      } catch (IOException e) {
        System.out.println(e.getMessage());
      }

  }

  public void setCurrentSslSocket(SSLSocket socket){
    this.currentSslSocket = socket;
  }


  public void setPort(int port) {
    this.port = port;
  }

  /**
   *
   * @return port number of the server
   */
  public int getPort() {
    return this.port;
  }


  /**
   * stops the server  if it is not null and stop broadcasting
   */
  public void close() {
    try {
      if (sslServerSocket != null) {
        //close the server socket and stop broadcasting
        sslServerSocket.close();
        System.out.println("sslServerSocket closed");
        bs.stopBroadcasting();
        System.out.println("server closed and broadcasting stopped");
      }
      if(currentSslSocket != null) {
        currentSslSocket.close();
        System.out.println("connection socket is closed");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

