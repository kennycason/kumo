package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.placement.LinearWordPlacer
import com.kennycason.kumo.placement.RTreeWordPlacer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.Test
import java.awt.Dimension
import java.io.InputStream

private val logger = KotlinLogging.logger {}

class WordPlacerPerfTest {

    companion object {
        private const val FREQUENCIES_TO_PLACE = 500

        private fun getInputStream(path: String): InputStream {
            return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
        }
    }

    @Test
    fun linearPlacement() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(FREQUENCIES_TO_PLACE)

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"))
        val wordCloud = WordCloud(Dimension(1000, 1000), CollisionMode.RECTANGLE)
        wordCloud.wordPlacer = LinearWordPlacer()

        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "linear: ${System.currentTimeMillis() - startTime}ms to build" }
    }

    @Test
    fun rTreePlacement() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setWordFrequenciesToReturn(FREQUENCIES_TO_PLACE)

        val wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"))
        val wordCloud = WordCloud(Dimension(1000, 1000), CollisionMode.RECTANGLE)
        wordCloud.wordPlacer = RTreeWordPlacer()

        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "rtree: ${System.currentTimeMillis() - startTime}ms to build" }
    }
}
