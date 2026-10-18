package com.kennycason.kumo.palette

import java.awt.Color
import java.util.Random

open class ColorPalette {

    val colors: List<Color>
    private var next: Int = 0

    constructor(vararg colors: Color) {
        this.colors = colors.toList()
    }

    constructor(vararg colors: Int) {
        this.colors = colors.map { Color(it) }
    }

    constructor(colors: List<Color>) {
        this.colors = colors
    }

    fun next(): Color = colors[next++ % colors.size]

    fun randomNext(): Color = colors[RANDOM.nextInt(colors.size)]

    companion object {
        private val RANDOM = Random()
    }
}
