package com.kennycason.kumo

import com.kennycason.kumo.font.KumoFont

class WordFrequency(
    val word: String,
    val frequency: Int,
    val font: KumoFont? = null
) : Comparable<WordFrequency> {

    val hasFont: Boolean
        get() = font != null

    override fun compareTo(other: WordFrequency): Int = other.frequency - frequency

    override fun toString(): String =
        "WordFrequency [word=$word, frequency=$frequency, font=${font?.font?.fontName ?: "default"}]"
}
