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
import java.io.IOException
import java.io.InputStream

private val logger = KotlinLogging.logger {}

class LayeredWordCloudITest {

    @Test
    fun layeredExample() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(300)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/new_york_positive.txt"))
        val wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/new_york_negative.txt"))
        val dimension = Dimension(600, 386)
        val layeredWordCloud = LayeredWordCloud(2, dimension, CollisionMode.PIXEL_PERFECT)

        layeredWordCloud.setPadding(0, 1)
        layeredWordCloud.setPadding(1, 1)

        layeredWordCloud.setKumoFont(0, KumoFont("LICENSE PLATE", FontWeight.BOLD))
        layeredWordCloud.setKumoFont(1, KumoFont("Comic Sans MS", FontWeight.BOLD))

        layeredWordCloud.setBackground(0, PixelBoundaryBackground(getInputStream("backgrounds/cloud_bg.bmp")))
        layeredWordCloud.setBackground(1, PixelBoundaryBackground(getInputStream("backgrounds/cloud_fg.bmp")))

        layeredWordCloud.setColorPalette(0, ColorPalette(Color(0xABEDFF), Color(0x82E4FF), Color(0x55D6FA)))
        layeredWordCloud.setColorPalette(1, ColorPalette(Color(0xFFFFFF), Color(0xDCDDDE), Color(0xCCCCCC)))

        layeredWordCloud.setFontScalar(0, SqrtFontScalar(10, 40))
        layeredWordCloud.setFontScalar(1, SqrtFontScalar(10, 40))

        val startTime = System.currentTimeMillis()
        layeredWordCloud.build(0, wordFrequencies)
        layeredWordCloud.build(1, wordFrequencies2)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        layeredWordCloud.writeToFile("output/layered_word_cloud.png")
    }

    @Test
    fun layeredHaskellExample() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(300)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/haskell_hate.txt"))
        val wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/haskell_love.txt"))
        val dimension = Dimension(600, 424)
        val layeredWordCloud = LayeredWordCloud(2, dimension, CollisionMode.PIXEL_PERFECT)
        layeredWordCloud.setBackgroundColor(Color.WHITE)

        layeredWordCloud.setPadding(0, 1)
        layeredWordCloud.setPadding(1, 1)

        layeredWordCloud.setKumoFont(0, KumoFont("LICENSE PLATE", FontWeight.BOLD))
        layeredWordCloud.setKumoFont(1, KumoFont("Comic Sans MS", FontWeight.BOLD))

        layeredWordCloud.setBackground(0, PixelBoundaryBackground(getInputStream("backgrounds/haskell_1.bmp")))
        layeredWordCloud.setBackground(1, PixelBoundaryBackground(getInputStream("backgrounds/haskell_2.bmp")))

        layeredWordCloud.setColorPalette(0, ColorPalette(Color(0xFA6C07), Color(0xFF7614), Color(0xFF8936)))
        layeredWordCloud.setColorPalette(1, ColorPalette(Color(0x080706), Color(0x3B3029), Color(0x47362A)))

        layeredWordCloud.setFontScalar(0, SqrtFontScalar(10, 40))
        layeredWordCloud.setFontScalar(1, SqrtFontScalar(10, 40))

        val startTime = System.currentTimeMillis()
        layeredWordCloud.build(0, wordFrequencies)
        layeredWordCloud.build(1, wordFrequencies2)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        layeredWordCloud.writeToFile("output/layered_haskell.png")
    }

    @Test
    fun layeredPhoBowl() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(1000)
        frequencyAnalyzer.setMinWordLength(3)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/pho_history.txt"))
        val wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/pho_history_viet.txt"))
        val wordFrequencies3 = frequencyAnalyzer.load(getInputStream("text/pho_recipee.txt"))
        val wordFrequencies4 = frequencyAnalyzer.load(getInputStream("text/pho_chopsticks.txt"))

        val dimension = Dimension(1000, 976)
        val layeredWordCloud = LayeredWordCloud(4, dimension, CollisionMode.PIXEL_PERFECT)
        layeredWordCloud.setBackgroundColor(Color(0x333333))

        layeredWordCloud.setPadding(0, 1)
        layeredWordCloud.setPadding(1, 1)
        layeredWordCloud.setPadding(2, 1)
        layeredWordCloud.setPadding(3, 1)

        layeredWordCloud.setKumoFont(0, KumoFont("Comic Sans MS", FontWeight.PLAIN))
        layeredWordCloud.setKumoFont(1, KumoFont("Comic Sans MS", FontWeight.BOLD))
        layeredWordCloud.setKumoFont(2, KumoFont("Comic Sans MS", FontWeight.ITALIC))
        layeredWordCloud.setKumoFont(3, KumoFont("Comic Sans MS", FontWeight.BOLD))

        layeredWordCloud.setBackground(0, PixelBoundaryBackground(getInputStream("backgrounds/pho_1.bmp")))
        layeredWordCloud.setBackground(1, PixelBoundaryBackground(getInputStream("backgrounds/pho_2.bmp")))
        layeredWordCloud.setBackground(2, PixelBoundaryBackground(getInputStream("backgrounds/pho_3.bmp")))
        layeredWordCloud.setBackground(3, PixelBoundaryBackground(getInputStream("backgrounds/pho_4.bmp")))

        layeredWordCloud.setColorPalette(0, ColorPalette(Color(0x26A621), Color(0x21961D), Color(0x187A15)))
        layeredWordCloud.setColorPalette(1, ColorPalette(Color(0x5963F0), Color(0x515CF0), Color(0x729FED)))
        layeredWordCloud.setColorPalette(2, ColorPalette(Color(0xEDC672), Color(0xDBB258), Color(0xDE7C1B)))
        layeredWordCloud.setColorPalette(3, ColorPalette(Color(0x70572B), Color(0x857150), Color(0xB09971)))

        layeredWordCloud.setFontScalar(0, SqrtFontScalar(8, 30))
        layeredWordCloud.setFontScalar(1, SqrtFontScalar(8, 40))
        layeredWordCloud.setFontScalar(2, SqrtFontScalar(8, 30))
        layeredWordCloud.setFontScalar(3, SqrtFontScalar(8, 30))

        val startTime = System.currentTimeMillis()
        layeredWordCloud.build(0, wordFrequencies)
        layeredWordCloud.build(1, wordFrequencies2)
        layeredWordCloud.build(2, wordFrequencies3)
        layeredWordCloud.build(3, wordFrequencies4)

        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        layeredWordCloud.writeToFile("output/layered_pho_bowl.png")
    }

    companion object {
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
}
