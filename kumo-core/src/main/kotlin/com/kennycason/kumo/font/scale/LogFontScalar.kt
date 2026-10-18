package com.kennycason.kumo.font.scale

import kotlin.math.ln

class LogFontScalar(private val minFont: Int, private val maxFont: Int) : FontScalar {

    init {
        require(maxFont >= minFont) { "maxFont cannot be smaller than minFont" }
    }

    override fun scale(value: Int, maxValue: Int): Float {
        val leftSpan = if (maxValue == 1) 1.0 else ln(maxValue.toDouble())
        val rightSpan = (maxFont - minFont).toDouble()
        val valueScaled = (if (value == 1) 1.0 else ln(value.toDouble())) / leftSpan
        return (minFont + (valueScaled * rightSpan)).toFloat()
    }
}
