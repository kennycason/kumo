package com.kennycason.kumo.padding;

import com.kennycason.kumo.Word;
import com.kennycason.kumo.abst.GraphicsAbst;
import com.kennycason.kumo.abst.ImageAbst;

/**
 * Created by kenny on 7/2/14.
 */
public class RectanglePadder implements Padder {

    @Override
    public void pad(final Word word, final int padding) {
        if (padding <= 0) { return; }

        final ImageAbst image = word.getBufferedImage();
        final int width = image.getWidth() + padding * 2;
        final int height = image.getHeight() + padding * 2;

        final ImageAbst newImage = ImageAbst.get(width, height);
        final GraphicsAbst graphics = GraphicsAbst.get(newImage);
        graphics.drawImg(image, padding, padding);

        word.setImage(newImage);
    }

}
