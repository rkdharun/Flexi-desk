package com.github.rkdharun.flexidesk.network.packets;

import java.io.FileInputStream;
import java.io.Serializable;

public class FilePacket implements Serializable {
  private String fileName;
  private Long fileLength;

  private FileInputStream fileInputStream;

  public FilePacket(FilePacketBuilder filePacketBuilder) {
      fileName = filePacketBuilder.getFileName();
      fileLength = filePacketBuilder.getFileLength();
      fileInputStream = filePacketBuilder.getFileInputStream();
  }

  int getFileNameLength() {
    return fileName.length();
  }

  public String getFileName() {
    return fileName;
  }


  public Long getFileLength() {
    return fileLength;
  }

  FileInputStream getFileInputStream(){
    return this.fileInputStream;
  }



}
