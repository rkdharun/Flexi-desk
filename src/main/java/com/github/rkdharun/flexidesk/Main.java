package com.github.rkdharun.flexidesk;

import com.github.rkdharun.flexidesk.controller.app.ApplicationController;

public class Main {
  public static void main(String[] args) {

    //Initialize the application controller
    MainApp.main(args);
    System.out.println("Active Threads  :: "+Thread.activeCount());

  }
}
