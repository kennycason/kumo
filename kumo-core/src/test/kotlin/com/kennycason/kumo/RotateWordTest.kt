package com.kennycason.kumo

import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import strikt.api.expectThat
import strikt.assertions.isTrue

/**
 * @author joerg1985
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RotateWordTest {

    fun data(): List<Arguments> = buildList {
        val text = "rotating text is great!!"
        for (size in 20 until 100 step 10) {
            val font = Font("Arial", Font.PLAIN, size)
            for (sample in listOf(text.lowercase(), text, text.uppercase())) {
                add(Arguments.of(font, sample))
            }
        }
    }

    @Disabled("Previously disabled visual cropping check")
    @ParameterizedTest
    @MethodSource("data")
    fun checkRotatedTextIsNotCropped(font: Font, text: String) {
        val image = BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR)
        val graphics = image.createGraphics()

        // set the rendering hint here to ensure the font metrics are correct
        graphics.setRenderingHints(Word.getRenderingHints())
        val fontMetrics = graphics.getFontMetrics(font)
        graphics.dispose()

        for (angle in 0 until 360 step 5) {
            val word = Word(
                text, Color.RED, fontMetrics, com.kennycason.kumo.collide.checkers.RectanglePixelCollisionChecker(), Math.toRadians(angle.toDouble())
            )

            val rendered = word.bufferedImage

            val width = rendered.width
            val height = rendered.height

            try {
                for (y in 0 until height) {
                    expectThat(rendered.getRGB(0, y))
                        .get("left edge is nearly transparent") { nearlyTransparent(this) }.isTrue()
                    expectThat(rendered.getRGB(width - 1, y))
                        .get("right edge is nearly transparent") { nearlyTransparent(this) }.isTrue()
                }

                for (x in 0 until width) {
                    expectThat(rendered.getRGB(x, 0))
                        .get("top edge is nearly transparent") { nearlyTransparent(this) }.isTrue()
                    expectThat(rendered.getRGB(x, height - 1))
                        .get("bottom edge is nearly transparent") { nearlyTransparent(this) }.isTrue()
                }
            } catch (e: AssertionError) {
                val file = File("output/FailedRotateWordTest_${System.currentTimeMillis()}.png")
                ImageIO.write(rendered, "png", file)

                throw e
            }
        }
    }
}

private fun nearlyTransparent(argb: Int): Boolean {
    return (argb and 0xFF000000.toInt()) < 0x40000000
}
