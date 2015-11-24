package wordcloud.wsc;

import java.awt.Dimension;
import java.awt.Point;

import wordcloud.Word;

/**
 * The WordSpreadScheme provides a possible starting position for a word on the
 * image
 * 
 * @author &#64;wolfposd
 *
 */
public interface WordSpreadScheme {

    /**
     * Calculate a starting point for the given word. The returned Point is not
     * necessarily the actual point on the image, rather a first try.
     * 
     * @param imagedimensions
     *            width/height of the image
     * @param word
     *            the word to be placed
     * @return X/Y starting position
     */
    public Point getStartingPoint(Dimension imagedimensions, Word word);

}
