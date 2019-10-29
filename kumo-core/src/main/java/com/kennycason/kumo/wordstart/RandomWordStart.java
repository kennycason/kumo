package com.kennycason.kumo.wordstart;

import com.kennycason.kumo.Word;
import com.kennycason.kumo.draw.Dimension;
import com.kennycason.kumo.draw.Point;

import java.util.Random;

/**
 * Always returns a random point in the image as starting point
 * 
 * @author &#64;wolfposd
 */
public class RandomWordStart implements WordStartStrategy {

    private static final Random RANDOM = new Random();

    @Override
    public Point getStartingPoint(final Dimension dimension, final Word word) {
        final int startX = RANDOM.nextInt(Math.max(dimension.getWidth() - word.getDimension().getWidth(), dimension.getWidth()));
        final int startY = RANDOM.nextInt(Math.max(dimension.getHeight() - word.getDimension().getHeight(), dimension.getHeight()));

        return new Point(startX, startY);
    }

}
