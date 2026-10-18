package com.kennycason.kumo.wordstart

import com.kennycason.kumo.Word
import java.awt.Dimension
import java.awt.Point
import java.util.Random

/**
 * The WordStartStrategy provides a possible starting position for a word on the image.
 */
interface WordStartStrategy {
    /**
     * Calculate a starting point for the given word. The returned Point is not
     * necessarily the actual point on the image, rather a first try.
     *
     * @param dimension width/height of the image
     * @param word the word to be placed
     * @return X/Y starting position
     */
    fun getStartingPoint(dimension: Dimension, word: Word): Point

    /** Override this when a custom strategy needs the build's seeded random source. */
    fun getStartingPoint(dimension: Dimension, word: Word, random: Random): Point =
        getStartingPoint(dimension, word)
}
