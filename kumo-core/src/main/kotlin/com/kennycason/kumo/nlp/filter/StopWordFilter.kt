package com.kennycason.kumo.nlp.filter

class StopWordFilter(stopWords: Collection<String>) : Filter {

    private val stopWords: Set<String> = HashSet(stopWords)

    override fun test(word: String): Boolean = word.lowercase() !in stopWords
}
