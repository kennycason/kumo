package com.kennycason.kumo.wordstart;

import com.kennycason.kumo.Word;
import com.kennycason.kumo.abst.DimensionAbst;
import com.kennycason.kumo.abst.PointAbst;

/**
 * Always returns the Center of the image
 * 
 * @author &#64;wolfposd
 */
public class CenterWordStart implements WordStartStrategy {

    @Override
    public PointAbst getStartingPoint(final DimensionAbst dimension, final Word word) {
        final int x = (dimension.getWidth() / 2) - (word.getDimension().getWidth() / 2);
        final int y = (dimension.getHeight() / 2) - (word.getDimension().getHeight() / 2);

        return PointAbst.get(x, y);
    }

}
