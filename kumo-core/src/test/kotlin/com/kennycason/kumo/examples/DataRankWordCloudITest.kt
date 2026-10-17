package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.LayeredWordCloud
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
import java.io.InputStream

private val logger = KotlinLogging.logger {}

class DataRankWordCloudITest {

    @Test
    fun datarank() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(200)
        frequencyAnalyzer.setMinWordLength(2)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"))

        val dimension = Dimension(990, 618)
        val layeredWordCloud = LayeredWordCloud(4, dimension, CollisionMode.PIXEL_PERFECT)
        layeredWordCloud.setBackgroundColor(Color(0x000000FF, true))

        layeredWordCloud.setPadding(0, 1)
        layeredWordCloud.setPadding(1, 1)

        layeredWordCloud.setKumoFont(0, KumoFont("Comic Sans MS", FontWeight.BOLD))
        layeredWordCloud.setKumoFont(1, KumoFont("Comic Sans MS", FontWeight.BOLD))

        layeredWordCloud.setBackground(0, PixelBoundaryBackground(getInputStream("backgrounds/datarank-1.png")))
        layeredWordCloud.setBackground(1, PixelBoundaryBackground(getInputStream("backgrounds/datarank-2.png")))

        layeredWordCloud.setColorPalette(0, ColorPalette(Color(0x0891d1)))
        layeredWordCloud.setColorPalette(1, ColorPalette(Color(0x76beea)))

        layeredWordCloud.setFontScalar(0, SqrtFontScalar(10, 60))
        layeredWordCloud.setFontScalar(1, SqrtFontScalar(10, 60))

        val startTime = System.currentTimeMillis()
        layeredWordCloud.build(0, wordFrequencies)
        layeredWordCloud.build(1, wordFrequencies)

        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        layeredWordCloud.writeToFile("output/datarank_test.png")
    }

    companion object {
        private fun loadStopWords(): Set<String> {
            return HashSet(IOUtils.readLines(getInputStream("text/stop_words.txt")))
        }

        private fun getInputStream(path: String): InputStream {
            return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
        }
    }
}
