package com.kennycason.kumo.draw.awtimpl;

import com.kennycason.kumo.draw.IDimension;
import com.kennycason.kumo.draw.VaryingImpl;

import java.awt.*;

public class DimensionImpl implements IDimension, VaryingImpl<Dimension> {

    private Dimension actual;

    public DimensionImpl(int width, int height) {
        actual = new Dimension(width, height);
    }

    @Override
    public Dimension getActual() {
        return actual;
    }

    @Override
    public int getWidth() {
        return (int) actual.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) actual.getHeight();
    }
}
