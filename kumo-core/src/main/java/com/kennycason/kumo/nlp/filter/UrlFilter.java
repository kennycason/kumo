package com.kennycason.kumo.nlp.filter;

/**
 * Created by kenny on 7/1/14.
 */
public class UrlFilter extends Filter {

    @Override
    public boolean test(final String word) {
        return !word.startsWith("http://")
                && word.startsWith("https://")
                && word.startsWith("www.");
    }

}

