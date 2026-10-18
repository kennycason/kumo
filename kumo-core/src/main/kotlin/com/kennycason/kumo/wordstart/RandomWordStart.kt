package com.kennycason.kumo.wordstart

import com.kennycason.kumo.Word
import java.awt.Dimension
import java.awt.Point
import java.util.Random
import kotlin.math.max

class RandomWordStart(private val random: Random = Random()) : WordStartStrategy {

    override fun getStartingPoint(dimension: Dimension, word: Word): Point =
        getStartingPoint(dimension, word, random)

    override fun getStartingPoint(dimension: Dimension, word: Word, random: Random): Point {
        val startX = random.nextInt(max(dimension.width - word.dimension.width, dimension.width))
        val startY = random.nextInt(max(dimension.height - word.dimension.height, dimension.height))
        return Point(startX, startY)
    }
}
