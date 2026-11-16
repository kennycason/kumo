package com.kennycason.kumo.collide

import com.kennycason.kumo.image.CollisionRaster
import java.awt.Dimension
import java.awt.Point

interface Collidable {
    fun collide(collidable: Collidable): Boolean
    val position: Point
    val dimension: Dimension
    val collisionRaster: CollisionRaster
}
