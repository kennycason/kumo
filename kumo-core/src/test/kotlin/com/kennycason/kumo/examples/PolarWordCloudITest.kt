package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.PolarBlendMode
import com.kennycason.kumo.PolarWordCloud
import com.kennycason.kumo.bg.CircleBackground
import com.kennycason.kumo.bg.PixelBoundaryBackground
import com.kennycason.kumo.bg.RectangleBackground
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.font.scale.SqrtFontScalar
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.logging.Logging
import org.apache.commons.io.IOUtils
import org.junit.Test
import java.awt.Color
import java.awt.Dimension
import java.io.IOException
import java.io.InputStream

class PolarWordCloudITest : Logging {

    @Test
    fun whaleImgLargePolarTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(600)
        frequencyAnalyzer.setMinWordLength(4)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/new_york_positive.txt"))
        val wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/new_york_negative.txt"))

        val dimension = Dimension(990, 618)
        val wordCloud = PolarWordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 2
        wordCloud.backgroundColor = Color.WHITE
        wordCloud.background = PixelBoundaryBackground(getInputStream("backgrounds/whale.png"))
        wordCloud.fontScalar = LinearFontScalar(15, 50)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies, wordFrequencies2)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/polar_newyork_whale_large_blur.png")
    }

    @Test
    fun newyorkPolarCircle() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(750)
        frequencyAnalyzer.setMinWordLength(4)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/new_york_positive.txt"))
        val wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/new_york_negative.txt"))

        val dimension = Dimension(600, 600)
        val wordCloud = PolarWordCloud(dimension, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR)
        wordCloud.padding = 2
        wordCloud.background = CircleBackground(300)
        wordCloud.fontScalar = SqrtFontScalar(10, 40)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies, wordFrequencies2)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/polar_newyork_circle_blur_sqrt_font.png")
    }

    @Test
    fun newyorkPolarRectangle() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(750)
        frequencyAnalyzer.setMinWordLength(4)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/new_york_positive.txt"))
        val wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/new_york_negative.txt"))

        val dimension = Dimension(800, 600)
        val wordCloud = PolarWordCloud(dimension, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR)
        wordCloud.padding = 2
        wordCloud.background = RectangleBackground(dimension)
        wordCloud.fontScalar = SqrtFontScalar(10, 40)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies, wordFrequencies2)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/polar_newyork_rectangle_blur.png")
    }

    @Test
    fun tidyCatLitter() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(400)
        frequencyAnalyzer.setMinWordLength(4)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/tidy_cat_litter_positive.txt"))
        val wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/tidy_cat_litter_negative.txt"))

        val dimension = Dimension(600, 600)
        val wordCloud = PolarWordCloud(dimension, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR)
        wordCloud.padding = 2
        wordCloud.kumoFont = KumoFont("Cairo", FontWeight.BOLD)
        wordCloud.background = PixelBoundaryBackground(getInputStream("backgrounds/cat.bmp"))
        wordCloud.fontScalar = SqrtFontScalar(10, 40)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies, wordFrequencies2)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/tidy_cat_litter_cat_shape3.png")
    }

    private fun loadStopWords(): Set<String> {
        try {
            val lines = IOUtils.readLines(getInputStream("text/stop_words.txt"))
            return HashSet(lines)
        } catch (e: IOException) {
            logger.error(e) { e.message }
        }
        return emptySet()
    }

    private fun getInputStream(path: String): InputStream =
        Thread.currentThread().contextClassLoader.getResourceAsStream(path)
}
