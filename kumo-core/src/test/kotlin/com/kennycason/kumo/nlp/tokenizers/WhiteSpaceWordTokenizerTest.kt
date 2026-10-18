package com.kennycason.kumo.nlp.tokenizers

import com.kennycason.kumo.nlp.tokenizer.core.WhiteSpaceWordTokenizer
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class WhiteSpaceWordTokenizerTest {

    @Test
    fun singleAndMultipleSpace() {
        val parser = WhiteSpaceWordTokenizer()

        val words = parser.tokenize("hello, i am a     programmer !!  ")
        expectThat(words.size).isEqualTo(6)
        expectThat(words[0]).isEqualTo("hello,")
        expectThat(words[1]).isEqualTo("i")
        expectThat(words[2]).isEqualTo("am")
        expectThat(words[3]).isEqualTo("a")
        expectThat(words[4]).isEqualTo("programmer")
        expectThat(words[5]).isEqualTo("!!")
    }
}
