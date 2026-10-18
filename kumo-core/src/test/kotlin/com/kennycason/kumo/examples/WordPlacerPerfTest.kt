package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.logging.Logging
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.placement.LinearWordPlacer
import com.kennycason.kumo.placement.RTreeWordPlacer
import java.awt.Dimension
import java.io.InputStream
import org.junit.jupiter.api.Test

class WordPlacerPerfTest : Logging {

    companion object {
        private const val FREQUENCIES_TO_PLACE = 500

        private fun getInputStream(path: String): InputStream {
            return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
        }
    }

    @Test
    fun linearPlacement() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = FREQUENCIES_TO_PLACE

        val wordFrequencies = getInputStream("text/datarank.txt").use { frequencyAnalyzer.load(it) }
        val wordCloud = WordCloud(Dimension(1000, 1000), CollisionMode.RECTANGLE)
        wordCloud.wordPlacer = LinearWordPlacer()

        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "linear: ${System.currentTimeMillis() - startTime}ms to build" }
    }

    @Test
    fun rTreePlacement() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = FREQUENCIES_TO_PLACE

        val wordFrequencies = getInputStream("text/datarank.txt").use { frequencyAnalyzer.load(it) }
        val wordCloud = WordCloud(Dimension(1000, 1000), CollisionMode.RECTANGLE)
        wordCloud.wordPlacer = RTreeWordPlacer()

        val startTime = System.currentTimeMillis()
        wordCloud.build(wordFrequencies)
        logger.info { "rtree: ${System.currentTimeMillis() - startTime}ms to build" }
    }
}
