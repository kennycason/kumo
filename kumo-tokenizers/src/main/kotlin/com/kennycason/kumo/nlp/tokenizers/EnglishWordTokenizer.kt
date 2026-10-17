package com.kennycason.kumo.nlp.tokenizers

import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer
import org.languagetool.language.English

class EnglishWordTokenizer : WordTokenizer {

    override fun tokenize(sentence: String): List<String> {
        return TOKENIZER.tokenize(sentence)
    }

    companion object {
        private val TOKENIZER = LanguageToolTokenizer(English())
    }
}
