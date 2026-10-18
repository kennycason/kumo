package com.kennycason.kumo.nlp.normalize

fun interface Normalizer {
    fun apply(input: String): String
}
