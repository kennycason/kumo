package com.kennycason.kumo.collide.checkers

import com.kennycason.kumo.collide.Collidable

interface CollisionChecker {
    fun collide(collidable: Collidable, collidable2: Collidable): Boolean
}
