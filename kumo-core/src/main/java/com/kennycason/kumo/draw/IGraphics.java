package com.kennycason.kumo.draw;

public interface IGraphics {

    void drawImg(IImage img, int x, int y);

    void drawString(String s, int x, int y, IColor color);

    void drawRect(IColor color, int x, int y, int width, int height);

    void enableAntiAliasing();

    void setFont(IFont font);

    IFontMetrics getFontMetrics();

}
