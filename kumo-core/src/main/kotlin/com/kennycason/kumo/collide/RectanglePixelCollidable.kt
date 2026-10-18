package com.kennycason.kumo.collide

import com.kennycason.kumo.collide.checkers.RectanglePixelCollisionChecker
import com.kennycason.kumo.image.CollisionRaster
import java.awt.Dimension
import java.awt.Point

class RectanglePixelCollidable(
    override val collisionRaster: CollisionRaster,
    override val position: Point
) : Collidable {

    override fun collide(collidable: Collidable): Boolean =
        RECTANGLE_PIXEL_COLLISION_CHECKER.collide(this, collidable)

    override val dimension: Dimension
        get() = collisionRaster.dimension

    companion object {
        private val RECTANGLE_PIXEL_COLLISION_CHECKER = RectanglePixelCollisionChecker()
    }
}
