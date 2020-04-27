package com.kennycason.kumo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageProcessor {
    /*
    Function to process image with a required size.
    Get the path of the file
    image_type can define the type of image
     */
    public static InputStream readImage(String file_name, int width, int height, String image_type) throws IOException, InterruptedException {
        BufferedImage origin_image;
        origin_image = ImageIO.read(getInputStream(file_name));

        Image scaledImage = origin_image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage;

        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);


        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, image_type, outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
    private static InputStream getInputStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

}
