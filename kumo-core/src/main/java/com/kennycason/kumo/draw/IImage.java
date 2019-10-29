package com.kennycason.kumo.draw;

public interface IImage<T> extends VaryingImpl<T>{

    int getColor(int x, int y);

    int getWidth();

    int getHeight();

}
