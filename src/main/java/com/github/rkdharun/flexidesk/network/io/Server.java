package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.config.SSLConfiguration;
import com.github.rkdharun.flexidesk.network.service.BroadcastSender;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


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
   * creates a serverSocket and wait for connection while sending a broadcast with server port to the specified broadcastPnort so,
   * that a client can perform an udp discovery
   *
   * @param broadcastPort port number in which the broadcast to be sent ,i.e the destination
   */
  public void start(int broadcastPort) {

    //Thread for connecting to the server. This thread will be stopped once the connection is established
    new Thread(() -> {
      try {

        //create a new broadcast sender
        bs = new BroadcastSender();

        //create a server socket
        sslServerSocket = (SSLServerSocket) sslConfiguration.getSslServerSocketFactory().createServerSocket(0);

        this.setPort(sslServerSocket.getLocalPort());
        System.out.println("Server Started at port " + getPort());

        //set client authentication to true so that the server can verify the client with ssl certificate
        sslServerSocket.setNeedClientAuth(true);

        //start accepting connections from client
        acceptConnections(broadcastPort);

      } catch (Exception e) {
        e.printStackTrace();

      }

      //print the active threads
//      Thread[] tarray = new Thread[Thread.activeCount()];
//      Thread.enumerate(tarray);
//      for (Thread tq : tarray)
//        System.out.println(tq.getName());

    }).start();

  }

  /**
   *   accepts connection from client runs in a separate thread,
   *   the server sends a broadcast to the specified port and waits for the client to receive the broadcast and initiate a TCP connection
   *   with the server by using the information provided in the broadcast message (ip and port)
   * @param broadcastPort port number in which the broadcast to be sent ,i.e the destination
   *
   */

  public void acceptConnections(int broadcastPort) {
      try {
        System.out.println("Active threads are :" + Thread.activeCount());
        //start broadcasting
        new Thread(new Runnable() {
          @Override
          public void run() {
            bs.startBroadcasting(String.valueOf(sslServerSocket.getLocalPort()), broadcastPort);
          }
        }).start();

        System.out.println("Waiting for conenctions");
        System.out.println("Active threads are :" + Thread.activeCount());
        //accept connection from client
        currentSslSocket = (SSLSocket) sslServerSocket.accept();

        MainApp.applicationController.setActiveConnectionRunning(true);
        //stop broadcasting to avoid further connections
        bs.stopBroadcasting();

        //create a new thread to handle the client connection
        new Thread(new ClientHandler(currentSslSocket,this)).start();


        System.out.println("Active threads are :" + Thread.activeCount());
        System.out.println("Connection ACcepted");

      } catch (IOException e) {
        // check if serversocket is closed and break the loop for stopping any further connections to the same sever
        if (sslServerSocket.isClosed()) ;
      }

  }


  public SSLSocket getCurrentSslSocket() {
    return currentSslSocket;
  }

  public void setCurrentSslSocket(SSLSocket socket){
    this.currentSslSocket = socket;
  }

  public SSLServerSocket getSslServerSocket() {
    return sslServerSocket;
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


class ClientHandler implements Runnable {
  private SSLSocket sslSocket;
  private Server server;
  public ClientHandler(SSLSocket sslSocket, Server server) {
    this.sslSocket = sslSocket;
    this.server = server;
  }


  //handles the client connection
  @Override
  public void run() {
    System.out.println(" Client handler Active threads are :" + Thread.activeCount());

    try {
      System.out.println("reading from client");
      InputStream is = sslSocket.getInputStream();
      OutputStream os = sslSocket.getOutputStream();
      byte[] buff = new byte[1000];
      while (is.read(buff) != -1) {
        os.write(buff);
      }

    } catch (Exception e) {
      server.setCurrentSslSocket(null);
      System.out.println("ERROR MESSAGE ON CLIENT HANDLER :: "  +e.getMessage());
      MainApp.applicationController.revertMainUI();

    }
    System.out.println("Active threads are :" + Thread.activeCount());

  }
}
