package com.kennycason.kumo;

/**
 * Created by kenny on 6/29/14.
 */
public class WordFrequency implements Comparable<WordFrequency> {

    private final String word;

    private final int frequency;

    public WordFrequency(final String word, final int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public int compareTo(final WordFrequency wordFrequency) {
        return  wordFrequency.frequency - frequency;
    }

    @Override
    public String toString() {
        return "WordFrequency [word=" + word + ", frequency=" + frequency + "]";
    }
}
