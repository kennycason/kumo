package com.kennycason.kumo.nlp.normalize;

/**
 * Replaces the characters a-zA-Z with their bubble pendants ⓐ-ⓩⒶ-Ⓩ
 * 
 * @author &#64;wolfposd
 *
 */
public class BubbleTextNormalizer implements Normalizer {

    private static final String NORMAL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String BUBBLES = "ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏ";

    @Override
    public String apply(final String text) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            final char letter = text.charAt(i);
            final int index = NORMAL.indexOf(letter);
            stringBuilder.append((index != -1) ? BUBBLES.charAt(index) : letter);
        }
        return stringBuilder.toString();
    }

}
