package com.kennycason.kumo.nlp.tokenizers;

import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer;
import org.languagetool.language.Chinese;

import java.util.List;

public class ChineseWordTokenizer implements WordTokenizer {
    private static final LanguageToolTokenizer TOKENIZER = new LanguageToolTokenizer(new Chinese());

    @Override
    public List<String> tokenize(final String sentence) {
        return TOKENIZER.tokenize(sentence);
    }

}
