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

class DataRankWordCloudITest : Logging {

    @Test
    fun datarank() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 200
        frequencyAnalyzer.minWordLength = 2
        frequencyAnalyzer.stopWords = loadStopWords()

        val wordFrequencies = getInputStream("text/datarank.txt").use { frequencyAnalyzer.load(it) }

        val dimension = Dimension(990, 618)
        val layeredWordCloud = LayeredWordCloud(4, dimension, CollisionMode.PIXEL_PERFECT)
        layeredWordCloud.backgroundColor = Color(0x000000FF, true)

        layeredWordCloud[0].padding = 1
        layeredWordCloud[1].padding = 1

        layeredWordCloud[0].kumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)
        layeredWordCloud[1].kumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)

        layeredWordCloud[0].background = getInputStream("backgrounds/datarank-1.png").use { PixelBoundaryBackground(it) }
        layeredWordCloud[1].background = getInputStream("backgrounds/datarank-2.png").use { PixelBoundaryBackground(it) }

        layeredWordCloud[0].colorPalette = ColorPalette(Color(0x0891d1))
        layeredWordCloud[1].colorPalette = ColorPalette(Color(0x76beea))

        layeredWordCloud[0].fontScalar = SqrtFontScalar(10, 60)
        layeredWordCloud[1].fontScalar = SqrtFontScalar(10, 60)

        val startTime = System.currentTimeMillis()
        layeredWordCloud.build(0, wordFrequencies)
        layeredWordCloud.build(1, wordFrequencies)

        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        layeredWordCloud.writeToFile("output/datarank_test.png")
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
