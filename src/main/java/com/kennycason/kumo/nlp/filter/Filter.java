package com.kennycason.kumo.nlp.filter;


import java.util.function.Predicate;

/**
 * Created by kenny on 7/1/14.
 */
public abstract class Filter implements Predicate<String> {

    @Override
    public Predicate<String> and(Predicate<? super String> other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate<String> negate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate<String> or(Predicate<? super String> other) {
        throw new UnsupportedOperationException();
    }

}

