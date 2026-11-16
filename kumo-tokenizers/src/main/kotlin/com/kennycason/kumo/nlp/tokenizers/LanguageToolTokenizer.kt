package com.kennycason.kumo.nlp.tokenizers

import org.languagetool.Language
import org.languagetool.tokenizers.Tokenizer

internal class LanguageToolTokenizer(private val language: Language) : Tokenizer {

    override fun tokenize(sentence: String): List<String> {
        val tokenizer = language.wordTokenizer
        val rawTokens = tokenizer.tokenize(sentence)
        return rawTokens
            .map { parseToken(it) }  // parse parts-of-speech tags away (政府/n, 依照/p, 法律/n, 行/ng, 使/v, 执法/vn)
            .filter { it.isNotBlank() }
    }

    companion object {
        private fun parseToken(rawToken: String): String {
            val trimmed = if (rawToken.contains("/")) {
                rawToken.substring(0, rawToken.indexOf('/'))
            } else {
                rawToken
            }
            return trimmed.trim()
        }
    }
}
