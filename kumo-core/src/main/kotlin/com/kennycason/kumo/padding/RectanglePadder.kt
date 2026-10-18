package com.kennycason.kumo.padding

import com.kennycason.kumo.Word
import java.awt.image.BufferedImage

class RectanglePadder : Padder {

    override fun pad(word: Word, padding: Int) {
        if (padding <= 0) return

        val bufferedImage = word.bufferedImage
        val width = bufferedImage.width + padding * 2
        val height = bufferedImage.height + padding * 2

        val newBufferedImage = BufferedImage(width, height, bufferedImage.type)
        val graphics = newBufferedImage.graphics
        try {
            graphics.drawImage(bufferedImage, padding, padding, null)
        } finally {
            graphics.dispose()
        }

        word.bufferedImage = newBufferedImage
    }
}
