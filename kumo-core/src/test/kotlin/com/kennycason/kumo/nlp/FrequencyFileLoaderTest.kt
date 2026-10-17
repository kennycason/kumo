package com.kennycason.kumo.nlp

import com.kennycason.kumo.exception.KumoException
import org.apache.commons.io.IOUtils
import org.junit.Assert.*
import org.junit.Test

class FrequencyFileLoaderTest {

    companion object {
        private val FREQUENCY_FILE_LOADER = FrequencyFileLoader()
    }

    @Test
    fun basic() {
        val wordFrequencies = FREQUENCY_FILE_LOADER.load(IOUtils.toInputStream(
            "100:cat\n" +
            "50:dog\n" +
            "25:fish\n" +
            "75:frog"))

        assertEquals(4, wordFrequencies.size)
        assertEquals(100, wordFrequencies[0].frequency)
        assertEquals("cat", wordFrequencies[0].word)
        assertEquals(75, wordFrequencies[1].frequency)
        assertEquals("frog", wordFrequencies[1].word)
        assertEquals(50, wordFrequencies[2].frequency)
        assertEquals("dog", wordFrequencies[2].word)
        assertEquals(25, wordFrequencies[3].frequency)
        assertEquals("fish", wordFrequencies[3].word)
    }

    // read valid file, and assert sorted order, ignores empty lines, spaces, and tabs
    @Test
    fun validFile() {
        val wordFrequencies = FREQUENCY_FILE_LOADER.load(Thread.currentThread().contextClassLoader.getResourceAsStream("frequency_file.txt"))

        assertEquals(4, wordFrequencies.size)
        assertEquals(100, wordFrequencies[0].frequency)
        assertEquals("cat", wordFrequencies[0].word)
        assertEquals(75, wordFrequencies[1].frequency)
        assertEquals("frog", wordFrequencies[1].word)
        assertEquals(50, wordFrequencies[2].frequency)
        assertEquals("dog", wordFrequencies[2].word)
        assertEquals(25, wordFrequencies[3].frequency)
        assertEquals("fish", wordFrequencies[3].word)
    }

    @Test(expected = KumoException::class)
    fun malformedWord() {
        FREQUENCY_FILE_LOADER.load(Thread.currentThread().contextClassLoader.getResourceAsStream("frequency_file_malformed_word.txt"))
    }

    @Test(expected = KumoException::class)
    fun malformedWordCount() {
        FREQUENCY_FILE_LOADER.load(Thread.currentThread().contextClassLoader.getResourceAsStream("frequency_file_malformed_word_count.txt"))
    }
}
