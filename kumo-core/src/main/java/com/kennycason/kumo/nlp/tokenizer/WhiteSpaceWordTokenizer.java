package com.kennycason.kumo.nlp.tokenizer;


import java.util.Arrays;
import java.util.List;

public class WhiteSpaceWordTokenizer implements WordTokenizer {

    @Override
    public List<String> tokenize(String sentence) {
        return Arrays.asList(sentence.split("[ ]+"));
    }

}
