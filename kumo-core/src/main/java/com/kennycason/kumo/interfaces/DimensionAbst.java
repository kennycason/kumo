package com.kennycason.kumo.interfaces;

public abstract class DimensionAbst<IMPL> implements VaryingImpl<IMPL>{

    private int width;
    private int height;

    public DimensionAbst(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
