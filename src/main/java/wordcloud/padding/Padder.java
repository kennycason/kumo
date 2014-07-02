package wordcloud.padding;

import wordcloud.Word;

import java.awt.*;

/**
 * Created by kenny on 7/2/14.
 */
public interface Padder {
    void pad(final Word word, final int padding, final Color color);
}
