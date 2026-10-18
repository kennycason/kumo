package com.kennycason.kumo.nlp.tokenizer.core

import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer

/**
 * Disables tokenization
 */
class NoTokenizer : WordTokenizer {

    override fun tokenize(sentence: String): List<String> = listOf(sentence)
}
