package com.kennycason.kumo.nlp.normalize

interface Normalizer {
    fun apply(input: String): String
}
