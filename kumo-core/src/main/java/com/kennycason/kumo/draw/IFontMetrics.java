package com.kennycason.kumo.draw;

public interface IFontMetrics {

    int getBottom();
    int getTop();
    int getLeading();

    int measure(String s);

    IFont getFont();

}
