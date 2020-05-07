package com.kennycason.kumo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ImageProcessor {
    /*
    Function to process image with a required size.
    Get the path of the file
    image_type can define the type of image
     */
    private ImageProcessor(){}
    public static InputStream readImage(String fileName, int width, int height, String imageType) throws IOException, InterruptedException {
        BufferedImage originImage;
        originImage = ImageIO.read(getInputStream(fileName));

        Image scaledImage = originImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage;

        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);


        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, imageType, outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
    private static InputStream getInputStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

}
