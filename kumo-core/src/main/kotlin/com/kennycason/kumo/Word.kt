package com.kennycason.kumo

import com.kennycason.kumo.collide.Collidable
import com.kennycason.kumo.collide.checkers.CollisionChecker
import com.kennycason.kumo.image.CollisionRaster
import com.kennycason.kumo.image.ImageRotation
import java.awt.*
import java.awt.image.BufferedImage

class Word(
    val word: String,
    private val color: Color,
    fontMetrics: FontMetrics,
    private val collisionChecker: CollisionChecker,
    val theta: Double
) : Collidable {

    var bufferedImage: BufferedImage = render(word, color, fontMetrics, theta)
        set(value) {
            field = value
            collisionRaster = CollisionRaster(value)
        }

    override var collisionRaster: CollisionRaster = CollisionRaster(bufferedImage)
        private set

    override val position: Point = Point(0, 0)

    override val dimension: Dimension
        get() = collisionRaster.dimension

    private fun render(text: String, fontColor: Color, fontMetrics: FontMetrics, theta: Double): BufferedImage {
        val width = fontMetrics.stringWidth(text)
        val height = fontMetrics.height

        val rendered = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val gOfRendered = rendered.graphics as Graphics2D

        gOfRendered.setRenderingHints(getRenderingHints())
        gOfRendered.color = fontColor
        gOfRendered.font = fontMetrics.font

        gOfRendered.drawString(text, 0, height - fontMetrics.maxDescent - fontMetrics.leading)

        return ImageRotation.rotate(rendered, theta)
    }

    override fun collide(collidable: Collidable): Boolean =
        collisionChecker.collide(this, collidable)

    fun draw(collisionRaster: CollisionRaster) {
        collisionRaster.mask(collisionRaster, position)
    }

    override fun toString(): String =
        "Word{word='$word', color=$color, x=${position.x}, y=${position.y}, width=${bufferedImage.width}, height=${bufferedImage.height}}"

    companion object {
        @JvmStatic
        fun getRenderingHints(): RenderingHints {
            val hints = HashMap<RenderingHints.Key, Any>()
            hints[RenderingHints.KEY_ALPHA_INTERPOLATION] = RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
            hints[RenderingHints.KEY_ANTIALIASING] = RenderingHints.VALUE_ANTIALIAS_ON
            hints[RenderingHints.KEY_COLOR_RENDERING] = RenderingHints.VALUE_COLOR_RENDER_QUALITY
            hints[RenderingHints.KEY_FRACTIONALMETRICS] = RenderingHints.VALUE_FRACTIONALMETRICS_ON
            hints[RenderingHints.KEY_INTERPOLATION] = RenderingHints.VALUE_INTERPOLATION_BICUBIC
            hints[RenderingHints.KEY_TEXT_ANTIALIASING] = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB
            return RenderingHints(hints)
        }
    }
}
