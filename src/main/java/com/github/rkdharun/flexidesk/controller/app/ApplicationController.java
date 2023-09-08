package com.github.rkdharun.flexidesk.controller.app;

import com.github.rkdharun.flexidesk.network.io.Server;

public class ApplicationController {
  Server server = null;
  public void createServer() {

    if (server != null)
      server.close();

    //create a new Server Object
    server = new Server();

    //set initial configuration for the server
    server.initConfiguration();

    //start the server
    server.start();
  }

  public Server getServer() {
    return server;
  }

  public void stopServer() {
    if (server != null) server.close();
  }
}
