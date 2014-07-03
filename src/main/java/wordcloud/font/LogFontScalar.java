package wordcloud.font;

/**
 * Created by kenny on 6/30/14.
 */
public class LogFontScalar implements FontScalar {

    private final int minFont;
    private final int maxFont;

    public LogFontScalar(int minFont, int maxFont) {
        this.minFont = minFont;
        this.maxFont = maxFont;
    }

    @Override
    public int scale(int value, int minValue, int maxValue) {
        double scaledFont = (minValue / (double) maxValue) * (maxFont - minFont) + minFont;
        return (int) scaledFont;
    }

}
