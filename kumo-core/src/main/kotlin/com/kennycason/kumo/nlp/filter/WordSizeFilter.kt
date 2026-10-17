package com.kennycason.kumo.nlp.filter

class WordSizeFilter(private val minLength: Int, private val maxLength: Int) : Filter() {

    override fun test(word: String): Boolean =
        word.isNotBlank() && word.length >= minLength && word.length < maxLength
}
