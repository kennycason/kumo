package com.kennycason.kumo.nlp.tokenizers;

import com.kennycason.kumo.nlp.tokenizer.api.WordTokenizer;
import org.languagetool.language.English;

import java.util.List;

public class EnglishWordTokenizer implements WordTokenizer {
    private static final LanguageToolTokenizer TOKENIZER = new LanguageToolTokenizer(new English());

    public EnglishWordTokenizer() {}

    @Override
    public List<String> tokenize(final String sentence) {
        return TOKENIZER.tokenize(sentence);
    }

}
