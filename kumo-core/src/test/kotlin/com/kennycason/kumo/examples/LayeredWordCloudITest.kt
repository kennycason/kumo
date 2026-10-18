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
import java.io.IOException
import java.io.InputStream
import org.junit.jupiter.api.Test

class LayeredWordCloudITest : Logging {

    @Test
    fun layeredExample() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 300
        frequencyAnalyzer.minWordLength = 5
        frequencyAnalyzer.stopWords = loadStopWords()

        val wordFrequencies = getInputStream("text/new_york_positive.txt").use { frequencyAnalyzer.analyze(it) }
        val wordFrequencies2 = getInputStream("text/new_york_negative.txt").use { frequencyAnalyzer.analyze(it) }
        val dimension = Dimension(600, 386)
        val layeredWordCloud = LayeredWordCloud(2, dimension, CollisionMode.PIXEL_PERFECT)

        layeredWordCloud[0].padding = 1
        layeredWordCloud[1].padding = 1

        layeredWordCloud[0].kumoFont = KumoFont("LICENSE PLATE", FontWeight.BOLD)
        layeredWordCloud[1].kumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)

        layeredWordCloud[0].background = getInputStream("backgrounds/cloud_bg.bmp").use { PixelBoundaryBackground(it) }
        layeredWordCloud[1].background = getInputStream("backgrounds/cloud_fg.bmp").use { PixelBoundaryBackground(it) }

        layeredWordCloud[0].colorPalette = ColorPalette(Color(0xABEDFF), Color(0x82E4FF), Color(0x55D6FA))
        layeredWordCloud[1].colorPalette = ColorPalette(Color(0xFFFFFF), Color(0xDCDDDE), Color(0xCCCCCC))

        layeredWordCloud[0].fontScalar = SqrtFontScalar(10, 40)
        layeredWordCloud[1].fontScalar = SqrtFontScalar(10, 40)

        val startTime = System.currentTimeMillis()
        layeredWordCloud.build(0, wordFrequencies)
        layeredWordCloud.build(1, wordFrequencies2)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        layeredWordCloud.writeToFile("output/layered_word_cloud.png")
    }

    @Test
    fun layeredHaskellExample() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 300
        frequencyAnalyzer.minWordLength = 5
        frequencyAnalyzer.stopWords = loadStopWords()

        val wordFrequencies = getInputStream("text/haskell_hate.txt").use { frequencyAnalyzer.analyze(it) }
        val wordFrequencies2 = getInputStream("text/haskell_love.txt").use { frequencyAnalyzer.analyze(it) }
        val dimension = Dimension(600, 424)
        val layeredWordCloud = LayeredWordCloud(2, dimension, CollisionMode.PIXEL_PERFECT)
        layeredWordCloud.backgroundColor = Color.WHITE

        layeredWordCloud[0].padding = 1
        layeredWordCloud[1].padding = 1

        layeredWordCloud[0].kumoFont = KumoFont("LICENSE PLATE", FontWeight.BOLD)
        layeredWordCloud[1].kumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)

        layeredWordCloud[0].background = getInputStream("backgrounds/haskell_1.bmp").use { PixelBoundaryBackground(it) }
        layeredWordCloud[1].background = getInputStream("backgrounds/haskell_2.bmp").use { PixelBoundaryBackground(it) }

        layeredWordCloud[0].colorPalette = ColorPalette(Color(0xFA6C07), Color(0xFF7614), Color(0xFF8936))
        layeredWordCloud[1].colorPalette = ColorPalette(Color(0x080706), Color(0x3B3029), Color(0x47362A))

        layeredWordCloud[0].fontScalar = SqrtFontScalar(10, 40)
        layeredWordCloud[1].fontScalar = SqrtFontScalar(10, 40)

        val startTime = System.currentTimeMillis()
        layeredWordCloud.build(0, wordFrequencies)
        layeredWordCloud.build(1, wordFrequencies2)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        layeredWordCloud.writeToFile("output/layered_haskell.png")
    }

    @Test
    fun layeredPhoBowl() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 1000
        frequencyAnalyzer.minWordLength = 3
        frequencyAnalyzer.stopWords = loadStopWords()

        val wordFrequencies = getInputStream("text/pho_history.txt").use { frequencyAnalyzer.analyze(it) }
        val wordFrequencies2 = getInputStream("text/pho_history_viet.txt").use { frequencyAnalyzer.analyze(it) }
        val wordFrequencies3 = getInputStream("text/pho_recipee.txt").use { frequencyAnalyzer.analyze(it) }
        val wordFrequencies4 = getInputStream("text/pho_chopsticks.txt").use { frequencyAnalyzer.analyze(it) }

        val dimension = Dimension(1000, 976)
        val layeredWordCloud = LayeredWordCloud(4, dimension, CollisionMode.PIXEL_PERFECT)
        layeredWordCloud.backgroundColor = Color(0x333333)

        layeredWordCloud[0].padding = 1
        layeredWordCloud[1].padding = 1
        layeredWordCloud[2].padding = 1
        layeredWordCloud[3].padding = 1

        layeredWordCloud[0].kumoFont = KumoFont("Comic Sans MS", FontWeight.PLAIN)
        layeredWordCloud[1].kumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)
        layeredWordCloud[2].kumoFont = KumoFont("Comic Sans MS", FontWeight.ITALIC)
        layeredWordCloud[3].kumoFont = KumoFont("Comic Sans MS", FontWeight.BOLD)

        layeredWordCloud[0].background = getInputStream("backgrounds/pho_1.bmp").use { PixelBoundaryBackground(it) }
        layeredWordCloud[1].background = getInputStream("backgrounds/pho_2.bmp").use { PixelBoundaryBackground(it) }
        layeredWordCloud[2].background = getInputStream("backgrounds/pho_3.bmp").use { PixelBoundaryBackground(it) }
        layeredWordCloud[3].background = getInputStream("backgrounds/pho_4.bmp").use { PixelBoundaryBackground(it) }

        layeredWordCloud[0].colorPalette = ColorPalette(Color(0x26A621), Color(0x21961D), Color(0x187A15))
        layeredWordCloud[1].colorPalette = ColorPalette(Color(0x5963F0), Color(0x515CF0), Color(0x729FED))
        layeredWordCloud[2].colorPalette = ColorPalette(Color(0xEDC672), Color(0xDBB258), Color(0xDE7C1B))
        layeredWordCloud[3].colorPalette = ColorPalette(Color(0x70572B), Color(0x857150), Color(0xB09971))

        layeredWordCloud[0].fontScalar = SqrtFontScalar(8, 30)
        layeredWordCloud[1].fontScalar = SqrtFontScalar(8, 40)
        layeredWordCloud[2].fontScalar = SqrtFontScalar(8, 30)
        layeredWordCloud[3].fontScalar = SqrtFontScalar(8, 30)

        val startTime = System.currentTimeMillis()
        layeredWordCloud.build(0, wordFrequencies)
        layeredWordCloud.build(1, wordFrequencies2)
        layeredWordCloud.build(2, wordFrequencies3)
        layeredWordCloud.build(3, wordFrequencies4)

        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        layeredWordCloud.writeToFile("output/layered_pho_bowl.png")
    }

    private fun loadStopWords(): Set<String> {
        try {
            val lines = getInputStream("text/stop_words.txt").bufferedReader().use { it.readLines() }
            return lines.toSet()
        } catch (e: IOException) {
            logger.error(e) { e.message }
        }
        return emptySet()
    }

    private fun getInputStream(path: String): InputStream =
        Thread.currentThread().contextClassLoader.getResourceAsStream(path)
}
