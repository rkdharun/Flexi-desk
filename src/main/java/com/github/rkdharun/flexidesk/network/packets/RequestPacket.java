package com.github.rkdharun.flexidesk.network.packets;

import java.io.Serializable;

public class RequestPacket implements Serializable {

  int packetType = 12;
  boolean isAccepted= false;

  public boolean isAccepted() {
    return isAccepted;
  }

  public void setAccepted(boolean accepted) {
    isAccepted = accepted;
  }
}
