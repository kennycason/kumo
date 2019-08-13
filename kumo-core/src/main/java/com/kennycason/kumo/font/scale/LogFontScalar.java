package com.kennycason.kumo.font.scale;

/**
 * Created by kenny on 6/30/14.
 */
public class LogFontScalar implements FontScalar {

    private final int minFont;
    private final int maxFont;

    public LogFontScalar(final int minFont, final int maxFont) {
        this.minFont = minFont;
        this.maxFont = maxFont;
    }

    @Override
    public float scale(final int value, final int minValue, final int maxValue) {
        final double leftSpan = Math.log(maxValue) - Math.log(minValue);
        final double rightSpan = maxFont - minFont;

        // Convert the left range into a 0-1 range
        final double valueScaled = (Math.log(value) - Math.log(minValue)) / leftSpan;

        // Convert the 0-1 range into a value in the right range.
        return (float) (minFont + (valueScaled * rightSpan));
    }

}
