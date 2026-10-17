package com.kennycason.kumo.image

import com.kennycason.kumo.Word
import java.awt.image.BufferedImage
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

object ImageRotation {

    @JvmStatic
    fun rotate90(bufferedImage: BufferedImage): BufferedImage =
        rotate(bufferedImage, Math.toRadians(90.0))

    @JvmStatic
    fun rotateMinus90(bufferedImage: BufferedImage): BufferedImage =
        rotate(bufferedImage, Math.toRadians(-90.0))

    @JvmStatic
    fun rotate(bufferedImage: BufferedImage, theta: Double): BufferedImage {
        if (theta == 0.0) return bufferedImage

        val sinVal = abs(sin(theta))
        val cosVal = abs(cos(theta))
        val width = bufferedImage.width
        val height = bufferedImage.height
        val newWidth = floor(width * cosVal + height * sinVal).toInt()
        val newHeight = floor(height * cosVal + width * sinVal).toInt()

        val result = BufferedImage(newWidth, newHeight, bufferedImage.type)
        val graphics = result.createGraphics()
        graphics.setRenderingHints(Word.getRenderingHints())
        graphics.translate((newWidth - width) / 2, (newHeight - height) / 2)
        graphics.rotate(theta, (width / 2).toDouble(), (height / 2).toDouble())
        graphics.drawRenderedImage(bufferedImage, null)
        graphics.dispose()

        return result
    }
}
