package com.kennycason.kumo.interfaces;

public abstract class FontMetricsAbst {

    public abstract int getBottom();
    public abstract int getTop();

    public abstract int measure(String s);

    public abstract FontAbst getFont();
}
