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
import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.stream.Collectors

class FrequencyAnalyzer {

    private val stopWords = HashSet<String>()
    private var wordTokenizer: WordTokenizer = WhiteSpaceWordTokenizer()
    private val filters = mutableListOf<Filter>()
    private val normalizers = mutableListOf<Normalizer>()
    private var wordFrequenciesToReturn = DEFAULT_WORD_FREQUENCIES_TO_RETURN
    private var maxWordLength = DEFAULT_WORD_MAX_LENGTH
    private var minWordLength = DEFAULT_WORD_MIN_LENGTH
    private var characterEncoding = DEFAULT_ENCODING
    private var urlLoadTimeout = DEFAULT_URL_LOAD_TIMEOUT

    init {
        normalizers.add(TrimToEmptyNormalizer())
        normalizers.add(CharacterStrippingNormalizer())
        normalizers.add(LowerCaseNormalizer())
    }

    @Throws(IOException::class)
    fun load(inputStream: InputStream): List<WordFrequency> =
        load(IOUtils.readLines(inputStream, characterEncoding))

    @Throws(IOException::class)
    fun load(file: File): List<WordFrequency> = load(FileInputStream(file))

    @Throws(IOException::class)
    fun load(filePath: String): List<WordFrequency> = load(File(filePath))

    @Throws(IOException::class)
    fun load(url: URL): List<WordFrequency> {
        val doc = Jsoup.parse(url, urlLoadTimeout.toInt())
        return load(listOf(doc.body().text()))
    }

    fun load(texts: List<String>): List<WordFrequency> {
        val wordFrequencies = mutableListOf<WordFrequency>()
        val cloud = buildWordFrequencies(texts, wordTokenizer)
        cloud.forEach { (key, value) -> wordFrequencies.add(WordFrequency(key, value)) }
        return takeTopFrequencies(wordFrequencies)
    }

    @Throws(IOException::class)
    fun load(inputStream: InputStream, autoFill: Boolean, autoFillWord: String): List<WordFrequency> =
        load(IOUtils.readLines(inputStream, characterEncoding), autoFill, autoFillWord)

    @Throws(IOException::class)
    fun load(inputStream: InputStream, autoFill: Boolean): List<WordFrequency> =
        load(IOUtils.readLines(inputStream, characterEncoding), autoFill, "nothing")

    fun load(texts: List<String>, autoFill: Boolean): List<WordFrequency> =
        load(texts, autoFill, "nothing")

    fun load(texts: List<String>, autoFill: Boolean, autoFillWord: String): List<WordFrequency> {
        if (!autoFill) return load(texts)

        val wordFrequencies = mutableListOf<WordFrequency>()
        val cloud = buildWordFrequencies(texts, wordTokenizer).toMutableMap()
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

    private fun buildWordFrequencies(texts: List<String>, tokenizer: WordTokenizer): Map<String, Int> =
        texts.stream()
            .map { tokenizer.tokenize(it) }
            .flatMap { it.stream() }
            .map { normalize(it) }
            .filter { buildFilter().test(it) }
            .collect(Collectors.groupingBy({ it }, Collectors.reducing(0, { 1 }, Integer::sum)))

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

    fun setStopWords(stopWords: Collection<String>) {
        this.stopWords.clear()
        this.stopWords.addAll(stopWords)
    }

    fun setWordFrequenciesToReturn(wordFrequenciesToReturn: Int) {
        this.wordFrequenciesToReturn = wordFrequenciesToReturn
    }

    fun setMinWordLength(minWordLength: Int) {
        this.minWordLength = minWordLength
    }

    fun setMaxWordLength(maxWordLength: Int) {
        this.maxWordLength = maxWordLength
    }

    fun setWordTokenizer(wordTokenizer: WordTokenizer) {
        this.wordTokenizer = wordTokenizer
    }

    fun clearFilters() {
        filters.clear()
    }

    fun addFilter(filter: Filter) {
        filters.add(filter)
    }

    fun setFilter(filter: Filter) {
        filters.clear()
        filters.add(filter)
    }

    fun clearNormalizers() {
        normalizers.clear()
    }

    fun addNormalizer(normalizer: Normalizer) {
        normalizers.add(normalizer)
    }

    fun setNormalizer(normalizer: Normalizer) {
        normalizers.clear()
        normalizers.add(normalizer)
    }

    fun setCharacterEncoding(characterEncoding: String) {
        this.characterEncoding = characterEncoding
    }

    fun setUrlLoadTimeout(urlLoadTimeout: Long) {
        this.urlLoadTimeout = urlLoadTimeout
    }

    companion object {
        const val DEFAULT_ENCODING = "UTF-8"
        const val DEFAULT_WORD_MAX_LENGTH = 32
        const val DEFAULT_WORD_MIN_LENGTH = 3
        const val DEFAULT_WORD_FREQUENCIES_TO_RETURN = 50
        const val DEFAULT_URL_LOAD_TIMEOUT = 3000L
    }
}
