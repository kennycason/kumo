package com.kennycason.kumo.image;

import com.kennycason.kumo.Word;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by kenny on 6/29/14.
 */
public class ImageRotation {

    private ImageRotation() {}

    public static BufferedImage rotate90(final BufferedImage bufferedImage) {
        return rotate(bufferedImage, Math.toRadians(90));
    }

    public static BufferedImage rotateMinus90(final BufferedImage bufferedImage) {
        return rotate(bufferedImage, Math.toRadians(-90));
    }

    public static BufferedImage rotate(final BufferedImage bufferedImage, final double theta) {
        if (theta == 0.0) { return bufferedImage; }

        final double sin = Math.abs(Math.sin(theta));
        final double cos = Math.abs(Math.cos(theta));
        final int weight = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final int newWeight = (int) Math.floor(weight * cos + height * sin);
        final int newHeight = (int) Math.floor(height * cos + weight * sin);

        final BufferedImage result = new BufferedImage(newWeight, newHeight, bufferedImage.getType());
        final Graphics2D graphics = result.createGraphics();
        graphics.setRenderingHints(Word.getRenderingHints());
        graphics.translate((newWeight - weight) / 2, (newHeight - height) / 2);
        graphics.rotate(theta, weight / 2, height / 2);
        graphics.drawRenderedImage(bufferedImage, null);
        graphics.dispose();

        return result;
    }

}
