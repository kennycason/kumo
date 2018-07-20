package com.kennycason.kumo.nlp;

import com.kennycason.kumo.WordFrequency;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class FrequencyAnalyzerTest {

    @Test
    public void defaultTokenizerTrimTest() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("trim_test.txt"));

        final Map<String, WordFrequency> wordFrequencyMap = wordFrequencies
                .stream()
                .collect(Collectors.toMap(WordFrequency::getWord,
                                          Function.identity()));

        assertEquals(2, wordFrequencyMap.get("random").getFrequency());
        assertEquals(1, wordFrequencyMap.get("some").getFrequency());
        assertEquals(1, wordFrequencyMap.get("with").getFrequency());
        assertEquals(1, wordFrequencyMap.get("spaces").getFrequency());
        assertEquals(1, wordFrequencyMap.get("i'm").getFrequency());
    }
}
