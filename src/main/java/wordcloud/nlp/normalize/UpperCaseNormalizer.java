package wordcloud.nlp.normalize;

import static org.apache.commons.lang3.StringUtils.upperCase;

/**
 * Created by kenny on 7/1/14.
 */
public class UpperCaseNormalizer implements Normalizer {
    @Override
    public String normalize(final String text) {
        return upperCase(text);
    }
}
