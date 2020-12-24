package com.kennycason.kumo.font.scale;

/**
 * Created by kenny on 6/30/14.
 */
public class SqrtFontScalar implements FontScalar {

    private final int minFont;
    private final int maxFont;

    public SqrtFontScalar(final int minFont, final int maxFont) {
        if (maxFont < minFont)
            throw new IllegalArgumentException("maxFont cannot be smaller than minFont");
        this.minFont = minFont;
        this.maxFont = maxFont;
    }

    @Override
    public float scale(final int value, final int maxValue) {
        final double leftSpan = Math.sqrt(maxValue);
        final double rightSpan = maxFont - minFont;

        // Convert the left range into a 0-1 range
        final double valueScaled = Math.sqrt(value) / leftSpan;

        // Convert the 0-1 range into a value in the right range.
        return (float) (minFont + (valueScaled * rightSpan));
    }

}
