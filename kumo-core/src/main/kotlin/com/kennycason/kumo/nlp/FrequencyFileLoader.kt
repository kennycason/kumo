package com.kennycason.kumo.nlp

import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.exception.KumoException
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * The purpose of this file is to load already computed frequency - word pairs from a text file.
 *
 * Sample format:
 * 100: frog
 * 94: dog
 * 43: cog
 * 20: bog
 * 3: fog
 * 1: log
 * 1: pog
 */
class FrequencyFileLoader {

    @Throws(IOException::class)
    fun load(file: File): List<WordFrequency> = load(FileInputStream(file))

    @Throws(IOException::class)
    fun load(inputStream: InputStream): List<WordFrequency> =
        processLines(IOUtils.readLines(inputStream, DEFAULT_ENCODING))

    companion object {
        const val DEFAULT_ENCODING = "UTF-8"
        private const val DEFAULT_ERROR_MESSAGE =
            "Frequency File format is: <frequency_count>: <word>. E.g. 100: foo"

        private fun processLines(lines: List<String>): List<WordFrequency> =
            lines.filter { it.isNotBlank() }
                .map { buildWordFrequency(it) }
                .sorted()

        private fun buildWordFrequency(line: String): WordFrequency {
            if (line.isEmpty()) {
                throw KumoException("Encountered an empty line in file. $DEFAULT_ERROR_MESSAGE")
            }
            if (!line.contains(":")) {
                throw KumoException("Unable to process line: [$line]. $DEFAULT_ERROR_MESSAGE")
            }

            val parts = line.split(":")
            if (parts.size != 2) {
                throw KumoException("Unable to process line: [$line]. $DEFAULT_ERROR_MESSAGE")
            }

            val wordCount = parts[0].trim().toIntOrNull() ?: 0
            if (wordCount <= 0) {
                throw KumoException("Word frequency must be a valid number > 0. $DEFAULT_ERROR_MESSAGE")
            }

            val word = parts[1].trim().ifEmpty { null }
            if (word == null) {
                throw KumoException("Word must not be blank $DEFAULT_ERROR_MESSAGE")
            }

            return WordFrequency(word, wordCount)
        }
    }
}
