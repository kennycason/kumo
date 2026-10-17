package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.bg.CircleBackground
import com.kennycason.kumo.bg.PixelBoundaryBackground
import com.kennycason.kumo.bg.RectangleBackground
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.font.scale.SqrtFontScalar
import com.kennycason.kumo.image.AngleGenerator
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.palette.ColorPalette
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.io.IOUtils
import org.junit.Ignore
import org.junit.Test
import java.awt.Color
import java.awt.Dimension
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.Random

private val logger = KotlinLogging.logger {}

class WordCloudITest {

    companion object {
        private val RANDOM = Random()

        private fun buildRandomColorPalette(n: Int): ColorPalette {
            val colors = Array(n) {
                Color(RANDOM.nextInt(230) + 25, RANDOM.nextInt(230) + 25, RANDOM.nextInt(230) + 25)
            }
            return ColorPalette(*colors)
        }

        private fun buildWordFrequencies(): List<WordFrequency> {
            val pokemonNames = getPokemonNames()
            val wordFrequencies = mutableListOf<WordFrequency>()
            for (pokemon in pokemonNames) {
                wordFrequencies.add(WordFrequency(pokemon, RANDOM.nextInt(100) + 1))
            }
            return wordFrequencies
        }

        private fun getPokemonNames(): List<String> {
            return IOUtils.readLines(getInputStream("text/pokemon.txt"))
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

        private fun getInputStream(path: String): InputStream {
            return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
        }
    }

    @Test
    fun simpleCircleTest() {
        val wordFrequencies = buildWordFrequencies().subList(0, 150)
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.RECTANGLE)
        wordCloud.padding = 0
        wordCloud.background = CircleBackground(300)
        wordCloud.colorPalette = buildRandomColorPalette(20)
        wordCloud.fontScalar = LinearFontScalar(10, 40)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output/wordcloud_circle.png")
    }

