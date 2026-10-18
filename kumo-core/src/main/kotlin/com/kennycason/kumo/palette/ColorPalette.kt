package com.kennycason.kumo.palette

import java.awt.Color
import java.util.Random

open class ColorPalette {

    val colors: List<Color>
    private var next: Int = 0
    private val random = Random()

    constructor(vararg colors: Color) {
        this.colors = colors.toList()
    }

    constructor(vararg colors: Int) {
        this.colors = colors.map { Color(it) }
    }

    constructor(colors: List<Color>) {
        this.colors = colors.toList()
    }

    fun next(): Color = colors[next++ % colors.size]

    fun randomNext(random: Random = this.random): Color = colors[random.nextInt(colors.size)]
}
