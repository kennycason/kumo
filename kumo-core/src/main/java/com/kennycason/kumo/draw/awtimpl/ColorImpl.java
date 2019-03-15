package com.kennycason.kumo.draw.awtimpl;

import com.kennycason.kumo.draw.IColor;
import com.kennycason.kumo.draw.VaryingImpl;

import java.awt.*;

public class ColorImpl implements IColor, VaryingImpl<Color> {

    private Color color;

    public ColorImpl(int red, int green, int blue) {
        color = new Color(red, green, blue);
    }

    public ColorImpl(int col, boolean alphaValid){
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

    @Override
    public int getRed() {
        return color.getRed();
    }

    @Override
    public int getGreen() {
        return color.getGreen();
    }

    @Override
    public int getBlue() {
        return color.getBlue();
    }
}
