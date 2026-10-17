package com.kennycason.kumo.bg

import com.kennycason.kumo.collide.RectanglePixelCollidable
import com.kennycason.kumo.image.CollisionRaster
import java.awt.Point
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO
import kotlin.math.max

class PixelBoundaryBackground : Background {

    private val collisionRaster: CollisionRaster
    private val position = Point(0, 0)

    @Throws(IOException::class)
    constructor(imageInputStream: InputStream) {
        val bufferedImage = ImageIO.read(imageInputStream)
        this.collisionRaster = CollisionRaster(bufferedImage)
    }

    @Throws(IOException::class)
    constructor(file: File) : this(FileInputStream(file))

    @Throws(IOException::class)
    constructor(filepath: String) : this(File(filepath))

    override fun mask(backgroundCollidable: RectanglePixelCollidable) {
        val dimensionOfShape = collisionRaster.dimension
        val dimensionOfBackground = backgroundCollidable.dimension

        val minY = max(position.y, 0)
        val minX = max(position.x, 0)

        val maxY = dimensionOfShape.height - position.y - 1
        val maxX = dimensionOfShape.width - position.x - 1

        val rasterOfBackground = backgroundCollidable.collisionRaster

        for (y in 0 until dimensionOfBackground.height) {
            if (y < minY || y > maxY) {
                for (x in 0 until dimensionOfBackground.width) {
                    rasterOfBackground.setPixelIsNotTransparent(position.x + x, position.y + y)
                }
            } else {
                for (x in 0 until dimensionOfBackground.width) {
                    if (x < minX || x > maxX || collisionRaster.isTransparent(x, y)) {
                        rasterOfBackground.setPixelIsNotTransparent(position.x + x, position.y + y)
                    }
                }
            }
        }
    }
}
