package com.kennycason.kumo.awtimpl;

import com.kennycason.kumo.interfaces.DimensionAbst;

import java.awt.*;

public class DimensionImpl extends DimensionAbst<Dimension> {

    public DimensionImpl(int width, int height) {
        super(width, height);
    }

    @Override
    public Dimension getActual() {
        return new Dimension(getWidth(), getHeight());
    }
}
