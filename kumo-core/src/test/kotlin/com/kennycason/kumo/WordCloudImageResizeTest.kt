package com.kennycason.kumo

import com.kennycason.kumo.bg.PixelBoundaryBackground
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.SqrtFontScalar
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.palette.ColorPalette
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.io.IOUtils
import org.junit.Test
import java.awt.Color
import java.awt.Dimension
import java.io.IOException
import java.io.InputStream

private val logger = KotlinLogging.logger {}

class WordCloudImageResizeTest {

    companion object {
        private const val INPUT_PATH = "backgrounds/whale.png"
        private const val DEFAULT_FONT = "Impact"
        private const val DEFAULT_IMAGE_TYPE = "png"

        private fun loadStopWords(): Set<String> {
            try {
                val lines = IOUtils.readLines(getInputStream("text/stop_words.txt"))
                return HashSet(lines)
            } catch (e: IOException) {
                logger.error(e) { e.message }
            }
            return emptySet()
        }

        private fun getInputStream(path: String): InputStream {
            return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
        }
    }

    @Test
    fun imageProcessorInstanceTest() {
        assert(ImageProcessor.getInstance() != null)
    }

    @Test
    fun whaleImgLargeToSmallTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(600)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())
        val width = 150
        val height = 100

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"))
        val dimension = Dimension(width, height)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 1
        wordCloud.backgroundColor = Color.WHITE
        val inputStream = ImageProcessor.readImage(INPUT_PATH, width, height, DEFAULT_IMAGE_TYPE)
        try {
            wordCloud.background = PixelBoundaryBackground(inputStream)
        } finally {
            inputStream.close()
        }

        wordCloud.kumoFont = KumoFont(DEFAULT_FONT, FontWeight.PLAIN)
        wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0x000000))
        wordCloud.fontScalar = SqrtFontScalar(10, 50)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output_test/a_whale_large_to_small.png")
        inputStream.close()
    }

    @Test
    fun whaleImgSmallToLargeTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(600)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())
        val width = 1500
        val height = 1000

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"), false)
        val dimension = Dimension(width, height)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 1
        wordCloud.backgroundColor = Color.WHITE
        val inputStream = ImageProcessor.readImage(INPUT_PATH, width, height, DEFAULT_IMAGE_TYPE)
        try {
            wordCloud.background = PixelBoundaryBackground(inputStream)
        } finally {
            inputStream.close()
        }

        wordCloud.kumoFont = KumoFont(DEFAULT_FONT, FontWeight.PLAIN)
        wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0x000000))
        wordCloud.fontScalar = SqrtFontScalar(10, 50)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output_test/a_whale_small_to_large.png")
    }
}
