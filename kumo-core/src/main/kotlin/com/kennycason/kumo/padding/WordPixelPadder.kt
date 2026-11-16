package com.kennycason.kumo.padding

import com.kennycason.kumo.Word
import com.kennycason.kumo.image.CollisionRaster
import kotlin.math.min

class WordPixelPadder : Padder {

    private val rectanglePadder = RectanglePadder()

    override fun pad(word: Word, padding: Int) {
        if (padding <= 0) return
        rectanglePadder.pad(word, padding)

        val collisionRaster = word.collisionRaster
        val originalRaster = CollisionRaster(collisionRaster)

        val width = originalRaster.dimension.width
        val height = originalRaster.dimension.height

        val pixelsSetInPaddingPerColumn = IntArray(width)

        for (x in 0 until width) {
            pixelsSetInPaddingPerColumn[x] = countNotTransparentPixels(
                originalRaster, 0, min(padding, height - 1), x
            )
        }

        for (y in 0 until height) {
            if (y - padding >= 0) {
                val line = y - padding
                var set = -1
                while (true) {
                    set = originalRaster.nextNotTransparentPixel(set + 1, width, line)
                    if (set == -1) break
                    pixelsSetInPaddingPerColumn[set]--
                }
            }

            if (y > 0 && y + padding < height) {
                val line = y + padding
                var set = -1
                while (true) {
                    set = originalRaster.nextNotTransparentPixel(set + 1, width, line)
                    if (set == -1) break
                    pixelsSetInPaddingPerColumn[set]++
                }
            }

            var pixelsSetInPaddingArea = 0

            for (x in 0 until min(padding, width - 1)) {
                pixelsSetInPaddingArea += pixelsSetInPaddingPerColumn[x]
            }

            for (x in 0 until width) {
                if (x - padding >= 0) {
                    pixelsSetInPaddingArea -= pixelsSetInPaddingPerColumn[x - padding]
                }
                if (x > 0 && x + padding < width) {
                    pixelsSetInPaddingArea += pixelsSetInPaddingPerColumn[x + padding]
                }

                if (pixelsSetInPaddingArea > 0) {
                    collisionRaster.setPixelIsNotTransparent(x, y)
                }
            }
        }
    }

    private fun countNotTransparentPixels(originalRaster: CollisionRaster, minY: Int, maxY: Int, x: Int): Int {
        var n = 0
        for (y in minY..maxY) {
            if (!originalRaster.isTransparent(x, y)) {
                n++
            }
        }
        return n
    }
}
