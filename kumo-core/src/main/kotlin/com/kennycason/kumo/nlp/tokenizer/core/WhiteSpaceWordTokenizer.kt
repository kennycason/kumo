package com.kennycason.kumo.nlp.tokenizer.core

import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer

class WhiteSpaceWordTokenizer : WordTokenizer {

    override fun tokenize(sentence: String): List<String> =
        sentence.trim().split(WHITESPACE_REGEX).filter { it.isNotEmpty() }

    companion object {
        private val WHITESPACE_REGEX = Regex("[ ]+")
    }
}
