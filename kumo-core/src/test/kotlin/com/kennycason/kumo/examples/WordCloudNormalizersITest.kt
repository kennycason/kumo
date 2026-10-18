package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.bg.CircleBackground
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.logging.Logging
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.nlp.normalize.BubbleTextNormalizer
import com.kennycason.kumo.nlp.normalize.StringToHexNormalizer
import com.kennycason.kumo.nlp.normalize.UpperCaseNormalizer
import com.kennycason.kumo.nlp.normalize.UpsideDownNormalizer
import java.awt.Dimension
import java.net.URL
import org.junit.jupiter.api.Test

class WordCloudNormalizersITest : Logging {

    @Test
    fun upperCaseNormalizer() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 750
        frequencyAnalyzer.minWordLength = 5
        frequencyAnalyzer.normalizers += UpperCaseNormalizer()

        val wordFrequencies = frequencyAnalyzer.load(URL("https://kennycason.com"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.RECTANGLE)
        wordCloud.padding = 0
        wordCloud.background = CircleBackground(300)
        wordCloud.fontScalar = LinearFontScalar(10, 40)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_uppercase.png")
    }

    @Test
    fun upsideDownNormalizer() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 750
        frequencyAnalyzer.minWordLength = 5
        frequencyAnalyzer.normalizers += UpsideDownNormalizer()

        val wordFrequencies = frequencyAnalyzer.load(URL("http://kennycason.com"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.RECTANGLE)
        wordCloud.padding = 0
        wordCloud.background = CircleBackground(300)
        wordCloud.fontScalar = LinearFontScalar(10, 40)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_upsidedown.png")
    }

    @Test
    fun hexStringNormalizer() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 750
        frequencyAnalyzer.minWordLength = 5
        frequencyAnalyzer.normalizers += StringToHexNormalizer()

        val wordFrequencies = frequencyAnalyzer.load(URL("http://kennycason.com"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.RECTANGLE)
        wordCloud.padding = 0
        wordCloud.background = CircleBackground(300)
        wordCloud.fontScalar = LinearFontScalar(10, 40)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_hex.png")
    }

    @Test
    fun noNormalizationOrFilters() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 750
        frequencyAnalyzer.minWordLength = 5
        frequencyAnalyzer.filters = emptyList()
        frequencyAnalyzer.normalizers = emptyList()

        val wordFrequencies = frequencyAnalyzer.load(URL("http://kennycason.com"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.RECTANGLE)
        wordCloud.padding = 0
        wordCloud.background = CircleBackground(300)
        wordCloud.fontScalar = LinearFontScalar(10, 40)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_normalization_or_filter.png")
    }

    @Test
    fun bubbleText() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = 750
        frequencyAnalyzer.normalizers += BubbleTextNormalizer()

        val wordFrequencies = frequencyAnalyzer.load(URL("http://kennycason.com"))
        val dimension = Dimension(600, 600)
        val wordCloud = WordCloud(dimension, CollisionMode.RECTANGLE)
        wordCloud.padding = 0
        wordCloud.fontScalar = LinearFontScalar(10, 40)
        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "Took ${System.currentTimeMillis() - startTime}ms to build" }
        wordCloud.writeToFile("output/bubbletext.png")
    }
}
