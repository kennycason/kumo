package com.kennycason.kumo.nlp.normalize;

import static org.apache.commons.lang3.StringUtils.lowerCase;

/**
 * Created by kenny on 7/1/14.
 */
public class LowerCaseNormalizer implements Normalizer {
    @Override
    public String apply(final String text) {
        return lowerCase(text);
    }
}
