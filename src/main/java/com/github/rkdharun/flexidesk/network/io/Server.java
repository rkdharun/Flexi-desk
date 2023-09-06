package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.config.ConnectionConfiguration;
import com.github.rkdharun.flexidesk.utilities.QRGenerator;
import com.google.zxing.WriterException;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Server {

  private int port;
  public SSLServerSocket sslServerSocket = null;

  ConnectionConfiguration connectionConfiguration;

  public Server() {
    connectionConfiguration = new ConnectionConfiguration();
  }

  public void start() {

    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          sslServerSocket = (SSLServerSocket) connectionConfiguration.getSslServerSocketFactory().createServerSocket(0);
          setPort(sslServerSocket.getLocalPort());
          System.out.println("Server started at port " + sslServerSocket.getLocalPort());
          sslServerSocket.setNeedClientAuth(true);
          acceptConnections();
          return;
        } catch (Exception e) {
          System.out.println(e.getMessage());

        }
      }
    }).start();

  }

  public void initConfiguration() {
    connectionConfiguration.setDefaultCertificate();

  }

  public void acceptConnections() {
    while (true) {

      SSLSocket sslSocket = null;
      try {
        sslSocket = (SSLSocket) sslServerSocket.accept();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      new Thread(new ClientHandler(sslSocket)).start();
    }
  }

  public javafx.scene.image.Image getQR() {
    try {
      return QRGenerator.createQRImage(Integer.toString(this.getPort()), 500);
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
      if (sslServerSocket != null)
        sslServerSocket.close();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

class ClientHandler implements Runnable {
  private final SSLSocket sslSocket;

  public ClientHandler(SSLSocket sslSocket) {
    this.sslSocket = sslSocket;
  }

  @Override
  public void run() {
    try {
      System.out.println("reading from client");
      InputStream is = sslSocket.getInputStream();
      OutputStream os = sslSocket.getOutputStream();
      byte[] buff = new byte[1000];
      String msg = "HI";

      while (is.read(buff) != -1) {
        os.write(buff);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
