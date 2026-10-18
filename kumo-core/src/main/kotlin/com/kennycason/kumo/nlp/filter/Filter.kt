package com.kennycason.kumo.nlp.filter

fun interface Filter {
    fun test(word: String): Boolean
}
