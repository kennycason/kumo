package com.kennycason.kumo.bg

import com.kennycason.kumo.collide.RectanglePixelCollidable
import com.kennycason.kumo.image.CollisionRaster
import java.awt.Point
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO
import kotlin.math.max

class PixelBoundaryBackground private constructor(bufferedImage: BufferedImage) : Background {

    private val collisionRaster = CollisionRaster(bufferedImage)
    private val position = Point(0, 0)

    /** Reads from a caller-owned stream without closing it. */
    constructor(imageInputStream: InputStream) : this(ImageIO.read(imageInputStream))

    constructor(file: File) : this(file.inputStream().use { ImageIO.read(it) })

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
