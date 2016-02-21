package com.kennycason.kumo.wsc;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

import com.kennycason.kumo.Word;

/**
 * Always returns a random point in the image as starting point
 * 
 * @author &#64;wolfposd
 */
public class RandomWordStart implements WordStartScheme {

    private static final Random RANDOM = new Random();

    @Override
    public Point getStartingPoint(Dimension imagedimensions, Word word) {
        final int startX = RANDOM.nextInt(Math.max(imagedimensions.width - word.getWidth(), imagedimensions.width));
        final int startY = RANDOM.nextInt(Math.max(imagedimensions.height - word.getHeight(), imagedimensions.height));
        return new Point(startX, startY);
    }

}
