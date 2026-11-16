package com.kennycason.kumo.collide.checkers

import com.kennycason.kumo.collide.Collidable

class RectangleCollisionChecker : CollisionChecker {

    override fun collide(collidable: Collidable, collidable2: Collidable): Boolean {
        val position = collidable.position
        val position2 = collidable2.position

        if (position.x + collidable.dimension.getWidth() < position2.x ||
            position2.x + collidable2.dimension.getWidth() < position.x) {
            return false
        }
        if (position.y + collidable.dimension.getHeight() < position2.y ||
            position2.y + collidable2.dimension.getHeight() < position.y) {
            return false
        }
        return true
    }
}
