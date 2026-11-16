package com.kennycason.kumo.nlp

import com.kennycason.kumo.exception.KumoException
import java.io.ByteArrayInputStream
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse

class FrequencyFileLoaderTest {

    @Test
    fun readsCallerOwnedStreamWithoutClosingIt() {
        val input = object : ByteArrayInputStream("2:cat\n1:dog".toByteArray()) {
            var closed = false
            override fun close() {
                closed = true
                super.close()
            }
        }
        input.use {
            expectThat(FrequencyFileLoader().load(it).map { frequency -> frequency.word })
                .containsExactly("cat", "dog")
            expectThat(input.closed).isFalse()
        }
    }

    private val frequencyFileLoader = FrequencyFileLoader()

    @Test
    fun basic() {
        val wordFrequencies = frequencyFileLoader.load(("100:cat\n" +
            "50:dog\n" +
            "25:fish\n" +
            "75:frog").byteInputStream())

        expectThat(wordFrequencies.size).isEqualTo(4)
        expectThat(wordFrequencies[0].frequency).isEqualTo(100)
        expectThat(wordFrequencies[0].word).isEqualTo("cat")
        expectThat(wordFrequencies[1].frequency).isEqualTo(75)
        expectThat(wordFrequencies[1].word).isEqualTo("frog")
        expectThat(wordFrequencies[2].frequency).isEqualTo(50)
        expectThat(wordFrequencies[2].word).isEqualTo("dog")
        expectThat(wordFrequencies[3].frequency).isEqualTo(25)
        expectThat(wordFrequencies[3].word).isEqualTo("fish")
    }

    // read valid file, and assert sorted order, ignores empty lines, spaces, and tabs
    @Test
    fun validFile() {
        val wordFrequencies = Thread.currentThread().contextClassLoader.getResourceAsStream("frequency_file.txt").use { frequencyFileLoader.load(it) }

        expectThat(wordFrequencies.size).isEqualTo(4)
        expectThat(wordFrequencies[0].frequency).isEqualTo(100)
        expectThat(wordFrequencies[0].word).isEqualTo("cat")
        expectThat(wordFrequencies[1].frequency).isEqualTo(75)
        expectThat(wordFrequencies[1].word).isEqualTo("frog")
        expectThat(wordFrequencies[2].frequency).isEqualTo(50)
        expectThat(wordFrequencies[2].word).isEqualTo("dog")
        expectThat(wordFrequencies[3].frequency).isEqualTo(25)
        expectThat(wordFrequencies[3].word).isEqualTo("fish")
    }

    @Test
    fun malformedWord() {
        expectThrows<KumoException> {
            Thread.currentThread().contextClassLoader.getResourceAsStream("frequency_file_malformed_word.txt").use { frequencyFileLoader.load(it) }
        }
    }

    @Test
    fun malformedWordCount() {
        expectThrows<KumoException> {
            Thread.currentThread().contextClassLoader.getResourceAsStream("frequency_file_malformed_word_count.txt").use { frequencyFileLoader.load(it) }
        }
    }
}
