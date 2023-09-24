package com.github.rkdharun.flexidesk.network.packets;


import java.io.FileInputStream;

public class FilePacketBuilder {

  private String fileName;
  private Long fileLength;
  private FileInputStream fileInputStream;


  public FilePacketBuilder setFileNames(String fileName) {
    this.fileName = fileName;
    return this;
  }

  public FilePacketBuilder setFileLength(Long fileLength) {
    this.fileLength = fileLength;
    return this;
  }

  public FilePacketBuilder setFileInputStream(FileInputStream fileInputStreamStream) {
    this.fileInputStream = fileInputStreamStream;
    return this;
  }


  public int getFileNameLength() {
    return fileName.length();
  }

  public String getFileName() {
    return fileName;
  }


  public Long getFileLength() {
    return fileLength;
  }

  public FileInputStream getFileInputStream(){
    return this.fileInputStream;
  }


  public FilePacket buildPacket() {
    return new FilePacket(this) ;
  }
}
