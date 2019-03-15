package com.kennycason.kumo.draw.awtimpl;

import com.kennycason.kumo.draw.IFont;
import com.kennycason.kumo.draw.IFontMetrics;

import java.awt.*;

public class FontMetricsImpl implements IFontMetrics {

    private FontMetrics fontMetrics;

    public FontMetricsImpl(FontMetrics fontMetrics) {
        this.fontMetrics = fontMetrics;
    }

    @Override
    public int getBottom() {
        return fontMetrics.getMaxDescent();
    }

    @Override
    public int getTop() {
        return fontMetrics.getHeight();
    }

    @Override
    public int measure(String s) {
        return fontMetrics.stringWidth(s);
    }

    @Override
    public IFont getFont() {
        return new FontImpl(fontMetrics.getFont());
    }
}
