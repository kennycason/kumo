package wordcloud.font.scale;

/**
 * Created by kenny on 6/30/14.
 */
public class SqrtFontScalar implements FontScalar {

    private final int minFont;
    private final int maxFont;

    public SqrtFontScalar(int minFont, int maxFont) {
        this.minFont = minFont;
        this.maxFont = maxFont;
    }

    @Override
    public int scale(int value, int minValue, int maxValue) {
        int leftSpan = (int)(Math.sqrt(maxValue) - Math.sqrt(minValue));
        int rightSpan = maxFont - minFont;

        // Convert the left range into a 0-1 range
        double valueScaled = (Math.sqrt(value) - Math.sqrt(minValue)) / (double)leftSpan;

        // Convert the 0-1 range into a value in the right range.
        return (int)(minFont + (valueScaled * rightSpan));
    }
}
