package com.kennycason.kumo.draw.awtimpl;

import com.kennycason.kumo.draw.IImage;
import com.kennycason.kumo.draw.IImageRotator;
import com.kennycason.kumo.draw.VaryingImpl;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageRotatorImpl implements IImageRotator {

    @Override
    public IImage rotate(IImage image, double theta) {

        theta = degreesToRadians(theta);

        if (theta == 0.0) { return image; }

        BufferedImage bufferedImage = (BufferedImage) image.getActual();

        final double sin = Math.abs(Math.sin(theta));
        final double cos = Math.abs(Math.cos(theta));
        final int weight = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final int newWeight = (int) Math.floor(weight * cos + height * sin);
        final int newHeight = (int) Math.floor(height * cos + weight * sin);

        final BufferedImage result = new BufferedImage(newWeight, newHeight, bufferedImage.getType());
        final Graphics2D graphics = result.createGraphics();
        graphics.translate((newWeight - weight) / 2, (newHeight - height) / 2);
        graphics.rotate(theta, weight / 2, height / 2);
        graphics.drawRenderedImage(bufferedImage, null);
        graphics.dispose();

        return new ImageImpl(result);
    }

    private double degreesToRadians(final double degrees) {
        return Math.PI * degrees / 180.0;
    }
}
