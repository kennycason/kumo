package com.kennycason.kumo.wordstart;

import com.kennycason.kumo.Word;
import com.kennycason.kumo.abst.DimensionAbst;
import com.kennycason.kumo.abst.PointAbst;

import java.util.Random;

/**
 * Always returns a random point in the image as starting point
 * 
 * @author &#64;wolfposd
 */
public class RandomWordStart implements WordStartStrategy {

    private static final Random RANDOM = new Random();

    @Override
    public PointAbst getStartingPoint(final DimensionAbst dimension, final Word word) {
        final int startX = RANDOM.nextInt(Math.max(dimension.getWidth() - word.getDimension().getWidth(), dimension.getWidth()));
        final int startY = RANDOM.nextInt(Math.max(dimension.getHeight() - word.getDimension().getHeight(), dimension.getHeight()));

        return PointAbst.get(startX, startY);
    }

}
