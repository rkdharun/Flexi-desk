package com.github.rkdharun.flexidesk.network.io;

import com.github.rkdharun.flexidesk.config.SSLConfiguration;
import com.github.rkdharun.flexidesk.utilities.ServerNotFoundException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.util.Arrays;

public class Client {
  SSLConfiguration sslConfiguration;
  SSLSocket sslSocket;

  /**
   * Create a new SSL configuration
   */
  public Client() {
    sslConfiguration = new SSLConfiguration();
  }

  /**
   * set default certificate
   */
  public void initConfiguration() {
    sslConfiguration.setDefaultCertificate();
  }

  /**
   *  Connects to the server and handles the connection is seperate thread
   * @param ip  ip address of the server
   * @param port port number of the server
   * @throws ServerNotFoundException thrown when there is no server in the given ip:port
   */
  public void joinNetwork(InetAddress ip, int port) throws ServerNotFoundException {

    // get the ssl socket factory from the created ssl configuration
    SSLSocketFactory sslSocketFactory = sslConfiguration.getSslSocketFactory();


    try {
      //create a ssl socket with the specified ip and port
      sslSocket = (SSLSocket) sslSocketFactory.createSocket(ip, port);

      //set client authentication to true so that the server can verify the client with ssl certificate
      sslSocket.setNeedClientAuth(true);

      System.out.println("Passing to Connection handler :: active Threads :: "+Thread.activeCount());
      //new Thread to handle the connection
      new Thread(new ConnectionHandler(sslSocket)).start();
      System.out.println("Control passed to Connection handler :: active Threads :: "+Thread.activeCount()+Thread.currentThread().getStackTrace()[1]);

    } catch (IOException e) {
      e.printStackTrace();
      throw new ServerNotFoundException("Server Not Found");
    }

  }

  /**
   *
   * @return return the ssl socket
   */
  public SSLSocket getSslSocket() {
    return this.sslSocket;
  }


  /**
   * Disconnects from the server by closing the ssl socket.
   */
  public void disconnect() {
    try {
      if(this.getSslSocket()!=null){
        //close the ssl socket
        this.getSslSocket().close();
        System.out.println("Client Disconnected");

      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    finally {
      System.out.println("Client Disconnected from server :: "+Thread.activeCount()+" "+Thread.currentThread().getStackTrace()[1]);

    }
  }


}

// program for handling the connection
class ConnectionHandler implements Runnable {

  SSLSocket sslSocket;

  ConnectionHandler(SSLSocket sslSocket) {
    this.sslSocket = sslSocket;
  }

  @Override
  public void run() {

    System.out.println("Active threads are :"+Thread.activeCount()+"Running thread : "+Thread.currentThread().getStackTrace()[1]);
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
        os.write(msg.getBytes());
        is.read(buff);
        System.out.println("Response : " + new String(buff).trim());
        msg = br.readLine();
        flag = Integer.parseInt(msg);
        System.out.println(flag);
      }

      if(sslSocket!=null) sslSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    finally {

      System.out.println("Connection Handler Closed  :: " +Thread.activeCount());
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








