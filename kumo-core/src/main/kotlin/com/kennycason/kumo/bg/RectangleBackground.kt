package com.kennycason.kumo.bg

import com.kennycason.kumo.collide.RectanglePixelCollidable
import java.awt.Dimension
import java.awt.Point
import kotlin.math.max

class RectangleBackground(
    private val position: Point,
    private val dimension: Dimension
) : Background {

    constructor(dimension: Dimension) : this(ZERO, dimension)

    override fun mask(backgroundCollidable: RectanglePixelCollidable) {
        val minY = max(position.y, 0)
        val minX = max(position.x, 0)

        val maxY = dimension.height + position.y - 1
        val maxX = dimension.width + position.x - 1

        val dimensionOfBackground = backgroundCollidable.dimension
        val rasterOfBackground = backgroundCollidable.collisionRaster

        for (y in 0 until dimensionOfBackground.height) {
            for (x in 0 until dimensionOfBackground.width) {
                if (y < minY || y > maxY || x < minX || x > maxX) {
                    rasterOfBackground.setPixelIsNotTransparent(x, y)
                }
            }
        }
    }

    override fun toString(): String =
        "RectangleBackground [x=${position.x}, y=${position.y}, width=${dimension.width}, height=${dimension.height}]"

    companion object {
        private val ZERO = Point(0, 0)
    }
}
