package com.kennycason.kumo.nlp.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kenny on 7/1/14.
 */
public class StopWordFilter extends Filter {

    private final Set<String> stopWords = new HashSet<>();

    public StopWordFilter(final Collection<String> stopWords) {
        this.stopWords.addAll(stopWords);
    }


    @Override
    public boolean test(final String word) {
       return !this.stopWords.contains(word.toLowerCase());
    }
}

