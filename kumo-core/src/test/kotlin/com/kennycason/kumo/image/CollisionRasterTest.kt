package com.kennycason.kumo.image

import com.kennycason.kumo.collide.Collidable
import com.kennycason.kumo.collide.checkers.RectanglePixelCollisionChecker
import java.awt.Dimension
import java.awt.Point
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class CollisionRasterTest {

    @Test
    fun basicTests() {
        val width = 10
        val height = 5
        val dimension = Dimension(width, height)
        val collisionRaster = CollisionRaster(dimension)

        expectThat(collisionRaster.dimension.width).isEqualTo(width)
        expectThat(collisionRaster.dimension.height).isEqualTo(height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                expectThat(collisionRaster.isTransparent(x, y)).isTrue()
            }
        }
    }

    @Test
    fun correctPixelIsTransparent() {
        val width = 90
        val height = 30

        for (ySet in 0 until height) {
            for (xSet in 0 until width) {
                val dimension = Dimension(width, height)
                val collisionRaster = CollisionRaster(dimension)

                collisionRaster.setPixelIsNotTransparent(xSet, ySet)

                val opaquePixels = buildList {
                    for (y in 0 until height) {
                        for (x in 0 until width) {
                            if (!collisionRaster.isTransparent(x, y)) {
                                add(Point(x, y))
                            }
                        }
                    }
                }
                expectThat(opaquePixels).containsExactly(Point(xSet, ySet))
            }
        }
    }

    @Test
    fun illegalX() {
        expectThrows<IllegalArgumentException> {
            val dimension = Dimension(90, 60)
            val collisionRaster = CollisionRaster(dimension)
            collisionRaster.setPixelIsNotTransparent(100, 0)
        }
    }

    @Test
    fun illegalY() {
        expectThrows<IllegalArgumentException> {
            val dimension = Dimension(90, 60)
            val collisionRaster = CollisionRaster(dimension)
            collisionRaster.setPixelIsNotTransparent(0, 70)
        }
    }

    @Test
    fun collisionsAreFound() {
        val checker = RectanglePixelCollisionChecker()

        val dimension = Dimension(90, 60)
        val collisionRasterA = CollisionRaster(dimension)
        val collisionRasterB = CollisionRaster(dimension)

        for (x in 0 until 10) {
            for (y in 0 until 10) {
                collisionRasterA.setPixelIsNotTransparent(20 + x, 20 + y)
                collisionRasterB.setPixelIsNotTransparent(30 + x, 30 + y)
            }
        }

        expectThat(checker.collide(
            collidable(Point(30, 30), dimension, collisionRasterA),
            collidable(Point(30, 30), dimension, collisionRasterB)
        )).isFalse()

        expectThat(checker.collide(
            collidable(Point(30, 30), dimension, collisionRasterA),
            collidable(Point(20, 20), dimension, collisionRasterB)
        )).isTrue()

        expectThat(checker.collide(
            collidable(Point(20, 20), dimension, collisionRasterA),
            collidable(Point(30, 30), dimension, collisionRasterB)
        )).isFalse()
    }

    private fun collidable(p: Point, d: Dimension, r: CollisionRaster): Collidable {
        return object : Collidable {
            override fun collide(collidable: Collidable): Boolean =
                throw UnsupportedOperationException("Not supported yet.")

            override val position: Point = p
            override val dimension: Dimension = d
            override val collisionRaster: CollisionRaster = r
        }
    }
}
