package wordcloud.font;

import java.awt.*;

/**
 * Created by kenny on 7/3/14.
 */
public class KumoFont {

    private static final int DEFAULT_WEIGHT = 10;

    private final Font font;

    public KumoFont(final String type, final FontWeight weight) {
        this.font = new Font(type, weight.getWeight(), DEFAULT_WEIGHT);
    }

    public KumoFont(final Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
    }

}
