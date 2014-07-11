package wordcloud.font.scale;

/**
 * Created by kenny on 6/30/14.
 */
public class LinearFontScalar implements FontScalar {

    private final int minFont;
    private final int maxFont;

    public LinearFontScalar(int minFont, int maxFont) {
        this.minFont = minFont;
        this.maxFont = maxFont;
    }

    @Override
    public float scale(int value, int minValue, int maxValue) {
        float leftSpan = maxValue - minValue;
        float rightSpan = maxFont - minFont;

        // Convert the left range into a 0-1 range
        float valueScaled = (value - minValue) / leftSpan;

        // Convert the 0-1 range into a value in the right range.
        return (minFont + (valueScaled * rightSpan));
    }
}
