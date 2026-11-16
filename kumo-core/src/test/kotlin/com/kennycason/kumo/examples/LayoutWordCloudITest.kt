package com.kennycason.kumo.examples

import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.wordcloud
import java.io.File
import org.junit.jupiter.api.Test

/** Run manually to compare PNG and vector SVG output from the same seeded layout. */
class LayoutWordCloudITest {
    @Test
    fun exportLayout() {
        val analyzer = FrequencyAnalyzer().apply { wordFrequenciesToReturn = 150 }
        val words = Thread.currentThread().contextClassLoader.getResourceAsStream("text/datarank.txt")
            .use { analyzer.analyze(it) }
        val wordcloud = wordcloud(words) {
            size(1000, 700)
            seed = 42
            padding = 2
        }
        File("output_test").mkdirs()
        wordcloud.writePng("output_test/layout.png")
        wordcloud.writeSvg("output_test/layout.svg")
    }
}
