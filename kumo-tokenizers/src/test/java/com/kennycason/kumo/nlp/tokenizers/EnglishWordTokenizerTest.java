package com.kennycason.kumo.nlp.tokenizers;

import com.kennycason.kumo.nlp.tokenizer.WordTokenizer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by kenny on 4/27/15.
 */
public class EnglishWordTokenizerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnglishWordTokenizerTest.class);

    @Test
    public void test() {
        final long time = System.currentTimeMillis();
        final WordTokenizer tokenizer = new EnglishWordTokenizer();
        LOGGER.info("load time: " + (System.currentTimeMillis() - time) + " ms");

        assertEquals(Arrays.asList("Kenny"), tokenizer.tokenize("Kenny"));

        assertEquals(Arrays.asList("I", "like", "cats"), tokenizer.tokenize("I like cats"));

        assertEquals(Arrays.asList("Wow", ",", "my", "name", "is", "kumo", ".", "I", "'", "m", "a", "word", "cloud", "library"),
                     tokenizer.tokenize("Wow, my name is kumo. I'm a word cloud library"));

    }

}
