package com.kennycason.kumo.awtimpl;

import com.kennycason.kumo.abst.ColorAbst;

import java.awt.*;

public class ColorImpl extends ColorAbst<Color> {

    private Color color;

    public ColorImpl(int red, int green, int blue) {
        super(red, green, blue);
        color = new Color(getRed(), getGreen(), getBlue());
    }

    public ColorImpl(int col, boolean alphaValid){
        super(new Color(col, alphaValid).getRed(), new Color(col, alphaValid).getGreen(), new Color(col, alphaValid).getBlue());
        color = new Color(col, alphaValid);
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
