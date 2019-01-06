package com.kennycason.kumo.awtimpl;

import com.kennycason.kumo.interfaces.ColorAbst;

import java.awt.*;

public class ColorImpl extends ColorAbst<Color> {

    private Color color;

    public ColorImpl(int red, int green, int blue) {
        super(red, green, blue);
        color = new Color(getRed(), getGreen(), getBlue());
    }

    @Override
    public Color getActual() {
        return color;
    }

    @Override
    public int getInt() {
        return color.getRGB();
    }
}
