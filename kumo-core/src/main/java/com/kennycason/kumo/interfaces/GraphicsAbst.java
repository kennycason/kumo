package com.kennycason.kumo.interfaces;

public abstract class GraphicsAbst {

    public abstract void drawImg(ImageAbst img, int x, int y);

    public abstract void drawRect(int x, int y, int height, int width);

    public abstract void drawString(String s, int x, int y);

    public abstract void setBackgroundColor(ColorAbst color);

    public abstract void translate(int x, int y);

    public abstract void rotate(double angle, int xPivot, int yPivot);

    public abstract void enableAntiAliasing();

    public abstract void setFont(FontAbst font);

    public abstract FontMetricsAbst getFontMetrics();

    public abstract void drawAndFinish(ImageAbst drawOn);
}
