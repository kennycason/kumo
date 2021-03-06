package com.kennycason.kumo.nlp.tokenizer.core;

import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer;
import java.util.Collections;
import java.util.List;

/**
 * Disables tokenization
 * 
 * @author &#64;wolfposd
 *
 */
public class NoTokenizer implements WordTokenizer {

    public List<String> tokenize(final String sentence) {
        return Collections.singletonList(sentence);
    }

}
