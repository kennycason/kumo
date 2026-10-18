package com.kennycason.kumo.nlp.tokenizers

import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer
import com.kennycason.kumo.logging.Logging
import org.junit.Assert.assertEquals
import org.junit.Test

class EnglishWordTokenizerTest : Logging {

    @Test
    fun test() {
        val time = System.currentTimeMillis()
        val tokenizer: WordTokenizer = EnglishWordTokenizer()
        logger.info { "load time: ${System.currentTimeMillis() - time} ms" }

        assertEquals(listOf("Kenny"), tokenizer.tokenize("Kenny"))

        assertEquals(listOf("I", "like", "cats"), tokenizer.tokenize("I like cats"))

        assertEquals(
            listOf("Wow", ",", "my", "name", "is", "kumo", ".", "I", "'", "m", "a", "word", "cloud", "library"),
            tokenizer.tokenize("Wow, my name is kumo. I'm a word cloud library")
        )
    }
}
