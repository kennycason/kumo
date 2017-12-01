package com.kennycason.kumo.palette;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kenny on 6/30/14.
 */
public class ColorPalette {
    private static final Random RANDOM = new Random();

    private final List<Color> colors;

    private int next;

    public ColorPalette(final Color... colors) {
        this.colors = new ArrayList<>();
        for (final Color color : colors) {
            this.colors.add(color);
        }
    }

    public ColorPalette(final int... colors) {
        this.colors = new ArrayList<>();
        for (final int color : colors) {
            this.colors.add(new Color (color));
        }
    }

    public ColorPalette(final List<Color> colors) {
        this.colors = colors;
    }

    public Color next() {
        return colors.get(next++ % colors.size());
    }

    public Color randomNext() {
        return colors.get(RANDOM.nextInt(colors.size()));
    }

    public List<Color> getColors() {
        return colors;
    }

}
