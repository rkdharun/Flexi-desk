package com.github.rkdharun.flexidesk.network.packets;

import java.io.FileInputStream;

public class ClipboardPacketBuilder {


    private int dataLength;

    private String dataType;

    private byte[] data;


  public ClipboardPacketBuilder setDataLength(int dataLength) {
    this.dataLength = dataLength;
    return this;
  }

  public ClipboardPacketBuilder setDataType(String dataType) {
    this.dataType = dataType;
    return this;
  }

  public ClipboardPacketBuilder setData(byte[] data) {
    this.data = data;
    return this;
  }


    int getDataLength() {
      return dataLength;
    }

    String getDataType() {
      return dataType;
    }


    byte[] getData() {
      return data;
    }

    public ClipboardPacket buildPacket() {
      return new ClipboardPacket(this) ;
    }
  }


