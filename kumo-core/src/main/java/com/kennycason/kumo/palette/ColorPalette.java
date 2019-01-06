package com.kennycason.kumo.palette;

import com.kennycason.kumo.interfaces.ColorAbst;
import com.kennycason.kumo.interfaces.InstanceCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kenny on 6/30/14.
 */
public class ColorPalette {
    private static final Random RANDOM = new Random();

    private final List<ColorAbst> colors;

    private int next;

    public ColorPalette(final ColorAbst... colors) {
        this.colors = new ArrayList<>();
        for (final ColorAbst color : colors) {
            this.colors.add(color);
        }
    }

    public ColorPalette(final int... colors) {
        this.colors = new ArrayList<>();
        for (final int color : colors) {
            this.colors.add(InstanceCreator.color(color));
        }
    }

    public ColorPalette(final List<ColorAbst> colors) {
        this.colors = colors;
    }

    public ColorAbst next() {
        return colors.get(next++ % colors.size());
    }

    public ColorAbst randomNext() {
        return colors.get(RANDOM.nextInt(colors.size()));
    }

    public List<ColorAbst> getColors() {
        return colors;
    }

}
