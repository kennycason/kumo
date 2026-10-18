package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.LayeredWordCloud
import com.kennycason.kumo.bg.PixelBoundaryBackground
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.SqrtFontScalar
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.palette.ColorPalette
import com.kennycason.kumo.logging.Logging
import org.apache.commons.io.IOUtils
import org.junit.Test
import java.awt.Color
import java.awt.Dimension
import java.io.InputStream

class SimplyMeasuredWordCloudITest : Logging {

    @Test
    fun simplymeasured() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(1000)
        frequencyAnalyzer.setMinWordLength(3)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/simplymeasured.txt"))
        val wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/simplymeasured.txt"))
        val wordFrequencies3 = frequencyAnalyzer.load(getInputStream("text/simplymeasured.txt"))

        val dimension = Dimension(600, 454)
        val layeredWordCloud = LayeredWordCloud(4, dimension, CollisionMode.PIXEL_PERFECT)
        layeredWordCloud.setBackgroundColor(Color(0x000000FF, true))

        layeredWordCloud.setPadding(0, 1)
        layeredWordCloud.setPadding(1, 1)
        layeredWordCloud.setPadding(2, 1)

        layeredWordCloud.setKumoFont(0, KumoFont("Comic Sans MS", FontWeight.PLAIN))
        layeredWordCloud.setKumoFont(1, KumoFont("Comic Sans MS", FontWeight.BOLD))
        layeredWordCloud.setKumoFont(2, KumoFont("Comic Sans MS", FontWeight.ITALIC))

        layeredWordCloud.setBackground(0, PixelBoundaryBackground(getInputStream("backgrounds/sm-logo-1.png")))
        layeredWordCloud.setBackground(1, PixelBoundaryBackground(getInputStream("backgrounds/sm-logo-2.png")))
        layeredWordCloud.setBackground(2, PixelBoundaryBackground(getInputStream("backgrounds/sm-logo-3.png")))

        layeredWordCloud.setColorPalette(0, ColorPalette(Color(0x3f5ca9)))
        layeredWordCloud.setColorPalette(1, ColorPalette(Color(0x6185c3)))
        layeredWordCloud.setColorPalette(2, ColorPalette(Color(0x8fb6e1)))

        layeredWordCloud.setFontScalar(0, SqrtFontScalar(10, 40))
        layeredWordCloud.setFontScalar(1, SqrtFontScalar(10, 40))
        layeredWordCloud.setFontScalar(2, SqrtFontScalar(10, 40))

        val startTime = System.currentTimeMillis()
        layeredWordCloud.build(0, wordFrequencies)
        layeredWordCloud.build(1, wordFrequencies2)
        layeredWordCloud.build(2, wordFrequencies3)

        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        layeredWordCloud.writeToFile("output/simplymeasured.png")
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
