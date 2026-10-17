package com.kennycason.kumo.wordstart

import com.kennycason.kumo.Word
import java.awt.Dimension
import java.awt.Point
import java.util.Random
import kotlin.math.max

class RandomWordStart : WordStartStrategy {

    override fun getStartingPoint(dimension: Dimension, word: Word): Point {
        val startX = RANDOM.nextInt(max(dimension.width - word.dimension.width, dimension.width))
        val startY = RANDOM.nextInt(max(dimension.height - word.dimension.height, dimension.height))
        return Point(startX, startY)
    }

    companion object {
        private val RANDOM = Random()
    }
}
