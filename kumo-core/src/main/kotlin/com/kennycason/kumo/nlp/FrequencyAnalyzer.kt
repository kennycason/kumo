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
import java.io.Reader
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

    /** Analyzes raw text, not a file path. */
    fun analyzeText(
        text: String,
        autoFill: Boolean = false,
        autoFillWord: String = "nothing"
    ): List<WordFrequency> = analyzeLines(text.lineSequence(), autoFill, autoFillWord)

    /** Reads incrementally without closing the caller's reader. */
    fun analyze(
        reader: Reader,
        autoFill: Boolean = false,
        autoFillWord: String = "nothing"
    ): List<WordFrequency> = analyzeLines(reader.buffered().lineSequence(), autoFill, autoFillWord)

    /** Reads incrementally without closing the caller's stream. */
    fun analyze(
        inputStream: InputStream,
        autoFill: Boolean = false,
        autoFillWord: String = "nothing"
    ): List<WordFrequency> =
        analyze(inputStream.reader(charset(characterEncoding)), autoFill, autoFillWord)

    fun analyzeFile(file: File): List<WordFrequency> = file.inputStream().use { analyze(it) }

    fun analyzeFile(path: String): List<WordFrequency> = analyzeFile(File(path))

    /** Fetches a page and analyzes its visible body text. */
    fun analyzeUrl(url: URL): List<WordFrequency> {
        val doc = Jsoup.parse(url, urlLoadTimeout.toInt())
        return analyzeText(doc.body().text())
    }

    fun analyzeTexts(
        texts: Iterable<String>,
        autoFill: Boolean = false,
        autoFillWord: String = "nothing"
    ): List<WordFrequency> = analyzeLines(texts.asSequence().flatMap { it.lineSequence() }, autoFill, autoFillWord)

    /** Selects the largest existing frequencies without tokenizing them again. */
    fun topFrequencies(wordFrequencies: List<WordFrequency>): List<WordFrequency> =
        takeTopFrequencies(wordFrequencies)

    @Deprecated("Use analyze for streams", ReplaceWith("analyze(inputStream, autoFill, autoFillWord)"))
    fun load(inputStream: InputStream, autoFill: Boolean = false, autoFillWord: String = "nothing"): List<WordFrequency> =
        analyze(inputStream, autoFill, autoFillWord)

    @Deprecated("Use analyzeFile", ReplaceWith("analyzeFile(file)"))
    fun load(file: File): List<WordFrequency> = analyzeFile(file)

    @Deprecated("Use analyzeFile for paths or analyzeText for raw text", ReplaceWith("analyzeFile(filePath)"))
    fun load(filePath: String): List<WordFrequency> = analyzeFile(filePath)

    @Deprecated("Use analyzeUrl", ReplaceWith("analyzeUrl(url)"))
    fun load(url: URL): List<WordFrequency> = analyzeUrl(url)

    @Deprecated("Use analyzeTexts", ReplaceWith("analyzeTexts(texts, autoFill, autoFillWord)"))
    fun load(texts: List<String>, autoFill: Boolean = false, autoFillWord: String = "nothing"): List<WordFrequency> =
        analyzeTexts(texts, autoFill, autoFillWord)

    @Deprecated("Use topFrequencies", ReplaceWith("topFrequencies(wordFrequencies)"))
    fun loadWordFrequencies(wordFrequencies: List<WordFrequency>): List<WordFrequency> = topFrequencies(wordFrequencies)

    private fun analyzeLines(
        texts: Sequence<String>,
        autoFill: Boolean,
        autoFillWord: String
    ): List<WordFrequency> {
        require(!autoFill || autoFillWord.isNotBlank()) { "The auto-fill word must not be blank" }
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

    private fun buildWordFrequencies(texts: Sequence<String>, tokenizer: WordTokenizer): MutableMap<String, Int> {
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
