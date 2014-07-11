package wordcloud.font;

/**
 * Created by kenny on 7/3/14.
 */
public class CloudFont {

    private final String type;

    private final FontWeight weight;

    public CloudFont(String type, FontWeight weight) {
        this.type = type;
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public int getWeight() {
        return weight.getWeight();
    }

}
