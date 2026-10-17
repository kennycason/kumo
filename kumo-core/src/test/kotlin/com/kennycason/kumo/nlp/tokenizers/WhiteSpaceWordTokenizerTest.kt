package com.kennycason.kumo.nlp.tokenizers

import com.kennycason.kumo.nlp.tokenizer.core.WhiteSpaceWordTokenizer
import org.junit.Assert.*
import org.junit.Test

class WhiteSpaceWordTokenizerTest {

    @Test
    fun singleAndMultipleSpace() {
        val parser = WhiteSpaceWordTokenizer()

        val words = parser.tokenize("hello, i am a     programmer !!  ")
        assertEquals(6, words.size)
        assertEquals("hello,", words[0])
        assertEquals("i", words[1])
        assertEquals("am", words[2])
        assertEquals("a", words[3])
        assertEquals("programmer", words[4])
        assertEquals("!!", words[5])
    }
}
