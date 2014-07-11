package wordcloud.font;

import java.awt.*;

/**
 * Created by kenny on 7/3/14.
 */
public class CloudFont {

    private static final int DEFAULT_WEIGHT = 10;

    private final Font font;

    public CloudFont(String type, FontWeight weight) {
        this.font = new Font(type, weight.getWeight(), DEFAULT_WEIGHT);
    }

    public CloudFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
    }

}
