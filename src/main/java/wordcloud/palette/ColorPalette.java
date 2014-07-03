package wordcloud.palette;

import java.awt.*;
import java.util.Random;

/**
 * Created by kenny on 6/30/14.
 */
public class ColorPalette {

    private static final Random RANDOM = new Random();


    private final Color[] colors;

    private int next = 0;

    public ColorPalette(Color... colors) {
        this.colors = colors;
    }

    public Color next() {
        return colors[next++ % colors.length];
    }

    public Color randomNext() {
        return colors[RANDOM.nextInt(colors.length)];
    }

}
