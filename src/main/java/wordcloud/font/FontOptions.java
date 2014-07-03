package wordcloud.font;

/**
 * Created by kenny on 7/3/14.
 */
public class FontOptions {

    private final String type;

    private final int weight;

    public FontOptions(String type, int weight) {
        this.type = type;
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public int getWeight() {
        return weight;
    }

}
