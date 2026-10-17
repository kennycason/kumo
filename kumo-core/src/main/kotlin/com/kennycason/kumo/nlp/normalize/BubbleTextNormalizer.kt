package com.kennycason.kumo.nlp.normalize

/**
 * Replaces the characters a-zA-Z with their bubble pendants.
 */
class BubbleTextNormalizer : Normalizer {

    override fun apply(input: String): String = buildString {
        for (letter in input) {
            val index = NORMAL.indexOf(letter)
            append(if (index != -1) BUBBLES[index] else letter)
        }
    }

    companion object {
        private const val NORMAL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        private const val BUBBLES = "\u24D0\u24D1\u24D2\u24D3\u24D4\u24D5\u24D6\u24D7\u24D8\u24D9\u24DA\u24DB\u24DC\u24DD\u24DE\u24DF\u24E0\u24E1\u24E2\u24E3\u24E4\u24E5\u24E6\u24E7\u24E8\u24E9\u24B6\u24B7\u24B8\u24B9\u24BA\u24BB\u24BC\u24BD\u24BE\u24BF\u24C0\u24C1\u24C2\u24C3\u24C4\u24C5\u24C6\u24C7\u24C8\u24C9\u24CA\u24CB\u24CC\u24CD\u24CE\u24CF"
    }
}
