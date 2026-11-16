package com.kennycason.kumo.nlp.normalize

class LowerCaseNormalizer : Normalizer {
    override fun apply(input: String): String = input.lowercase()
}
