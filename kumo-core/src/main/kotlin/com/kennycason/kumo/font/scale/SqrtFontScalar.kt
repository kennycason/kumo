package com.kennycason.kumo.font.scale

import kotlin.math.sqrt

class SqrtFontScalar(private val minFont: Int, private val maxFont: Int) : FontScalar {

    init {
        require(maxFont >= minFont) { "maxFont cannot be smaller than minFont" }
    }

    override fun scale(value: Int, maxValue: Int): Float {
        val leftSpan = sqrt(maxValue.toDouble())
        val rightSpan = (maxFont - minFont).toDouble()
        val valueScaled = sqrt(value.toDouble()) / leftSpan
        return (minFont + (valueScaled * rightSpan)).toFloat()
    }
}
