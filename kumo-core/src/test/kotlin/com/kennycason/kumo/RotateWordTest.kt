package com.kennycason.kumo

import org.junit.Assert
import org.junit.Ignore
import org.junit.runners.Parameterized
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * @author joerg1985
 */
//@RunWith(Parameterized::class)
class RotateWordTest(private val _font: Font, private val _text: String) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Iterable<Array<Any>> {
            val params = mutableListOf<Array<Any>>()
            val text = "rotating text is great!!"

            for (i in 20 until 100 step 10) {
                params.add(arrayOf(Font("Arial", 0, i), text.lowercase()))
                params.add(arrayOf(Font("Arial", 0, i), text))
                params.add(arrayOf(Font("Arial", 0, i), text.uppercase()))
            }

            return params
        }
    }

    @Ignore
    fun checkRotatedTextIsNotCropped() {
        val image = BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR)
        val graphics = image.createGraphics()

        // set the rendering hint here to ensure the font metrics are correct
        graphics.setRenderingHints(Word.getRenderingHints())
        val fontMetrics = graphics.getFontMetrics(_font)

        for (angle in 0 until 360 step 5) {
            val word = Word(
                _text, Color.red, fontMetrics, com.kennycason.kumo.collide.checkers.RectanglePixelCollisionChecker(), Math.toRadians(angle.toDouble())
            )

            val rendered = word.bufferedImage

            val width = rendered.width
            val height = rendered.height

            try {
                for (y in 0 until height) {
                    Assert.assertTrue(
                        "text doesn't touch the outer left line",
                        nearlyTransparent(rendered.getRGB(0, y))
                    )
                    Assert.assertTrue(
                        "text doesn't touch the outer right line",
                        nearlyTransparent(rendered.getRGB(width - 1, y))
                    )
                }

                for (x in 0 until width) {
                    Assert.assertTrue(
                        "text doesn't touch the top line",
                        nearlyTransparent(rendered.getRGB(x, 0))
                    )
                    Assert.assertTrue(
                        "text doesn't touch the bottom line",
                        nearlyTransparent(rendered.getRGB(x, height - 1))
                    )
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
