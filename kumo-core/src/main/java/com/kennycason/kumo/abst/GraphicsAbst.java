package com.kennycason.kumo.abst;

public abstract class GraphicsAbst {

    public abstract void drawImg(ImageAbst img, int x, int y);

    public abstract void drawString(String s, int x, int y, ColorAbst color);

    public abstract void drawRect(ColorAbst color, int x, int y, int width, int height);

    public abstract void translate(int x, int y);

    public abstract void rotate(double angle, int xPivot, int yPivot);

    public abstract void enableAntiAliasing();

    public abstract void setFont(FontAbst font);

    public abstract FontMetricsAbst getFontMetrics();

}
