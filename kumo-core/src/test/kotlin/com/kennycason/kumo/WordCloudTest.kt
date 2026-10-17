package com.kennycason.kumo

import com.kennycason.kumo.bg.RectangleBackground
import org.junit.Test
import java.awt.Dimension
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import org.junit.Assert.*

class WordCloudTest {

    companion object {
        @JvmField
        val WORD_FREQUENCIES = listOf(
            WordFrequency("apple", 22),
            WordFrequency("baby", 3),
            WordFrequency("back", 15),
            WordFrequency("bear", 9),
            WordFrequency("boy", 26)
        )
    }

    @Test
    fun testWriteToStreamAsPNG() {
        val dimension = Dimension(200, 200)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.build(WORD_FREQUENCIES)
        wordCloud.padding = 2
        wordCloud.background = RectangleBackground(dimension)

        val byteArrayOutputStream = ByteArrayOutputStream()
        wordCloud.writeToStreamAsPNG(byteArrayOutputStream)

        val bytes = byteArrayOutputStream.toByteArray()
        assertNotEquals(0, bytes.size)

        val byteArrayInputStream = ByteArrayInputStream(bytes)
        val imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream)
        val imageReaders = ImageIO.getImageReaders(imageInputStream)
        assertTrue(imageReaders.hasNext())
        val imageReader = imageReaders.next()
        assertEquals("png", imageReader.formatName)
    }
}
