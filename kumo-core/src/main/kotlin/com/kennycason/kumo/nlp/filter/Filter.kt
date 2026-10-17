package com.kennycason.kumo.nlp.filter

abstract class Filter {
    abstract fun test(word: String): Boolean
}
