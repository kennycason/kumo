package com.kennycason.kumo.nlp

import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.nlp.filter.CompositeFilter
import com.kennycason.kumo.nlp.filter.Filter
import com.kennycason.kumo.nlp.filter.StopWordFilter
import com.kennycason.kumo.nlp.filter.WordSizeFilter
import com.kennycason.kumo.nlp.normalize.CharacterStrippingNormalizer
import com.kennycason.kumo.nlp.normalize.LowerCaseNormalizer
import com.kennycason.kumo.nlp.normalize.Normalizer
import com.kennycason.kumo.nlp.normalize.TrimToEmptyNormalizer
import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer
import com.kennycason.kumo.nlp.tokenizer.core.WhiteSpaceWordTokenizer
import java.io.File
import java.io.InputStream
import java.net.URL
import org.jsoup.Jsoup

class FrequencyAnalyzer {

    var stopWords: Set<String> = emptySet()
        set(value) {
            field = value.toSet()
        }
    var wordTokenizer: WordTokenizer = WhiteSpaceWordTokenizer()
    var filters: List<Filter> = emptyList()
    var normalizers: List<Normalizer> = listOf(
        TrimToEmptyNormalizer(), CharacterStrippingNormalizer(), LowerCaseNormalizer()
    )
    var wordFrequenciesToReturn = DEFAULT_WORD_FREQUENCIES_TO_RETURN
    var maxWordLength = DEFAULT_WORD_MAX_LENGTH
    var minWordLength = DEFAULT_WORD_MIN_LENGTH
    var characterEncoding = DEFAULT_ENCODING
    var urlLoadTimeout = DEFAULT_URL_LOAD_TIMEOUT

    /** Reads from a caller-owned stream without closing it. */
    fun load(
        inputStream: InputStream,
        autoFill: Boolean = false,
        autoFillWord: String = "nothing"
    ): List<WordFrequency> =
        load(inputStream.bufferedReader(charset(characterEncoding)).lineSequence().toList(), autoFill, autoFillWord)

    fun load(file: File): List<WordFrequency> = file.inputStream().use { load(it) }

    fun load(filePath: String): List<WordFrequency> = load(File(filePath))

    fun load(url: URL): List<WordFrequency> {
        val doc = Jsoup.parse(url, urlLoadTimeout.toInt())
        return load(listOf(doc.body().text()))
    }

    fun load(
        texts: List<String>,
        autoFill: Boolean = false,
        autoFillWord: String = "nothing"
    ): List<WordFrequency> {
        val wordFrequencies = mutableListOf<WordFrequency>()
        val cloud = buildWordFrequencies(texts, wordTokenizer)
        if (!autoFill) {
            return takeTopFrequencies(cloud.map { (word, frequency) -> WordFrequency(word, frequency) })
        }

        var totalLength = cloud.keys.sumOf { it.length }

        if (totalLength == 0) {
            cloud[autoFillWord] = 1
            totalLength = autoFillWord.length
            cloud.forEach { (key, value) -> wordFrequencies.add(WordFrequency(key, value)) }
        }

        val timesToAdd = maxOf(wordFrequenciesToReturn / totalLength, 1)
        for (i in 0 until timesToAdd) {
            cloud.forEach { (key, value) -> wordFrequencies.add(WordFrequency(key, value)) }
        }

        return takeTopFrequencies(wordFrequencies)
    }

    fun loadWordFrequencies(wordFrequencies: List<WordFrequency>): List<WordFrequency> =
        takeTopFrequencies(wordFrequencies)

    private fun buildWordFrequencies(texts: List<String>, tokenizer: WordTokenizer): MutableMap<String, Int> {
        val filter = buildFilter()
        val counts = mutableMapOf<String, Int>()
        for (text in texts) {
            for (token in tokenizer.tokenize(text)) {
                val normalized = normalize(token)
                if (filter.test(normalized)) {
                    counts[normalized] = (counts[normalized] ?: 0) + 1
                }
            }
        }
        return counts
    }

    private fun buildFilter(): Filter {
        val allFilters = mutableListOf<Filter>()
        allFilters.add(StopWordFilter(stopWords))
        allFilters.add(WordSizeFilter(minWordLength, maxWordLength))
        allFilters.addAll(filters)
        return CompositeFilter(allFilters)
    }

    private fun normalize(word: String): String =
        normalizers.fold(word) { acc, normalizer -> normalizer.apply(acc) }

    private fun takeTopFrequencies(wordCloudEntities: Collection<WordFrequency>): List<WordFrequency> =
        wordCloudEntities.sorted().take(wordFrequenciesToReturn)

    companion object {
        const val DEFAULT_ENCODING = "UTF-8"
        const val DEFAULT_WORD_MAX_LENGTH = 32
        const val DEFAULT_WORD_MIN_LENGTH = 3
        const val DEFAULT_WORD_FREQUENCIES_TO_RETURN = 50
        const val DEFAULT_URL_LOAD_TIMEOUT = 3000L
    }
}
