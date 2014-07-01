package wordcloud;

/**
 * Created by kenny on 6/29/14.
 */
public class WordFrequency {

    private final String word;

    private final int frequency;

    public WordFrequency(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }
}
