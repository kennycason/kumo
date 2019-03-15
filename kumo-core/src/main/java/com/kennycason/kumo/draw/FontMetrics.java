package com.kennycason.kumo.draw;

public class FontMetrics implements IFontMetrics{

    private IFontMetrics platformSpecificImplementation;

    public FontMetrics(IFontMetrics platformSpecificImplementation) {
        this.platformSpecificImplementation = platformSpecificImplementation;
    }

    @Override
    public int getBottom() {
        return platformSpecificImplementation.getBottom();
    }

    @Override
    public int getTop() {
        return platformSpecificImplementation.getTop();
    }

    @Override
    public int measure(String s) {
        return platformSpecificImplementation.measure(s);
    }

    @Override
    public IFont getFont() {
        return platformSpecificImplementation.getFont();
    }
}
