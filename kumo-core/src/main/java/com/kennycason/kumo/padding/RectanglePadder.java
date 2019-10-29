package com.kennycason.kumo.padding;

import com.kennycason.kumo.Word;
import com.kennycason.kumo.draw.Graphics;
import com.kennycason.kumo.draw.Image;

/**
 * Created by kenny on 7/2/14.
 */
public class RectanglePadder implements Padder {

    @Override
    public void pad(final Word word, final int padding) {
        if (padding <= 0) { return; }

        final Image image = word.getImage();
        final int width = image.getWidth() + padding * 2;
        final int height = image.getHeight() + padding * 2;

        final Image newImage = new Image(width, height);
        final Graphics graphics = new Graphics(newImage);
        graphics.drawImg(image, padding, padding);

        word.setImage(newImage);
    }

}
