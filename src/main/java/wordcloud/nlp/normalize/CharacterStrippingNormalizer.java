package wordcloud.nlp.normalize;

import java.util.regex.Pattern;

/**
 * Created by kenny on 7/1/14.
 */
public class CharacterStrippingNormalizer implements Normalizer {

    private static final Pattern DEFAULT_PATTERN = Pattern.compile("\\.|:|;|\\(|\\)|\"|,|\\?|,|!|<|>|/");

    private static final String DEFAULT_REPLACE_WITH = "";

    private final Pattern replacePattern;

    private final String replaceWith;

    public CharacterStrippingNormalizer() {
        replacePattern = DEFAULT_PATTERN;
        replaceWith = DEFAULT_REPLACE_WITH;
    }

    public CharacterStrippingNormalizer(final Pattern replacePattern, final String replaceWith) {
        this.replacePattern = replacePattern;
        this.replaceWith = replaceWith;
    }

    @Override
    public String normalize(String text) {
        return DEFAULT_PATTERN.matcher(text).replaceAll(DEFAULT_REPLACE_WITH);
    }

}