    @Test
    fun simpleRectangleTest() {
        val wordFrequencies = buildWordFrequencies().subList(0, 150)
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.RECTANGLE)
        wordCloud.padding = 0
        wordCloud.background = RectangleBackground(dimension)
        wordCloud.colorPalette = buildRandomColorPalette(20)
        wordCloud.fontScalar = LinearFontScalar(10, 40)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output/wordcloud_rectangle2.png")
    }

    @Test
    fun loadWikipediaFromUrl() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(1000)
        frequencyAnalyzer.setMinWordLength(1)

        val wordFrequencies = frequencyAnalyzer.load(URL("http://en.wikipedia.org/wiki/Main_Page"))
        val dimension = Dimension(1000, 1000)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 1
        wordCloud.background = RectangleBackground(dimension)
        wordCloud.kumoFont = KumoFont("Impact", FontWeight.PLAIN)
        wordCloud.colorPalette = buildRandomColorPalette(5)
        wordCloud.fontScalar = LinearFontScalar(18, 70)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output/wikipedia.png")
    }

    @Test
    fun readCNN() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(500)
        frequencyAnalyzer.setMinWordLength(3)

        val wordFrequencies = frequencyAnalyzer.load(URL("http://www.cnn.com/"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 1
        wordCloud.background = RectangleBackground(dimension)
        wordCloud.kumoFont = KumoFont("Impact", FontWeight.PLAIN)
        wordCloud.colorPalette = buildRandomColorPalette(5)
        wordCloud.fontScalar = LinearFontScalar(18, 70)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output/cnn.png")
    }

    @Test
    fun whaleImgLargeTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(600)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"))
        val dimension = Dimension(990, 618)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 1
        wordCloud.backgroundColor = Color.WHITE
        wordCloud.background = PixelBoundaryBackground(getInputStream("backgrounds/whale.png"))
        wordCloud.kumoFont = KumoFont("Impact", FontWeight.PLAIN)
        wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0x000000))
        wordCloud.fontScalar = SqrtFontScalar(10, 50)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output/whale_wordcloud_large_impact.png")
    }

    @Test
    fun whaleImgSmallTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(300)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"))
        val dimension = Dimension(500, 312)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 1
        wordCloud.background = PixelBoundaryBackground(getInputStream("backgrounds/whale_small.png"))
        wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0xFFFFFF))
        wordCloud.fontScalar = LinearFontScalar(10, 40)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output/whale_wordcloud_small.png")
    }

    @Test
    fun whaleImgSmallAnglesTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(100)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"))
        val dimension = Dimension(500, 312)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 2
        wordCloud.angleGenerator = AngleGenerator(-90.0, 90.0, 10)
        wordCloud.background = PixelBoundaryBackground(getInputStream("backgrounds/whale_small.png"))
        wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0xFFFFFF))
        wordCloud.fontScalar = LinearFontScalar(10, 30)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output/whale_wordcloud_small_angles4.png")
    }

    @Test
    fun whaleImgLargeAnglesTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(600)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"))
        val dimension = Dimension(990, 618)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 2
        wordCloud.angleGenerator = AngleGenerator(-90.0, 90.0, 10)
        wordCloud.background = PixelBoundaryBackground(getInputStream("backgrounds/whale.png"))
        wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0xFFFFFF))
        wordCloud.fontScalar = LinearFontScalar(20, 50)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output/whale_wordcloud_large_angles2.png")
    }

    @Test
    fun datarankCircle() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(750)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 2
        wordCloud.background = CircleBackground(300)
        wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0xFFFFFF))
        wordCloud.fontScalar = SqrtFontScalar(10, 40)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/datarank_wordcloud_circle_sqrt_font.png")
    }

    @Test
    fun datarankCircleLarge() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(750)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"))
        val dimension = Dimension(1000, 1000)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 2
        wordCloud.background = CircleBackground(500)
        wordCloud.colorPalette = ColorPalette(Color(0x4055F1), Color(0x408DF1), Color(0x40AAF1), Color(0x40C5F1), Color(0x40D3F1), Color(0xFFFFFF))
        wordCloud.fontScalar = SqrtFontScalar(10, 50)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/datarank_wordcloud_circle_large2.png")
    }

    @Test
    fun datarankEarthImage() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(300)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 2
        wordCloud.kumoFont = KumoFont("Simple Slumg__G", FontWeight.BOLD)
        wordCloud.background = PixelBoundaryBackground(getInputStream("backgrounds/earth.bmp"))
        wordCloud.colorPalette = buildRandomColorPalette(3)
        wordCloud.fontScalar = SqrtFontScalar(10, 40)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/earth_image_word_cloud_different_fonts.png")
    }

    @Test
    fun catImage() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(300)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/tidy_cat_litter_top.txt"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 1
        wordCloud.kumoFont = KumoFont("Marker Felt", FontWeight.PLAIN)
        wordCloud.backgroundColor = Color.BLACK
        wordCloud.background = PixelBoundaryBackground(getInputStream("backgrounds/cat.bmp"))
        wordCloud.colorPalette = ColorPalette(Color(0xcccccc), Color(0xdddddd), Color(0xffffff))
        wordCloud.fontScalar = SqrtFontScalar(10, 50)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/tidy_cat_litter_black_cat.png")
    }

    @Ignore
    @Test
    fun datarankCode() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(600)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())

        val wordFrequencies = frequencyAnalyzer.load(FileInputStream("/tmp/code.txt"))
        val dimension = Dimension(990, 618)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 2
        wordCloud.backgroundColor = Color.WHITE
        wordCloud.background = PixelBoundaryBackground(getInputStream("backgrounds/whale.png"))
        wordCloud.colorPalette = buildRandomColorPalette(3)
        wordCloud.fontScalar = SqrtFontScalar(10, 50)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("/tmp/datarank_code.png")
    }

    @Ignore
    @Test
    fun largeCircleTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(5000)
        frequencyAnalyzer.setMinWordLength(2)

        val wordFrequencies = frequencyAnalyzer.load(FileInputStream("/tmp/code.txt"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 1
        wordCloud.background = CircleBackground(500)
        wordCloud.colorPalette = buildRandomColorPalette(20)
        wordCloud.fontScalar = LinearFontScalar(10, 100)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output/wordcloud_large_code_circle.png")
    }

    @Ignore
    @Test
    fun matchOnlineExample() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(600)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())
        val wordFrequencies = frequencyAnalyzer.load(FileInputStream("/tmp/code.txt"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 1
        wordCloud.background = CircleBackground(300)
        wordCloud.backgroundColor = Color.WHITE
        wordCloud.colorPalette = buildRandomColorPalette(2)
        wordCloud.kumoFont = KumoFont("Helvitica", FontWeight.PLAIN)
        wordCloud.fontScalar = LinearFontScalar(8, 130)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output/wordcloud_match_online_example.png")
    }

    @Test
    fun anotherRectangleTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(250)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.setStopWords(loadStopWords())
        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/sample.txt"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
        wordCloud.padding = 2
        wordCloud.background = RectangleBackground(dimension)
        wordCloud.colorPalette = buildRandomColorPalette(4)
        wordCloud.fontScalar = LinearFontScalar(30, 60)
        wordCloud.build(wordFrequencies)
        wordCloud.writeToFile("output/random_sample.png")
    }
}
