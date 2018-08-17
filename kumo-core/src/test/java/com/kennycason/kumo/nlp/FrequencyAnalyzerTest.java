package com.kennycason.kumo.nlp;

import com.kennycason.kumo.WordFrequency;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

    @Test
    public void noNormalizationNoFilter() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.clearFilters();
        frequencyAnalyzer.clearNormalizers();

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("text/simplymeasured.txt"));

        final Map<String, WordFrequency> wordFrequencyMap = wordFrequencies
                .stream()
                .collect(Collectors.toMap(WordFrequency::getWord,
                                          Function.identity()));

        assertEquals(15122, wordFrequencyMap.get("the").getFrequency());
        assertEquals(2086, wordFrequencyMap.get("The").getFrequency());
        assertEquals(3020, wordFrequencyMap.get("Instagram").getFrequency());
        assertEquals(1064, wordFrequencyMap.get("You").getFrequency());
        assertEquals(4434, wordFrequencyMap.get("you").getFrequency());
    }


    @Test
    public void largeTextFileTest() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("text/simplymeasured.txt"));

        final Map<String, WordFrequency> wordFrequencyMap = wordFrequencies
                .stream()
                .collect(Collectors.toMap(WordFrequency::getWord,
                                          Function.identity()));

        assertEquals(17336, wordFrequencyMap.get("the").getFrequency());
        assertEquals(12574, wordFrequencyMap.get("@simplymeasured").getFrequency());
        assertEquals(7187, wordFrequencyMap.get("and").getFrequency());
        assertEquals(5815, wordFrequencyMap.get("simply").getFrequency());
        assertEquals(5755, wordFrequencyMap.get("you").getFrequency());
        assertEquals(5501, wordFrequencyMap.get("measured").getFrequency());
        assertEquals(3733, wordFrequencyMap.get("instagram").getFrequency());
    }

    @Test
    public void largeTextFileTestWithStopWords() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setStopWords(Arrays.asList("the", "and", "you"));

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("text/simplymeasured.txt"));

        final Map<String, WordFrequency> wordFrequencyMap = wordFrequencies
                .stream()
                .collect(Collectors.toMap(WordFrequency::getWord,
                                          Function.identity()));

        assertFalse(wordFrequencyMap.containsKey("the"));
        assertFalse(wordFrequencyMap.containsKey("and"));
        assertFalse(wordFrequencyMap.containsKey("you"));

        assertEquals(12574, wordFrequencyMap.get("@simplymeasured").getFrequency());
        assertEquals(5815, wordFrequencyMap.get("simply").getFrequency());
        assertEquals(5501, wordFrequencyMap.get("measured").getFrequency());
        assertEquals(3733, wordFrequencyMap.get("instagram").getFrequency());
    }

}
