package com.kennycason.kumo.nlp;

import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.exception.KumoException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FrequencyFileLoaderTest {
    private static final FrequencyFileLoader FREQUENCY_FILE_LOADER = new FrequencyFileLoader();

    @Test
    public void basic() throws IOException {
        final List<WordFrequency> wordFrequencies = FREQUENCY_FILE_LOADER.load(IOUtils.toInputStream(
                "100:cat\n" +
                "50:dog\n" +
                "25:fish\n" +
                "75:frog"));

        assertEquals(4, wordFrequencies.size());
        assertEquals(100, wordFrequencies.get(0).getFrequency());
        assertEquals("cat", wordFrequencies.get(0).getWord());
        assertEquals(75, wordFrequencies.get(1).getFrequency());
        assertEquals("frog", wordFrequencies.get(1).getWord());
        assertEquals(50, wordFrequencies.get(2).getFrequency());
        assertEquals("dog", wordFrequencies.get(2).getWord());
        assertEquals(25, wordFrequencies.get(3).getFrequency());
        assertEquals("fish", wordFrequencies.get(3).getWord());
    }

    // read valid file, and assert sorted order, ignores empty lines, spaces, and tabs
    @Test
    public void validFile() throws IOException {
        final List<WordFrequency> wordFrequencies = FREQUENCY_FILE_LOADER.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("frequency_file.txt"));

        assertEquals(4, wordFrequencies.size());
        assertEquals(100, wordFrequencies.get(0).getFrequency());
        assertEquals("cat", wordFrequencies.get(0).getWord());
        assertEquals(75, wordFrequencies.get(1).getFrequency());
        assertEquals("frog", wordFrequencies.get(1).getWord());
        assertEquals(50, wordFrequencies.get(2).getFrequency());
        assertEquals("dog", wordFrequencies.get(2).getWord());
        assertEquals(25, wordFrequencies.get(3).getFrequency());
        assertEquals("fish", wordFrequencies.get(3).getWord());
    }

    @Test(expected = KumoException.class)
    public void malformedWord() throws IOException {
        FREQUENCY_FILE_LOADER.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("frequency_file_malformed_word.txt"));
    }

    @Test(expected = KumoException.class)
    public void malformedWordCount() throws IOException {
        FREQUENCY_FILE_LOADER.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("frequency_file_malformed_word_count.txt"));
    }

}
