package com.kennycason.kumo.font.scale;

/**
 * Created by kenny on 6/30/14.
 */
public class LinearFontScalar implements FontScalar {

    private final int minFont;
    private final int maxFont;

    public LinearFontScalar(final int minFont, final int maxFont) {
        this.minFont = minFont;
        this.maxFont = maxFont;
    }

    @Override
    public float scale(final int value, final int minValue, final int maxValue) {
        final float leftSpan = maxValue - minValue;
        final float rightSpan = maxFont - minFont;

        // Convert the left range into a 0-1 range
        final float valueScaled = (value - minValue) / leftSpan;

        // Convert the 0-1 range into a value in the right range.
        return minFont + (valueScaled * rightSpan);
    }
}
