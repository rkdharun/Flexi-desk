package com.github.rkdharun.flexidesk.network.packets;

import java.io.Serializable;

public class HeaderPacket implements Serializable {
  private int headerType;
  public HeaderPacket() {

  }

  public void setHeader(int headerType) {
    this.headerType = headerType;
  }

  public int getHeader() {
    return headerType;
  }


}
