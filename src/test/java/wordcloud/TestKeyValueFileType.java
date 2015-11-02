package wordcloud;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import wordcloud.bg.RectangleBackground;
import wordcloud.font.CloudFont;
import wordcloud.font.FontWeight;
import wordcloud.font.scale.LinearFontScalar;
import wordcloud.nlp.FileType;
import wordcloud.nlp.FrequencyAnalyzer;

public class TestKeyValueFileType {

    @Test
    public void testKeyValueFileType() throws FileNotFoundException, IOException {
        final FrequencyAnalyzer frequencyAnalizer = new FrequencyAnalyzer();
        frequencyAnalizer.setMinWordLength(3);
        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/keyvaluefile.txt"),
                FileType.KEY_VALUE);
        final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackground(new RectangleBackground(600, 600));
        wordCloud.setCloudFont(new CloudFont("Impact", FontWeight.PLAIN));
        wordCloud.setFontScalar(new LinearFontScalar(18, 70));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/keyvalue.png");
    }

    private static InputStream getInputStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

}
