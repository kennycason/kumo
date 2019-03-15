package com.kennycason.kumo.draw;

public interface IFontMetrics {

    int getBottom();
    int getTop();

    int measure(String s);

    IFont getFont();

}
