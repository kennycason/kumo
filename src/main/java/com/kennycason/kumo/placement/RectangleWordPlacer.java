package com.kennycason.kumo.placement;

import com.kennycason.kumo.Word;

/**
 * Created by kenny on 2/21/16.
 */
public interface RectangleWordPlacer {
    void reset();
    boolean place(Word word);
}
