package com.kennycason.kumo.image

import java.awt.Dimension
import java.awt.Point
import java.awt.image.BufferedImage
import java.util.BitSet
import kotlin.math.min

class CollisionRaster {

    private val data: BitSet
    val dimension: Dimension

    constructor(bufferedImage: BufferedImage) : this(Dimension(bufferedImage.width, bufferedImage.height)) {
        for (y in 0 until dimension.height) {
            for (x in 0 until dimension.width) {
                val pixelIsTransparent = (bufferedImage.getRGB(x, y) and 0xFF000000.toInt()) == 0x00000000
                if (!pixelIsTransparent) {
                    setPixelIsNotTransparent(x, y)
                }
            }
        }
    }

    constructor(dimension: Dimension) {
        this.dimension = dimension
        data = BitSet(dimension.width * dimension.height)
    }

    constructor(other: CollisionRaster) {
        this.dimension = other.dimension
        this.data = other.data.clone() as BitSet
    }

    private fun computeIndex(x: Int, y: Int): Int {
        require(x in 0 until dimension.width) { "x is out of bounds" }
        require(y in 0 until dimension.height) { "y is out of bounds" }
        return (y * dimension.width) + x
    }

    fun setPixelIsNotTransparent(x: Int, y: Int) {
        data.set(computeIndex(x, y))
    }

    fun clear() {
        data.clear()
    }

    fun mask(collisionRaster: CollisionRaster, point: Point) {
        val maxHeight = min(point.y + collisionRaster.dimension.height, dimension.height)
        val maxWidth = min(point.x + collisionRaster.dimension.width, dimension.width)

        var offY = point.y
        var offY2 = 0
        while (offY < maxHeight) {
            var offX = point.x
            var offX2 = 0
            while (offX < maxWidth) {
                if (!collisionRaster.isTransparent(offX2, offY2)) {
                    setPixelIsNotTransparent(offX, offY)
                }
                offX++
                offX2++
            }
            offY++
            offY2++
        }
    }

    fun nextNotTransparentPixel(minX: Int, maxX: Int, y: Int): Int {
        require(maxX <= dimension.width) { "maxX is out of bounds" }

        val idx = computeIndex(minX, y)
        val set = data.nextSetBit(idx)

        return if (set != -1 && set < idx + maxX - minX) {
            (set - idx) + minX
        } else {
            -1
        }
    }

    fun isTransparent(x: Int, y: Int): Boolean = !data.get(computeIndex(x, y))
}
