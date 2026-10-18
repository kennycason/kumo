package com.kennycason.kumo.nlp.normalize

class StringToHexNormalizer : Normalizer {

    override fun apply(input: String): String = buildString {
        for (char in input) {
            append("x${Integer.toHexString(char.code)}")
        }
    }
}
