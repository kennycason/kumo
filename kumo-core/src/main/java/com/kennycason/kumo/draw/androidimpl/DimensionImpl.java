package com.kennycason.kumo.draw.androidimpl;

import com.kennycason.kumo.draw.IDimension;

public class DimensionImpl implements IDimension {

    private int width;
    private int height;

    public DimensionImpl(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
