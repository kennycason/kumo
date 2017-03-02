package com.kennycason.kumo.nlp.tokenizer;

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
