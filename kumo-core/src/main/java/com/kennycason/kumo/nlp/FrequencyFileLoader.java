package com.kennycason.kumo.nlp;

import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.exception.KumoException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.trimToNull;

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
public class FrequencyFileLoader {
    public static final String DEFAULT_ENCODING = "UTF-8";
    private static final String DEFAULT_ERROR_MESSAGE = "Frequency File format is: <frequency_count>: <word>. E.g. 100: foo";

    public List<WordFrequency> load(final File file) throws IOException {
        return load(new FileInputStream(file));
    }

    public List<WordFrequency> load(final InputStream inputStream) throws IOException {
        return processLines(IOUtils.readLines(inputStream, DEFAULT_ENCODING));
    }

    private static List<WordFrequency> processLines(final List<String> lines) {
        return lines.stream()
                    .filter(StringUtils::isNotBlank)
                    .map(FrequencyFileLoader::buildWordFrequency)
                    .sorted(WordFrequency::compareTo)
                    .collect(Collectors.toList());
    }

    private static WordFrequency buildWordFrequency(final String line) {
        if (line.isEmpty()) {
            throw new KumoException("Encountered an empty line in file. " + DEFAULT_ERROR_MESSAGE);
        }
        if (!line.contains(":")) {
            throw new KumoException("Unable to process line: [" + line + "]. " + DEFAULT_ERROR_MESSAGE);
        }

        final String[] parts = line.split(":");

        if (parts.length != 2) {
            throw new KumoException("Unable to process line: [" + line + "]. " + DEFAULT_ERROR_MESSAGE);
        }

        final int wordCount = NumberUtils.toInt(trimToNull(parts[0]), 0);
        if (wordCount <= 0) {
            throw new KumoException("Word frequency must be a valid number > 0. " + DEFAULT_ERROR_MESSAGE);
        }

        final String word = trimToNull(parts[1]);
        if (word == null) {
            throw new KumoException("Word must not be blank " + DEFAULT_ERROR_MESSAGE);
        }

        return new WordFrequency(word, wordCount);
    }

}
