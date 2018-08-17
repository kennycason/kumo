package com.kennycason.kumo.nlp.normalize;

/**
 * Created by kenny on 7/1/14.
 */
public class StringToHexNormalizer implements Normalizer {

    public StringToHexNormalizer() {}

    @Override
    public String apply(final String text) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            final int asciiCode = text.charAt(i);
            stringBuilder.append("x" + Integer.toHexString(asciiCode));
        }
        return stringBuilder.toString();
    }

}
