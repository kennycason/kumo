package com.kennycason.kumo.nlp

import com.kennycason.kumo.nlp.filter.Filter
import com.kennycason.kumo.nlp.normalize.Normalizer
import java.io.ByteArrayInputStream
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse

class FrequencyAnalyzerTest {

    @Test
    fun readsCallerOwnedStreamWithoutClosingIt() {
        val input = object : ByteArrayInputStream("cat cat dog".toByteArray()) {
            var closed = false
            override fun close() {
                closed = true
                super.close()
            }
        }
        input.use {
            val frequencies = FrequencyAnalyzer().analyze(it)
            expectThat(frequencies.map { frequency -> frequency.word to frequency.frequency })
                .containsExactly("cat" to 2, "dog" to 1)
            expectThat(input.closed).isFalse()
        }
    }

    @Test
    fun customAutoFillMatchesForTextAndStreamInputs() {
        val analyzer = FrequencyAnalyzer().apply { wordFrequenciesToReturn = 6 }
        val fromText = analyzer.analyzeTexts(emptyList(), autoFill = true, autoFillWord = "cat")
        val fromStream = "".byteInputStream().use {
            analyzer.analyze(it, autoFill = true, autoFillWord = "cat")
        }
        expectThat(fromText.map { it.word }).containsExactly("cat", "cat", "cat")
        expectThat(fromStream.map { it.word }).containsExactly("cat", "cat", "cat")
        expectThat(analyzer.analyzeTexts(emptyList())).isEmpty()
    }

    @Test
    fun stopWordsAreCopiedWhenAssigned() {
        val stopWords = mutableSetOf("the")
        val analyzer = FrequencyAnalyzer().apply { this.stopWords = stopWords }
        stopWords.clear()

        expectThat(analyzer.analyzeTexts(listOf("the cat")).map { it.word }).containsExactly("cat")
    }

    @Test
    fun normalizeThenFilter() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.normalizers = listOf(Normalizer { it.replace("@", "") })
        frequencyAnalyzer.filters = listOf(Filter { "@" !in it })

        val wordFrequencies = frequencyAnalyzer.analyzeTexts(listOf("@datarank"))
        expectThat(wordFrequencies.size).isEqualTo(1)
        expectThat(wordFrequencies[0].word).isEqualTo("datarank")
    }

    @Test
    fun defaultTokenizerTrim() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        val wordFrequencies = Thread.currentThread().contextClassLoader.getResourceAsStream("trim_test.txt").use { frequencyAnalyzer.analyze(it) }

        val wordFrequencyMap = wordFrequencies.associateBy { it.word }

        expectThat(wordFrequencyMap.getValue("random").frequency).isEqualTo(2)
        expectThat(wordFrequencyMap.getValue("some").frequency).isEqualTo(1)
        expectThat(wordFrequencyMap.getValue("with").frequency).isEqualTo(1)
        expectThat(wordFrequencyMap.getValue("spaces").frequency).isEqualTo(1)
        expectThat(wordFrequencyMap.getValue("i'm").frequency).isEqualTo(1)
    }

    @Test
    fun noNormalizationNoFilter() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.filters = emptyList()
        frequencyAnalyzer.normalizers = emptyList()

        val wordFrequencies = Thread.currentThread().contextClassLoader.getResourceAsStream("text/simplymeasured.txt").use { frequencyAnalyzer.analyze(it) }

        val wordFrequencyMap = wordFrequencies.associateBy { it.word }

        expectThat(wordFrequencyMap.getValue("the").frequency).isEqualTo(15122)
        expectThat(wordFrequencyMap.getValue("The").frequency).isEqualTo(2086)
        expectThat(wordFrequencyMap.getValue("Instagram").frequency).isEqualTo(3020)
        expectThat(wordFrequencyMap.getValue("You").frequency).isEqualTo(1064)
        expectThat(wordFrequencyMap.getValue("you").frequency).isEqualTo(4434)
    }

    @Test
    fun largeTextFileTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        val wordFrequencies = Thread.currentThread().contextClassLoader.getResourceAsStream("text/simplymeasured.txt").use { frequencyAnalyzer.analyze(it) }

        val wordFrequencyMap = wordFrequencies.associateBy { it.word }

        expectThat(wordFrequencyMap.getValue("the").frequency).isEqualTo(17336)
        expectThat(wordFrequencyMap.getValue("@simplymeasured").frequency).isEqualTo(12574)
        expectThat(wordFrequencyMap.getValue("and").frequency).isEqualTo(7187)
        expectThat(wordFrequencyMap.getValue("simply").frequency).isEqualTo(5815)
        expectThat(wordFrequencyMap.getValue("you").frequency).isEqualTo(5755)
        expectThat(wordFrequencyMap.getValue("measured").frequency).isEqualTo(5501)
        expectThat(wordFrequencyMap.getValue("instagram").frequency).isEqualTo(3733)
    }

    @Test
    fun largeTextFileTestWithStopWords() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.stopWords = listOf("the", "and", "you").toSet()

        val wordFrequencies = Thread.currentThread().contextClassLoader.getResourceAsStream("text/simplymeasured.txt").use { frequencyAnalyzer.analyze(it) }

        val wordFrequencyMap = wordFrequencies.associateBy { it.word }

        expectThat(wordFrequencyMap.containsKey("the")).isFalse()
        expectThat(wordFrequencyMap.containsKey("and")).isFalse()
        expectThat(wordFrequencyMap.containsKey("you")).isFalse()

        expectThat(wordFrequencyMap.getValue("@simplymeasured").frequency).isEqualTo(12574)
        expectThat(wordFrequencyMap.getValue("simply").frequency).isEqualTo(5815)
        expectThat(wordFrequencyMap.getValue("measured").frequency).isEqualTo(5501)
        expectThat(wordFrequencyMap.getValue("instagram").frequency).isEqualTo(3733)
    }
}
