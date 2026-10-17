package com.kennycason.kumo.nlp

import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.nlp.filter.Filter
import com.kennycason.kumo.nlp.normalize.Normalizer
import org.junit.Assert.*
import org.junit.Test

class FrequencyAnalyzerTest {

    @Test
    fun normalizeThenFilter() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.clearNormalizers()
        frequencyAnalyzer.clearFilters()
        frequencyAnalyzer.addNormalizer(object : Normalizer {
            override fun apply(input: String): String = input.replace("@", "")
        })
        frequencyAnalyzer.addFilter(object : Filter() {
            override fun test(word: String): Boolean = !word.contains("@")
        })

        val wordFrequencies = frequencyAnalyzer.load(listOf("@datarank"))
        assertEquals(1, wordFrequencies.size)
        assertEquals("datarank", wordFrequencies[0].word)
    }

    @Test
    fun defaultTokenizerTrim() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        val wordFrequencies = frequencyAnalyzer.load(
            Thread.currentThread().contextClassLoader.getResourceAsStream("trim_test.txt"))

        val wordFrequencyMap = wordFrequencies.associateBy { it.word }

        assertEquals(2, wordFrequencyMap["random"]!!.frequency)
        assertEquals(1, wordFrequencyMap["some"]!!.frequency)
        assertEquals(1, wordFrequencyMap["with"]!!.frequency)
        assertEquals(1, wordFrequencyMap["spaces"]!!.frequency)
        assertEquals(1, wordFrequencyMap["i'm"]!!.frequency)
    }

    @Test
    fun noNormalizationNoFilter() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.clearFilters()
        frequencyAnalyzer.clearNormalizers()

        val wordFrequencies = frequencyAnalyzer.load(
            Thread.currentThread().contextClassLoader.getResourceAsStream("text/simplymeasured.txt"))

        val wordFrequencyMap = wordFrequencies.associateBy { it.word }

        assertEquals(15122, wordFrequencyMap["the"]!!.frequency)
        assertEquals(2086, wordFrequencyMap["The"]!!.frequency)
        assertEquals(3020, wordFrequencyMap["Instagram"]!!.frequency)
        assertEquals(1064, wordFrequencyMap["You"]!!.frequency)
        assertEquals(4434, wordFrequencyMap["you"]!!.frequency)
    }

    @Test
    fun largeTextFileTest() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        val wordFrequencies = frequencyAnalyzer.load(
            Thread.currentThread().contextClassLoader.getResourceAsStream("text/simplymeasured.txt"))

        val wordFrequencyMap = wordFrequencies.associateBy { it.word }

        assertEquals(17336, wordFrequencyMap["the"]!!.frequency)
        assertEquals(12574, wordFrequencyMap["@simplymeasured"]!!.frequency)
        assertEquals(7187, wordFrequencyMap["and"]!!.frequency)
        assertEquals(5815, wordFrequencyMap["simply"]!!.frequency)
        assertEquals(5755, wordFrequencyMap["you"]!!.frequency)
        assertEquals(5501, wordFrequencyMap["measured"]!!.frequency)
        assertEquals(3733, wordFrequencyMap["instagram"]!!.frequency)
    }

    @Test
    fun largeTextFileTestWithStopWords() {
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.setStopWords(listOf("the", "and", "you"))

        val wordFrequencies = frequencyAnalyzer.load(
            Thread.currentThread().contextClassLoader.getResourceAsStream("text/simplymeasured.txt"))

        val wordFrequencyMap = wordFrequencies.associateBy { it.word }

        assertFalse(wordFrequencyMap.containsKey("the"))
        assertFalse(wordFrequencyMap.containsKey("and"))
        assertFalse(wordFrequencyMap.containsKey("you"))

        assertEquals(12574, wordFrequencyMap["@simplymeasured"]!!.frequency)
        assertEquals(5815, wordFrequencyMap["simply"]!!.frequency)
        assertEquals(5501, wordFrequencyMap["measured"]!!.frequency)
        assertEquals(3733, wordFrequencyMap["instagram"]!!.frequency)
    }
}
