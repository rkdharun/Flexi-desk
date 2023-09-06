package com.github.rkdharun.flexidesk.config;

import javax.net.ssl.X509TrustManager;


public class DefaultTrustManager implements X509TrustManager {
  @Override
  public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
  }

  @Override
  public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
  }

  @Override
  public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    return new java.security.cert.X509Certificate[]{};
  }
}
