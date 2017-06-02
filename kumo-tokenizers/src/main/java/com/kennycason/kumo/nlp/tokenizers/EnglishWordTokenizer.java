package com.kennycason.kumo.nlp.tokenizers;

import com.kennycason.kumo.nlp.tokenizer.WordTokenizer;
import org.languagetool.language.English;
import org.languagetool.tokenizers.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class EnglishWordTokenizer implements WordTokenizer {
    private static final English ENGLISH = new English();

    public EnglishWordTokenizer() {}

    @Override
    public List<String> tokenize(final String sentence) {
        final Tokenizer tokenizer = ENGLISH.getWordTokenizer();
        final List<String> rawTokens = tokenizer.tokenize(sentence);
        final List<String> tokens = new ArrayList<>();
        for (final String rawToken : rawTokens) {
            tokens.add(rawToken.substring(0, rawToken.indexOf('/')));
        }
        return tokens;
    }

}
