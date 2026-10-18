package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.bg.CircleBackground
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.nlp.normalize.BubbleTextNormalizer
import com.kennycason.kumo.nlp.normalize.StringToHexNormalizer
import com.kennycason.kumo.nlp.normalize.UpperCaseNormalizer
import com.kennycason.kumo.nlp.normalize.UpsideDownNormalizer
import com.kennycason.kumo.logging.Logging
import org.junit.Test
import java.awt.Dimension
import java.net.URL

class WordCloudNormalizersITest : Logging {

    @Test
    fun upperCaseNormalizer() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(750)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.addNormalizer(UpperCaseNormalizer())

        val wordFrequencies = frequencyAnalyzer.load(URL("http://kennycason.com"))
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
        frequencyAnalyzer.setWordFrequenciesToReturn(750)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.addNormalizer(UpsideDownNormalizer())

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
        frequencyAnalyzer.setWordFrequenciesToReturn(750)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.addNormalizer(StringToHexNormalizer())

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
        frequencyAnalyzer.setWordFrequenciesToReturn(750)
        frequencyAnalyzer.setMinWordLength(5)
        frequencyAnalyzer.clearFilters()
        frequencyAnalyzer.clearNormalizers()

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
        frequencyAnalyzer.setWordFrequenciesToReturn(750)
        frequencyAnalyzer.addNormalizer(BubbleTextNormalizer())

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
