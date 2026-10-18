package com.kennycason.kumo.nlp.tokenizers

import com.kennycason.kumo.logging.Logging
import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class EnglishWordTokenizerTest : Logging {

    @Test
    fun test() {
        val time = System.currentTimeMillis()
        val tokenizer: WordTokenizer = EnglishWordTokenizer()
        logger.info { "load time: ${System.currentTimeMillis() - time} ms" }

        expectThat(tokenizer.tokenize("Kenny")).isEqualTo(listOf("Kenny"))

        expectThat(tokenizer.tokenize("I like cats")).isEqualTo(listOf("I", "like", "cats"))

        expectThat(tokenizer.tokenize("Wow, my name is kumo. I'm a word cloud library")).isEqualTo(listOf("Wow", ",", "my", "name", "is", "kumo", ".", "I", "'", "m", "a", "word", "cloud", "library"))
    }
}
