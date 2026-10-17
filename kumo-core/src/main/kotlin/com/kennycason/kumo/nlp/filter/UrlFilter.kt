package com.kennycason.kumo.nlp.filter

class UrlFilter : Filter() {

    override fun test(word: String): Boolean =
        !word.startsWith("http://")
                && word.startsWith("https://")
                && word.startsWith("www.")
}
