package wordcloud.nlp.sanitize;

/**
 * Created by kenny on 7/1/14.
 */
public class BasicTextSanitizer implements Sanitizer {
    @Override
    public String sanitize(String text) {
        return text.replaceAll("\\.|:|;|\\(|\\)|\"|,|\\?|,|!|<|>|/", " ");
    }
}
