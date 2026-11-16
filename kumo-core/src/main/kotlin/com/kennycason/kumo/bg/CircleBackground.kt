package com.kennycason.kumo.bg

import com.kennycason.kumo.collide.RectanglePixelCollidable
import java.awt.Point

class CircleBackground(private val radius: Int) : Background {

    private val position = Point(0, 0)

    override fun mask(backgroundCollidable: RectanglePixelCollidable) {
        val dimensionOfBackground = backgroundCollidable.dimension
        val rasterOfBackground = backgroundCollidable.collisionRaster

        for (y in 0 until dimensionOfBackground.height) {
            for (x in 0 until dimensionOfBackground.width) {
                if (!inCircle(x, y)) {
                    rasterOfBackground.setPixelIsNotTransparent(position.x + x, position.y + y)
                }
            }
        }
    }

    private fun inCircle(x: Int, y: Int): Boolean {
        val centerX = position.x + x - radius
        val centerY = position.y + y - radius
        return (centerX * centerX) + (centerY * centerY) <= radius * radius
    }
}
