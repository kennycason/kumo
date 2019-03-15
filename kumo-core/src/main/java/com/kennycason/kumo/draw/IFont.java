package com.kennycason.kumo.draw;

public interface IFont<T> extends VaryingImpl<T>{

    void registerIfNecessary();

    void setSize(float size);

    float getSize();
}
