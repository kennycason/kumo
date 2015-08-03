package wordcloud.padding;

import wordcloud.Word;

/**
 * Created by kenny on 7/2/14.
 *
 * Add padding around words in the word cloud.
 */
public interface Padder {
    void pad(final Word word, final int padding);
}
