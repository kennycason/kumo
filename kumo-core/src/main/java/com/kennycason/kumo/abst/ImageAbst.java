package com.kennycason.kumo.abst;

public abstract class ImageAbst<IMPL> implements VaryingImpl<IMPL>{

    public abstract int getColor(int x, int y);

    public abstract int getWidth();

    public abstract int getHeight();
}
