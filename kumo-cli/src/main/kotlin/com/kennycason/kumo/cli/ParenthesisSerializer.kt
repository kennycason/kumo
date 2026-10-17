package com.kennycason.kumo.cli

/**
 * Created by kenny on 6/12/16.
 */
object ParenthesisSerializer {
    private val PARENTHESIS_SPLIT_REGEX = Regex("""\)[ | ]*,[ | ]*\(""")

    fun <T> serialize(collection: Collection<T>): String {
        if (collection.isEmpty()) return ""
        val joined = collection.joinToString("),(") { it.toString() }
        return "($joined)"
    }

    fun deserialize(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()

        val normalizedValue = value.trim()
        if (normalizedValue.startsWith("(") && normalizedValue.endsWith(")")) {
            return PARENTHESIS_SPLIT_REGEX.split(
                normalizedValue.substring(1, normalizedValue.length - 1)
            )
        }

        return listOf(normalizedValue)
    }
}
