package com.kennycason.kumo.bg

import com.kennycason.kumo.collide.RectanglePixelCollidable

interface Background {
    fun mask(backgroundCollidable: RectanglePixelCollidable)
}
