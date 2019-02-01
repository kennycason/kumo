package com.kennycason.kumo.androidimpl;

import android.graphics.Color;
import com.kennycason.kumo.abst.ColorAbst;

public class ColorImpl extends ColorAbst<Color> {

    private int color;

    public ColorImpl(int red, int green, int blue) {
        color = Color.argb(255, red, green, blue);
    }

    public ColorImpl(int col, boolean alphaValid){
        color = col;

        if(!alphaValid){
            color = Color.argb(255, getRed(), getGreen(), getBlue());
        }
    }

    public ColorImpl(int col){
        color = col;
    }

    @Override
    public int getInt() {
        return color;
    }

    @Override
    public int getRed() {
        return Color.red(color);
    }

    @Override
    public int getGreen() {
        return Color.green(color);
    }

    @Override
    public int getBlue() {
        return Color.blue(color);
    }

    @Override
    public Color getActual() {
        throw new NullPointerException("Color has no actual implementation in Android!");
    }
}
