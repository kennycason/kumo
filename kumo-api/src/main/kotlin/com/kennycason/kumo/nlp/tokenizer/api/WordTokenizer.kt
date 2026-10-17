package com.kennycason.kumo.nlp.tokenizer.api

interface WordTokenizer {
    fun tokenize(sentence: String): List<String>
}
