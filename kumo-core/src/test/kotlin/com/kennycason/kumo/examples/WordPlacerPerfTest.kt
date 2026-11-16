package com.kennycason.kumo.examples

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.placement.LinearWordPlacer
import com.kennycason.kumo.placement.RTreeWordPlacer
import java.awt.Dimension
import java.io.InputStream
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

/** Correctness smoke coverage. Repeatable performance measurements live in kumo-benchmarks. */
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
        frequencyAnalyzer.wordFrequenciesToReturn = FREQUENCIES_TO_PLACE

        val wordFrequencies = getInputStream("text/datarank.txt").use { frequencyAnalyzer.analyze(it) }
        val wordCloud = WordCloud(Dimension(1000, 1000), CollisionMode.RECTANGLE)
        wordCloud.wordPlacer = LinearWordPlacer()
        wordCloud.seed = 42

        wordCloud.build(wordFrequencies)
        expectThat(wordCloud.layout.words.isNotEmpty()).isTrue()
        expectThat(wordCloud.layout.words.size + wordCloud.layout.skipped.size).isEqualTo(wordFrequencies.size)
    }

    @Test
    fun rTreePlacement() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.wordFrequenciesToReturn = FREQUENCIES_TO_PLACE

        val wordFrequencies = getInputStream("text/datarank.txt").use { frequencyAnalyzer.analyze(it) }
        val wordCloud = WordCloud(Dimension(1000, 1000), CollisionMode.RECTANGLE)
        wordCloud.wordPlacer = RTreeWordPlacer()
        wordCloud.seed = 42

        wordCloud.build(wordFrequencies)
        expectThat(wordCloud.layout.words.isNotEmpty()).isTrue()
        expectThat(wordCloud.layout.words.size + wordCloud.layout.skipped.size).isEqualTo(wordFrequencies.size)
    }
}
