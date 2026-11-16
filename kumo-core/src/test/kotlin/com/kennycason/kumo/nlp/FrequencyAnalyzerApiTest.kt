package com.kennycason.kumo.nlp

import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer
import java.io.Reader
import java.io.StringReader
import java.nio.file.Path
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse

class FrequencyAnalyzerApiTest {
    @TempDir
    lateinit var directory: Path

    @Test
    fun rawTextIsNotTreatedAsAPathAndNewlinesSeparateWords() {
        val words = FrequencyAnalyzer().analyzeText("cat cat\ndog\r\ncat")
        expectThat(words.map { it.word to it.frequency }).containsExactly("cat" to 3, "dog" to 1)
    }

    @Test
    fun filesReadersStreamsAndTextBatchesGiveTheSameFrequencies() {
        val text = "cat cat\ndog cat"
        val file = directory.resolve("words.txt").toFile().apply { writeText(text) }
        val analyzer = FrequencyAnalyzer()
        val expected = analyzer.analyzeText(text).map { it.word to it.frequency }
        val results = listOf(
            analyzer.analyzeFile(file), analyzer.analyzeFile(file.path),
            StringReader(text).use { analyzer.analyze(it) },
            text.byteInputStream().use { analyzer.analyze(it) },
            analyzer.analyzeTexts(listOf("cat cat", "dog cat"))
        )
        results.forEach { expectThat(it.map { frequency -> frequency.word to frequency.frequency }).containsExactly(expected) }
    }

    @Test
    fun callerOwnedReadersAreNotClosed() {
        val reader = object : StringReader("cat cat dog") {
            var closed = false
            override fun close() { closed = true; super.close() }
        }
        reader.use {
            expectThat(FrequencyAnalyzer().analyze(it).first().word).isEqualTo("cat")
            expectThat(reader.closed).isFalse()
        }
    }

    @Test
    fun linesAreCountedWhileReadingRatherThanCollectedFirst() {
        var tokenizedLines = 0
        val reader = object : Reader() {
            var readLines = 0
            override fun read(buffer: CharArray, offset: Int, length: Int): Int {
                expectThat(tokenizedLines).isEqualTo(readLines)
                if (readLines == 100) return -1
                "cat\n".toCharArray(buffer, offset)
                readLines++
                return 4
            }
            override fun close() = Unit
        }
        val analyzer = FrequencyAnalyzer().apply {
            wordTokenizer = object : WordTokenizer {
                override fun tokenize(sentence: String): List<String> {
                    tokenizedLines++
                    return listOf(sentence)
                }
            }
        }
        expectThat(analyzer.analyze(reader).single().frequency).isEqualTo(100)
    }

    @Test
    fun streamAnalysisHonorsConfiguredEncoding() {
        val analyzer = FrequencyAnalyzer().apply { characterEncoding = "ISO-8859-1" }
        val input = "café café".byteInputStream(Charsets.ISO_8859_1)
        input.use { expectThat(analyzer.analyze(it).single().word).isEqualTo("café") }
    }

    @Test
    fun selectsTopExistingFrequenciesWithoutReanalyzingThem() {
        val analyzer = FrequencyAnalyzer().apply { wordFrequenciesToReturn = 2 }
        val words = listOf(WordFrequency("cat", 2), WordFrequency("dog", 5), WordFrequency("owl", 3))
        expectThat(analyzer.topFrequencies(words).map { it.word }).containsExactly("dog", "owl")
        expectThat(words.map { it.word }).containsExactly("cat", "dog", "owl")
    }

    @Test
    @Suppress("DEPRECATION")
    fun deprecatedLoadMethodsDelegateToTheExplicitApis() {
        val analyzer = FrequencyAnalyzer()
        val file = directory.resolve("words.txt").toFile().apply { writeText("cat cat dog") }
        expectThat(analyzer.load(file.path).map { it.word to it.frequency })
            .containsExactly(analyzer.analyzeFile(file).map { it.word to it.frequency })
    }

    @Test
    fun blankAutoFillWordsAreRejected() {
        assertThrows<IllegalArgumentException> { FrequencyAnalyzer().analyzeText("", autoFill = true, autoFillWord = "") }
    }
}
