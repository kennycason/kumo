package wordcloud.wsc;

import java.awt.Dimension;
import java.awt.Point;

import wordcloud.Word;

/**
 * Always returns the Center of the image
 * 
 * @author &#64;wolfposd
 */
public class CenterWordSpread implements WordSpreadScheme {

    @Override
    public Point getStartingPoint(Dimension imagedimensions, Word word) {
        int x = (imagedimensions.width / 2) - (word.getWidth() / 2);
        int y = (imagedimensions.height / 2) - (word.getHeight() / 2);
        return new Point(x, y);
    }

}
