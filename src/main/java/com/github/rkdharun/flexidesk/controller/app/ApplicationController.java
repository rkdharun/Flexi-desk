package com.github.rkdharun.flexidesk.controller.app;

import com.github.rkdharun.flexidesk.MainApp;
import com.github.rkdharun.flexidesk.network.io.Server;

public class ApplicationController {


  Server server = null;

  public void createServer() {
    //create a new Server Object
    if(server == null )
      server = new Server();
    else{
      server.close();
      server = new Server();
    }
    //set initial configuration for the server
    server.initConfiguration();
    //start the server
    server.start();
  }

  public Server getServer() {
    return server;
  }
}
