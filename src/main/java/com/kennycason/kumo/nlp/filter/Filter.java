package com.kennycason.kumo.nlp.filter;

import ch.lambdaj.function.matcher.Predicate;

/**
 * Created by kenny on 7/1/14.
 */
public abstract class Filter extends Predicate<String> {
    public abstract boolean apply(String word);
}

