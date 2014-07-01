package wordcloud.palette;

import java.awt.*;

/**
 * Created by kenny on 6/30/14.
 */
public class ColorPalette {

    private final Color[] colors;

    private int next = 0;

    public ColorPalette(Color... colors) {
        this.colors = colors;
    }

    public Color next() {
        return colors[next++ % colors.length];
    }

}
