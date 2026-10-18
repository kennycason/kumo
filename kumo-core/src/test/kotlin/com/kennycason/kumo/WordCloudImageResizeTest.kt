package com.kennycason.kumo

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

class WordCloudImageResizeTest : Logging {

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

    companion object {
        private const val INPUT_PATH = "backgrounds/whale.png"
        private const val DEFAULT_FONT = "Impact"
        private const val DEFAULT_IMAGE_TYPE = "png"
    }

    @Test
    fun whaleImgLargeToSmallTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 600
        frequencyAnalyzer.minWordLength = 5
        frequencyAnalyzer.stopWords = loadStopWords()
        val width = 150
        val height = 100

        val wordFrequencies = getInputStream("text/datarank.txt").use { frequencyAnalyzer.load(it) }
        val dimension = Dimension(width, height)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 1
        wordCloud.backgroundColor = Color.WHITE
        wordCloud.background = ImageProcessor.readImage(INPUT_PATH, width, height, DEFAULT_IMAGE_TYPE).use { PixelBoundaryBackground(it) }

        wordCloud.kumoFont = KumoFont(DEFAULT_FONT, FontWeight.PLAIN)
        wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0x000000))
        wordCloud.fontScalar = SqrtFontScalar(10, 50)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output_test/a_whale_large_to_small.png")
    }

    @Test
    fun whaleImgSmallToLargeTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 600
        frequencyAnalyzer.minWordLength = 5
        frequencyAnalyzer.stopWords = loadStopWords()
        val width = 1500
        val height = 1000

        val wordFrequencies = getInputStream("text/datarank.txt").use { frequencyAnalyzer.load(it, false) }
        val dimension = Dimension(width, height)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 1
        wordCloud.backgroundColor = Color.WHITE
        wordCloud.background = ImageProcessor.readImage(INPUT_PATH, width, height, DEFAULT_IMAGE_TYPE).use { PixelBoundaryBackground(it) }

        wordCloud.kumoFont = KumoFont(DEFAULT_FONT, FontWeight.PLAIN)
        wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0x000000))
        wordCloud.fontScalar = SqrtFontScalar(10, 50)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output_test/a_whale_small_to_large.png")
    }
}
