package com.kennycason.kumo.nlp.filter

class CompositeFilter(private val filters: List<Filter>) : Filter() {

    override fun test(word: String): Boolean = filters.all { it.test(word) }
}
