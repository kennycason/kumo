package com.kennycason.kumo.interfaces;

public abstract class ImageAbst<IMPL> implements VaryingImpl<IMPL>{

    protected int width;
    protected int height;

    public ImageAbst(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public abstract int getColor(int x, int y);

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
