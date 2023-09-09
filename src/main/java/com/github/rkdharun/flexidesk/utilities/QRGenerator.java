package com.github.rkdharun.flexidesk.utilities;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.WriterException;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;


public class QRGenerator {
  public static Image createQRImage(String qrCodeText, int size)
    throws WriterException, IOException {

    // Create the ByteMatrix for the QR-Code that encodes the given String
    Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);

    // Make the BufferedImage that are to hold the QRCode
    int matrixWidth = byteMatrix.getWidth();
    BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
    image.createGraphics();
    Graphics2D graphics = (Graphics2D) image.getGraphics();
    graphics.setColor(Color.BLACK);
    graphics.fillRect(0, 0, matrixWidth, matrixWidth);

    // Paint and save the image using the ByteMatrix
    graphics.setColor(Color.WHITE);

    for (int i = 0; i < matrixWidth; i++) {
      for (int j = 0; j < matrixWidth; j++) {
        if (byteMatrix.get(i, j)) {
          graphics.fillRect(i, j, 1, 1);
        }
      }
    }
    ImageIO.write(image, "png", new File("portal.png"));

    return convertToFxImage(image);
  }


  /**
   * @param image a BufferedImage object
   * @return an Image object of javafx.scene.image.Image type
   */
  private static Image convertToFxImage(BufferedImage image) {
    WritableImage wr = null;
    if (image != null) {
      wr = new WritableImage(image.getWidth(), image.getHeight());
      PixelWriter pw = wr.getPixelWriter();
      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          pw.setArgb(x, y, image.getRGB(x, y));
        }
      }
    }
    return new ImageView(wr).getImage();
  }
}
