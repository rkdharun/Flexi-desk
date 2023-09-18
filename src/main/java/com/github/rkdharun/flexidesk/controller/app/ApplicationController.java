package com.github.rkdharun.flexidesk.controller.app;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.network.io.Client;
import com.github.rkdharun.flexidesk.network.io.Server;
import com.github.rkdharun.flexidesk.network.service.BroadcastReceiver;
import com.github.rkdharun.flexidesk.utilities.FXMLoader;
import com.github.rkdharun.flexidesk.utilities.ServerNotFoundException;
import javafx.application.Platform;

import java.io.IOException;
import java.net.DatagramPacket;

public class ApplicationController {
  Server server = null;
  Client client = null;

  public BroadcastReceiver br = null;


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
    server.start(broadcastPort);

  }


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
    new Thread(() -> {
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

    }).start();
    System.out.println(" Outside the broadcast receive and connect code :: active Threads :: " + Thread.activeCount() + Thread.currentThread().getStackTrace()[1]);
  }


  /**
   * @return Current Server object
   */
  public Server getServer() {
    return server;
  }

  public boolean getHasActiveConnection() {
    return isActiveConnectionRunning;
  }

  public void setActiveConnectionRunning(boolean activeConnectionRunning) {
    isActiveConnectionRunning = activeConnectionRunning;
  }

  private boolean isActiveConnectionRunning = false;

  /**
   * Calls the disconnect method in client object
   */
  public void stopClient() {
    if (client != null) client.disconnect();
  }

  /**
   * Stops the server  if it is not null
   */
  public void stopServer() {
    if (server != null) server.close();
  }

  public void resetApplication() {
    if (br != null) br.close();
    stopClient();
    stopServer();
  }

  public void revertMainUI() {
    resetApplication();
    Platform.runLater(() -> {
      try {
        MainApp.mainUI.setCenter(FXMLoader.getPage("infoCenter").load());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });


  }
}
