package com.kennycason.kumo.awtimpl;

import com.kennycason.kumo.abst.FontAbst;
import com.kennycason.kumo.abst.FontMetricsAbst;

import java.awt.*;

public class FontMetricsImpl extends FontMetricsAbst {

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
    public FontAbst getFont() {
        return new FontImpl(fontMetrics.getFont());
    }
}
