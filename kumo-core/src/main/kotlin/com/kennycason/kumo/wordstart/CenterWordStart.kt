package com.kennycason.kumo.wordstart

import com.kennycason.kumo.Word
import java.awt.Dimension
import java.awt.Point

class CenterWordStart : WordStartStrategy {

    override fun getStartingPoint(dimension: Dimension, word: Word): Point {
        val x = (dimension.width / 2) - (word.dimension.width / 2)
        val y = (dimension.height / 2) - (word.dimension.height / 2)
        return Point(x, y)
    }
}
