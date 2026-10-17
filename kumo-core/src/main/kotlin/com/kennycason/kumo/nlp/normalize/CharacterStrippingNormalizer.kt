package com.kennycason.kumo.nlp.normalize

import java.util.regex.Pattern

class CharacterStrippingNormalizer @JvmOverloads constructor(
    private val replacePattern: Pattern = DEFAULT_PATTERN,
    private val replaceWith: String = DEFAULT_REPLACE_WITH
) : Normalizer {

    override fun apply(input: String): String =
        replacePattern.matcher(input).replaceAll(replaceWith)

    companion object {
        private val DEFAULT_PATTERN = Pattern.compile("\\.|:|;|\\(|\\)|\"|,|\\?|,|!|<|>|/")
        private const val DEFAULT_REPLACE_WITH = ""
    }
}
