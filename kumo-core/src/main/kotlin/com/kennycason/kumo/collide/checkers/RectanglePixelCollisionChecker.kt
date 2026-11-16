package com.kennycason.kumo.collide.checkers

import com.kennycason.kumo.collide.Collidable
import kotlin.math.max
import kotlin.math.min

class RectanglePixelCollisionChecker : CollisionChecker {

    override fun collide(collidable: Collidable, collidable2: Collidable): Boolean {
        if (!RECTANGLE_COLLISION_CHECKER.collide(collidable, collidable2)) {
            return false
        }

        val position = collidable.position
        val position2 = collidable2.position
        val collisionRaster = collidable.collisionRaster
        val collisionRaster2 = collidable2.collisionRaster

        val startX = max(position.x, position2.x)
        val endX = min(
            position.x + collidable.dimension.width,
            position2.x + collidable2.dimension.width
        )

        val startY = max(position.y, position2.y)
        val endY = min(
            position.y + collidable.dimension.height,
            position2.y + collidable2.dimension.height
        )

        val endX1 = endX - position.x
        val endX2 = endX - position2.x

        val stop1 = position.x + -1
        val stop2 = position2.x + -1

        val stepSize = ((endY - startY) / 3) + 1

        for (i in stepSize - 1 downTo 0) {
            var y = startY + i
            while (y < endY) {
                val yOfPosition = y - position.y
                var absolute1 = position.x + collisionRaster.nextNotTransparentPixel(
                    startX - position.x, endX1, yOfPosition
                )

                if (absolute1 == stop1) {
                    y += stepSize
                    continue
                }

                val yOfPosition2 = y - position2.y
                var absolute2 = position2.x + collisionRaster2.nextNotTransparentPixel(
                    startX - position2.x, endX2, yOfPosition2
                )

                if (absolute2 == stop2) {
                    y += stepSize
                    continue
                }

                while (true) {
                    if (absolute1 > absolute2) {
                        absolute2 = position2.x + collisionRaster2.nextNotTransparentPixel(
                            absolute1 - position2.x, endX2, yOfPosition2
                        )
                        if (absolute2 == stop2) break
                    } else if (absolute1 < absolute2) {
                        absolute1 = position.x + collisionRaster.nextNotTransparentPixel(
                            absolute2 - position.x, endX1, yOfPosition
                        )
                        if (absolute1 == stop1) break
                    } else {
                        return true
                    }
                }
                y += stepSize
            }
        }
        return false
    }

    companion object {
        private val RECTANGLE_COLLISION_CHECKER = RectangleCollisionChecker()
    }
}
