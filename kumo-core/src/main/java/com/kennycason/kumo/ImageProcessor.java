package com.kennycason.kumo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
*Class to process image with a required size.
*
* the static method {@link #readImage(String fileName, int width, int height, String imageType)}
* can read the path of the file and resize the image
 */
public final class ImageProcessor {

    private ImageProcessor(){}
    private static ImageProcessor imageProcessor = new ImageProcessor();
    public static ImageProcessor getInstance(){
        return imageProcessor;
    }
    /**
    *@param fileName is the path of the file
     * @param imageType can define the type of image
     * @param width is the resized image's width
     * @param height is the resized image's height
     * @return the resized image in ByteArrayInputStream form
     */
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
