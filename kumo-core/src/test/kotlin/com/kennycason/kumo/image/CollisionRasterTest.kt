package com.kennycason.kumo.image

import com.kennycason.kumo.collide.Collidable
import com.kennycason.kumo.collide.checkers.RectanglePixelCollisionChecker
import org.junit.Assert.*
import org.junit.Test
import java.awt.Dimension
import java.awt.Point

class CollisionRasterTest {

    @Test
    fun basicTests() {
        val width = 10
        val height = 5
        val dimension = Dimension(width, height)
        val collisionRaster = CollisionRaster(dimension)

        assertEquals(width, collisionRaster.dimension.width)
        assertEquals(height, collisionRaster.dimension.height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                assertTrue(collisionRaster.isTransparent(x, y))
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

                for (y in 0 until height) {
                    for (x in 0 until width) {
                        if (x == xSet && y == ySet) {
                            assertFalse(collisionRaster.isTransparent(x, y))
                        } else {
                            assertTrue(collisionRaster.isTransparent(x, y))
                        }
                    }
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun illegalX() {
        val dimension = Dimension(90, 60)
        val collisionRaster = CollisionRaster(dimension)
        collisionRaster.setPixelIsNotTransparent(100, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun illegalY() {
        val dimension = Dimension(90, 60)
        val collisionRaster = CollisionRaster(dimension)
        collisionRaster.setPixelIsNotTransparent(0, 70)
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

        assertFalse(checker.collide(
            collidable(Point(30, 30), dimension, collisionRasterA),
            collidable(Point(30, 30), dimension, collisionRasterB)
        ))

        assertTrue(checker.collide(
            collidable(Point(30, 30), dimension, collisionRasterA),
            collidable(Point(20, 20), dimension, collisionRasterB)
        ))

        assertFalse(checker.collide(
            collidable(Point(20, 20), dimension, collisionRasterA),
            collidable(Point(30, 30), dimension, collisionRasterB)
        ))
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
