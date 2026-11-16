package com.kennycason.kumo.nlp.normalize

class UpperCaseNormalizer : Normalizer {
    override fun apply(input: String): String = input.uppercase()
}
