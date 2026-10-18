package com.kennycason.kumo.font.scale

class LinearFontScalar(private val minFont: Int, private val maxFont: Int) : FontScalar {

    init {
        require(maxFont >= minFont) { "maxFont cannot be smaller than minFont" }
    }

    override fun scale(value: Int, maxValue: Int): Float {
        val leftSpan = maxValue.toFloat()
        val rightSpan = (maxFont - minFont).toFloat()
        val valueScaled = value / leftSpan
        return minFont + (valueScaled * rightSpan)
    }
}
