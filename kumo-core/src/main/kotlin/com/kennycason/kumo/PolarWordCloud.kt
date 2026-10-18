package com.kennycason.kumo

import com.kennycason.kumo.palette.ColorPalette
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.util.Random
import kotlin.math.pow
import kotlin.math.sqrt

class PolarWordCloud : WordCloud {

    private val polarBlendMode: PolarBlendMode

    @set:JvmName("setColorPalette2")
    var colorPalette2: ColorPalette

    constructor(dimension: Dimension, collisionMode: CollisionMode) :
            this(dimension, collisionMode, PolarBlendMode.EVEN) {
        this.colorPalette = DEFAULT_POSITIVE_COLORS
        this.colorPalette2 = DEFAULT_NEGATIVE_COLORS
    }

    constructor(
        dimension: Dimension,
        collisionMode: CollisionMode,
        polarBlendMode: PolarBlendMode
    ) : super(dimension, collisionMode) {
        this.polarBlendMode = polarBlendMode
        this.colorPalette = DEFAULT_POSITIVE_COLORS
        this.colorPalette2 = DEFAULT_NEGATIVE_COLORS
    }

    fun build(wordFrequencies: List<WordFrequency>, wordFrequencies2: List<WordFrequency>) {
        val sorted1 = wordFrequencies.sorted()
        val sorted2 = wordFrequencies2.sorted()

        val words = buildWords(sorted1, colorPalette)
        val words2 = buildWords(sorted2, colorPalette2)

        val wordIterator = words.iterator()
        val wordIterator2 = words2.iterator()

        val (pole1, pole2) = getRandomPoles()

        background.mask(backgroundCollidable)

        while (wordIterator.hasNext() || wordIterator2.hasNext()) {
            if (wordIterator.hasNext()) {
                val word = wordIterator.next()
                val startPosition = getStartPosition(pole1)
                place(word, startPosition)
            }
            if (wordIterator2.hasNext()) {
                val word = wordIterator2.next()
                val startPosition = getStartPosition(pole2)
                place(word, startPosition)
            }
        }

        drawForegroundToBackground()
    }

    private fun getStartPosition(pole: Point): Point = when (polarBlendMode) {
        PolarBlendMode.BLUR -> {
            val blurX = dimension.width / 2
            val blurY = dimension.height / 2
            Point(
                pole.x + -blurX + RANDOM.nextInt(blurX * 2),
                pole.y + -blurY + RANDOM.nextInt(blurY * 2)
            )
        }
        PolarBlendMode.EVEN -> pole
    }

    private fun getRandomPoles(): Pair<Point, Point> {
        var best1 = Point(0, 0)
        var best2 = Point(dimension.width, dimension.height)
        var maxDistance = 0.0
        for (i in 0 until 100) {
            val x = RANDOM.nextInt(dimension.width)
            val y = RANDOM.nextInt(dimension.height)
            val x2 = RANDOM.nextInt(dimension.width)
            val y2 = RANDOM.nextInt(dimension.height)
            val distance = sqrt((x - x2).toDouble().pow(2) + (y - y2).toDouble().pow(2))
            if (distance > maxDistance) {
                maxDistance = distance
                best1 = Point(x, y)
                best2 = Point(x2, y2)
            }
        }
        return Pair(best1, best2)
    }

    companion object {
        private val RANDOM = Random()

        private val DEFAULT_POSITIVE_COLORS = ColorPalette(
            Color(0x1BE000), Color(0x1AC902), Color(0x15B000),
            Color(0x129400), Color(0x0F7A00), Color(0x0B5E00)
        )

        private val DEFAULT_NEGATIVE_COLORS = ColorPalette(
            Color(0xF50000), Color(0xDE0000), Color(0xC90202),
            Color(0xB50202), Color(0x990202), Color(0x800101)
        )
    }
}
