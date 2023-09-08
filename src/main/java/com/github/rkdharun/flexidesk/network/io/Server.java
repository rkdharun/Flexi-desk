package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.config.SSLConfiguration;
import com.github.rkdharun.flexidesk.utilities.QRGenerator;
import com.google.zxing.WriterException;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Server {

  private int port;
  public SSLServerSocket sslServerSocket = null;
  private final SSLConfiguration sslConfiguration;
  private BroadcastSender bs;

  public Server() {
    sslConfiguration = new SSLConfiguration();
  }

  public void start() {
    new Thread(() -> {
      MainApp.currentThreadCounts += 1;
      try {
        bs = new BroadcastSender();
        sslServerSocket = (SSLServerSocket) sslConfiguration.getSslServerSocketFactory().createServerSocket(0);
        setPort(sslServerSocket.getLocalPort());
        sslServerSocket.setNeedClientAuth(true);


        new Thread(this::acceptConnections).start();

        System.out.println("Ready To accept Conections");
      } catch (Exception e) {
        e.printStackTrace();

      }
      MainApp.currentThreadCounts -= 1;

    }).start();

  }

  public void initConfiguration() {
    sslConfiguration.setDefaultCertificate();

  }

  public void acceptConnections() {
    while (true) {
      SSLSocket sslSocket;
      try {
        bs.startBroadcasting(String.valueOf(sslServerSocket.getLocalPort()));
        sslSocket = (SSLSocket) sslServerSocket.accept();
        bs.stopBroadcasting();
        Thread t1 = new Thread(new ClientHandler(sslSocket));
        t1.start();


        System.out.println("Connection ACcepted");

      } catch (IOException e) {
        if (sslServerSocket.isClosed()) break;
      }
    }
  }


  public javafx.scene.image.Image getQR() {
    try {
      String message = this.getPort() + ":" + bs.getBroadcastPort();
      System.out.println(message);
      return QRGenerator.createQRImage(message, 500);
    } catch (WriterException | IOException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public void setPort(int port) {

    this.port = port;
  }

  public int getPort() {
    return port;
  }


  public void close() {
    try {
      if (sslServerSocket != null) {
        sslServerSocket.close();
        bs.stopBroadcasting();
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}


class ClientHandler implements Runnable {
  private SSLSocket sslSocket;

  public ClientHandler(SSLSocket sslSocket) {
    this.sslSocket = sslSocket;
  }

  @Override
  public void run() {
    MainApp.currentThreadCounts += 1;
    try {
      System.out.println("reading from client");
      InputStream is = sslSocket.getInputStream();
      OutputStream os = sslSocket.getOutputStream();
      byte[] buff = new byte[1000];

      while (is.read(buff) != -1) {
        os.write(buff);
      }

    } catch (Exception e) {
      sslSocket = null;
      System.out.println(e.getMessage());
      return;

    }

    MainApp.currentThreadCounts -= 1;
  }
}
