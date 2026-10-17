package com.kennycason.kumo.nlp.normalize

class TrimToEmptyNormalizer : Normalizer {
    override fun apply(input: String): String = input.trim()
}
