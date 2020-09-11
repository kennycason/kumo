package com.kennycason.kumo.nlp.tokenizer.api;

import java.util.List;

public interface WordTokenizer {
    List<String> tokenize(String sentence);
}
