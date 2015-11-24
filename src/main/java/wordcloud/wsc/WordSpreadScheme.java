package wordcloud.wsc;

import java.awt.Dimension;
import java.awt.Point;

import wordcloud.Word;

/**
 * 
 * @author wolfposd
 *
 */
public interface WordSpreadScheme {

    public Point getStartingPoint(Dimension imagedimensions, Word word);

}
