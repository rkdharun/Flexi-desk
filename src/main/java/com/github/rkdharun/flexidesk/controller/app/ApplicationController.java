package com.github.rkdharun.flexidesk.controller.app;

import com.github.rkdharun.flexidesk.network.io.BroadcastReceiver;
import com.github.rkdharun.flexidesk.network.io.Client;
import com.github.rkdharun.flexidesk.network.io.Server;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class ApplicationController {
  Server server = null;
  Client client = null;

  BroadcastReceiver br = null;

  /**
   * Creates a new Server Object and closes any previous servers and clients
   * and initiates a new ssl configuration
   */
  public void createServer() {

    //close any previously running clients or servers
    if(client!=null)
      client.disconnect();
    if (server != null)
      server.close();

    //create a new Server Object
    server = new Server();

    //set initial configuration for the server
    server.initConfiguration();

    //start the server
    server.start();

  }


  /**
   * @return Current Server object
   */
  public Server getServer() {
    return server;
  }

  /**
   * stops the server  if it is not null
   */
  public void stopServer() {
    if (server != null) server.close();
  }


  /**
   * Discovers broadcast and initialize tcp connection
   * @param port port number that the broadcast receiver uses to receive connection information
   * */
  public void join(int port) {
    //close previous connections
    if(server!=null) server.close();
    if(client!=null) client.disconnect();

    //create a new client object
    client = new Client();

    //create a new ssl configuration for client
    client.initConfiguration();

    //closes any running broadcast receivers
    if(br!=null)br.close();

    //create a new broadcast receiver
    br = new BroadcastReceiver();

    //start receiving the broadcast
    DatagramPacket ipData = br.receiveBroadcast(port); //this line blocks further execution untill a broadcast is received
    //check if packet is received
    if(ipData == null) return;
    System.out.println("going against return");
    int tcp_port = Integer.parseInt(new String(ipData.getData()).trim());
    System.out.println("Received data of tcp port: "+tcp_port);
    System.out.println("Ip address found : "+ipData.getAddress());
    client.joinNetwork(ipData.getAddress(),tcp_port);


  }

  /**
   * calls the disconnect method in client object
   */
  public void stopClient() {
    if(client!=null)client.disconnect();
  }
}
