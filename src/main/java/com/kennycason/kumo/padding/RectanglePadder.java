package com.kennycason.kumo.padding;

import com.kennycason.kumo.Word;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by kenny on 7/2/14.
 */
public class RectanglePadder implements Padder {

    @Override
    public void pad(final Word word, final int padding) {
        if (padding <= 0) { return; }

        final BufferedImage bufferedImage = word.getBufferedImage();
        final int width = bufferedImage.getWidth() + padding * 2;
        final int height = bufferedImage.getHeight() + padding * 2;

        final BufferedImage newBufferedImage = new BufferedImage(width, height, bufferedImage.getType());
        final Graphics graphics = newBufferedImage.getGraphics();
        graphics.drawImage(bufferedImage, padding, padding, null);

        word.setBufferedImage(newBufferedImage);
    }

}
