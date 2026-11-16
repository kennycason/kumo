package com.kennycason.kumo.nlp.tokenizer.api

fun interface WordTokenizer {
    fun tokenize(sentence: String): List<String>
}
