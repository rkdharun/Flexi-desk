package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.config.SSLConfiguration;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.util.Arrays;

public class Client {
  SSLConfiguration sslConfiguration;
  SSLSocket sslSocket;

  public Client() {
    sslConfiguration = new SSLConfiguration();
  }

  public void initConfiguration() {
    sslConfiguration.setDefaultCertificate();
  }

  public void joinNetwork(InetAddress ip, int port) {
    // Create SSL socket factory and socket
    SSLSocketFactory sslSocketFactory = sslConfiguration.getSslSocketFactory();

    try {
      String ipA = Arrays.toString(ip.getAddress());
      System.out.println("IP Address inside "+ipA);
      sslSocket = (SSLSocket) sslSocketFactory.createSocket(ip, port);
      sslSocket.setNeedClientAuth(true);
      new Thread(new ConnectionHandler(sslSocket)).start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public SSLSocket getSslSocket() {
    return this.sslSocket;
  }

  public void disconnect() {
    try {
      if(this.getSslSocket()!=null)
        this.getSslSocket().close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.out.println("Nothing to worrry");
    }
  }

}


class ConnectionHandler implements Runnable {

  SSLSocket sslSocket;

  ConnectionHandler(SSLSocket sslSocket) {
    this.sslSocket = sslSocket;
  }

  @Override
  public void run() {
    OutputStream os = null;
    try {

      InputStream is = sslSocket.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      os = sslSocket.getOutputStream();
      String msg;
      int flag = 0;
      byte[] buff = new byte[1000 * 1024];

      while (flag != 1) {
        msg = br.readLine();
        try {
          os.write(msg.getBytes());
          is.read(buff);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

        System.out.println("Response : " + new String(buff).trim());
        msg = br.readLine();
        flag = Integer.parseInt(msg);
        System.out.println(flag);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
//--------------------------------------------------------------------------------------
//                           printing the ssl certificates
// SSLSession session = sslSocket.getSession();
// java.security.cert.Certificate[] servercerts = session.getPeerCertificates();

// List mylist = new ArrayList();
// for (int i = 0; i < servercerts.length; i++) {
//     mylist.add(servercerts[i]);
//     System.out.println("This is form server\n");
//     System.out.println(servercerts[i].toString());
// }
//----------------------------------------------------------------------------------------


// Certificate[] certs = sslSocket.getHandshakeSession().getLocalCertificates();

// for (Certificate certificate : certs) {
//     System.out.println(certs.toString());
// }








