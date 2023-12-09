package com.github.rkdharun.flexidesk.controller.app;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.network.io.Client;
import com.github.rkdharun.flexidesk.network.io.Receiver;
import com.github.rkdharun.flexidesk.network.io.Sender;
import com.github.rkdharun.flexidesk.network.io.Server;
import com.github.rkdharun.flexidesk.network.service.BroadcastReceiver;
import com.github.rkdharun.flexidesk.utilities.FXMLoader;
import com.github.rkdharun.flexidesk.utilities.ServerNotFoundException;

import javafx.application.Platform;
import javafx.scene.layout.VBox;

import javax.net.ssl.SSLSocket;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;

public class ApplicationController {
  Server server = null;
  Client client = null;
  public static Sender sender;
  public static Receiver receiver;
  public BroadcastReceiver br = null;
  public Thread serverStartThread;
  public Thread clientJoinThread;

  private SSLSocket activeSocket;
  public  VBox  chatView;

  public FXMLoader fl;

  /*---------------------------------------------------Server Controller Functions------------------------------------*/

  /**
   * Creates a new Server Object and closes any previous servers and clients
   * and initiates a new ssl configuration
   *
   * @param broadcastPort port number in which the broadcast to be sent ,i.e the destination
   */
  public void createServer(int broadcastPort) {

    resetApplication();

    //create a new Server Object
    server = new Server();

    //set initial configuration for the server
    server.initConfiguration();

    //start the server
    serverStartThread = new Thread(() -> server.start(broadcastPort));
    serverStartThread.start();

  }


  /**
   * @return Current Server object
   */
  public Server getServer() {
    return server;
  }


  /**
   * Stops the server  if it is not null
   */
  public void stopServer() {
    if (server != null) server.close();
  }

  /*----------------------------------------------Client Controller  Functions --------------------------------------------------------*/

  /**
   * Waits for broadcast and initialize tcp connection whwn received using the broadcast message
   */
  public void join() {

    //close running process (server or client)
    resetApplication();

    //create a new broadcast receiver
    br = new BroadcastReceiver();

    //create a new client object
    client = new Client();

    //create a new ssl configuration for client
    client.initConfiguration();

    System.out.println("Going inside the broadcast receive and connect code :: active Threads :: " + Thread.activeCount() + Thread.currentThread().getStackTrace()[1]);


    //start receiving broadcast and wait for broadcast message  and initialize tcp connection when received using the broadcast message
    clientJoinThread = new Thread(() -> {

      //this line blocks further execution until a broadcast is received
      DatagramPacket ipData = br.receiveBroadcast();

      //check if packet is received
      if (ipData == null) return;

      int tcp_port = Integer.parseInt(new String(ipData.getData()).trim());
      System.out.println("Server found at Ip: " + ipData.getAddress() + " port : " + tcp_port);

      try {
        client.joinNetwork(ipData.getAddress(), tcp_port);

        //returns to the thread after joining the network or when client disconnect
      } catch (ServerNotFoundException e) {
        e.printStackTrace();
      } finally {
        System.out.println(" Outside the broadcast receive and connect code Thread :: active Threads :: " + Thread.activeCount() + Thread.currentThread().getStackTrace()[1]);

      }

    });
    clientJoinThread.setName("Client Join Thread");
    clientJoinThread.start();
    System.out.println(" Outside the broadcast receive and connect code :: active Threads :: " + Thread.activeCount() + Thread.currentThread().getStackTrace()[1]);
  }

  /**
   * @return the client object
   */
  public Client getClient() {
    return client;
  }

/**
 * @return state - if true - client is connected else flase
 */
  public boolean isClientConnected(){
    if(getClient() != null)
      return this.getClient().getSslSocket().isConnected();
    else
      return false;
  }


  /**
   * Calls the disconnect method in client object
   */
  public void stopClient() {
    if (client != null) client.disconnect();
  }


  /**
   * Resets the Application to stop any running server
   */
  public void resetApplication() {
    if (br != null) br.close();
    stopClient();
    stopServer();
  }

  /**
   * revert to the initial UI
   */
  public void revertMainUI() {
    resetApplication();
    Platform.runLater(() -> {
      System.out.println("Inside platform run later that updates the infoCenter");
      try {
        MainApp.mainUI.setCenter(FXMLoader.getPage("infoCenter").load());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

  }
  /*------------------------------------------------------Data Handling controller ---------------------------------------------------------------------------------------------------------*/

  public void sendFile(File file){

    sender.sendFile(file,this.getActiveSocket());
  }


  public void setActiveSocket(SSLSocket socket){
    this.activeSocket= socket;
  }

  public SSLSocket getActiveSocket() {
    return activeSocket;
  }
}
