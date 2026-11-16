package com.kennycason.kumo

import com.kennycason.kumo.bg.RectangleBackground
import java.awt.Dimension
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo
import strikt.assertions.isTrue

class WordCloudTest {

    private val wordFrequencies = listOf(
        WordFrequency("apple", 22),
        WordFrequency("baby", 3),
        WordFrequency("back", 15),
        WordFrequency("bear", 9),
        WordFrequency("boy", 26)
    )

    @Test
    fun testWriteToStreamAsPNG() {
        val dimension = Dimension(200, 200)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.build(wordFrequencies)
        wordCloud.padding = 2
        wordCloud.background = RectangleBackground(dimension)

        val byteArrayOutputStream = ByteArrayOutputStream()
        wordCloud.writeToStreamAsPNG(byteArrayOutputStream)

        val bytes = byteArrayOutputStream.toByteArray()
        expectThat(bytes.size).isNotEqualTo(0)

        val byteArrayInputStream = ByteArrayInputStream(bytes)
        val imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream)
        val imageReaders = ImageIO.getImageReaders(imageInputStream)
        expectThat(imageReaders.hasNext()).isTrue()
        val imageReader = imageReaders.next()
        expectThat(imageReader.formatName).isEqualTo("png")
    }
}
