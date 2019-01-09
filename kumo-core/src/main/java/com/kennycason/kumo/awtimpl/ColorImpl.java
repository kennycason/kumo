package com.kennycason.kumo.awtimpl;

import com.kennycason.kumo.abst.ColorAbst;

import java.awt.*;

public class ColorImpl extends ColorAbst<Color> {

    private Color color;

    public ColorImpl(int red, int green, int blue) {
        super(red, green, blue);
        color = new Color(getRed(), getGreen(), getBlue());
    }

    public ColorImpl(Color c){
        super(c.getRed(), c.getGreen(), c.getBlue());
        color = c;
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
