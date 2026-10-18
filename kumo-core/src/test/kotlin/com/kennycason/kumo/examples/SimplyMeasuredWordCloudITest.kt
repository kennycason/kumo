package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.LayeredWordCloud
import com.kennycason.kumo.bg.PixelBoundaryBackground
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.SqrtFontScalar
import com.kennycason.kumo.logging.Logging
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.palette.ColorPalette
import java.awt.Color
import java.awt.Dimension
import java.io.InputStream
import org.junit.jupiter.api.Test

class SimplyMeasuredWordCloudITest : Logging {

    @Test
    fun simplymeasured() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 1000
        frequencyAnalyzer.minWordLength = 3
        frequencyAnalyzer.stopWords = loadStopWords()

        val wordFrequencies = getInputStream("text/simplymeasured.txt").use { frequencyAnalyzer.load(it) }
        val wordFrequencies2 = getInputStream("text/simplymeasured.txt").use { frequencyAnalyzer.load(it) }
        val wordFrequencies3 = getInputStream("text/simplymeasured.txt").use { frequencyAnalyzer.load(it) }

        val dimension = Dimension(600, 454)
        val layeredWordCloud = LayeredWordCloud(4, dimension, CollisionMode.PIXEL_PERFECT)
        layeredWordCloud.backgroundColor = Color(0x000000FF, true)

        layeredWordCloud[0].padding = 1
        layeredWordCloud[1].padding = 1
        layeredWordCloud[2].padding = 1

        layeredWordCloud[0].kumoFont = KumoFont("Comic Sans MS", FontWeight.PLAIN)
        layeredWordCloud[1].kumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)
        layeredWordCloud[2].kumoFont = KumoFont("Comic Sans MS", FontWeight.ITALIC)

        layeredWordCloud[0].background = getInputStream("backgrounds/sm-logo-1.png").use { PixelBoundaryBackground(it) }
        layeredWordCloud[1].background = getInputStream("backgrounds/sm-logo-2.png").use { PixelBoundaryBackground(it) }
        layeredWordCloud[2].background = getInputStream("backgrounds/sm-logo-3.png").use { PixelBoundaryBackground(it) }

        layeredWordCloud[0].colorPalette = ColorPalette(Color(0x3f5ca9))
        layeredWordCloud[1].colorPalette = ColorPalette(Color(0x6185c3))
        layeredWordCloud[2].colorPalette = ColorPalette(Color(0x8fb6e1))

        layeredWordCloud[0].fontScalar = SqrtFontScalar(10, 40)
        layeredWordCloud[1].fontScalar = SqrtFontScalar(10, 40)
        layeredWordCloud[2].fontScalar = SqrtFontScalar(10, 40)

        val startTime = System.currentTimeMillis()
        layeredWordCloud.build(0, wordFrequencies)
        layeredWordCloud.build(1, wordFrequencies2)
        layeredWordCloud.build(2, wordFrequencies3)

        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        layeredWordCloud.writeToFile("output/simplymeasured.png")
    }

    companion object {
        private fun loadStopWords(): Set<String> {
            return getInputStream("text/stop_words.txt").bufferedReader().use { it.readLines() }.toSet()
        }

        private fun getInputStream(path: String): InputStream {
            return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
        }
    }
}
