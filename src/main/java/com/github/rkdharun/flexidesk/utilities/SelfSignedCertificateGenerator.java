package com.github.rkdharun.flexidesk.utilities;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

public class SelfSignedCertificateGenerator {

  public KeyPair keyPair;

  public X509Certificate[] generate() throws Exception {
    Security.addProvider(new BouncyCastleProvider());

    // Generate Key Pair
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
    keyPairGenerator.initialize(2048); // Change the key size as needed
    keyPair = keyPairGenerator.generateKeyPair();

    // Generate Certificate
    X500Name issuerName = new X500Name("CN=SelfSigned");
    BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

    // Valid from now to 1 year
    Date notBefore = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24); // 1 day before
    Date notAfter = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365); // 1 year after

    SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
    X509v3CertificateBuilder certificateBuilder = new X509v3CertificateBuilder(issuerName, serialNumber, notBefore, notAfter, issuerName, publicKeyInfo);

    ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());
    X509CertificateHolder certificate = certificateBuilder.build(contentSigner);

    // Write the certificate to a file
    FileOutputStream fs = new FileOutputStream("Certificate.pem");
    fs.write(keyPair.getPublic().toString().getBytes());
    fs.close();

    // Return the signed certificate from the X509CertificateHolder by converting to X509Certificate
    X509Certificate cert = new JcaX509CertificateConverter().getCertificate(certificate);

    return new X509Certificate[]{cert};
  }
}
