import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import wordcloud.CollisionMode;
import wordcloud.PolarBlendMode;
import wordcloud.PolarWordCloud;
import wordcloud.WordFrequency;
import wordcloud.bg.CircleBackground;
import wordcloud.bg.PixelBoundryBackground;
import wordcloud.bg.RectangleBackground;
import wordcloud.font.LinearFontScalar;
import wordcloud.nlp.FrequencyAnalizer;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kenny on 6/29/14.
 */
public class TestPolarWordCloud {

    private static final Logger LOGGER = Logger.getLogger(TestWordCloud.class);

    @Test
    public void whaleImgLargePolarTest() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(600);
        frequencyAnalizer.setMinWordLength(4);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/new_york_positive.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalizer.load(getInputStream("text/new_york_negative.txt"));

        final PolarWordCloud wordCloud = new PolarWordCloud(990, 618, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackgroundColor(Color.WHITE);
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale.png")));
        wordCloud.setFontScalar(new LinearFontScalar(10, 50));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies, wordFrequencies2);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/polar_newyork_whale_large_blur.png");
    }

    @Test
    public void newyorkPolarCircle() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(750);
        frequencyAnalizer.setMinWordLength(4);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/new_york_positive.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalizer.load(getInputStream("text/new_york_negative.txt"));

        final PolarWordCloud wordCloud = new PolarWordCloud(600, 600, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies, wordFrequencies2);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/polar_newyork_circle_blur.png");
    }

    @Test
    public void newyorkPolarRectangle() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(750);
        frequencyAnalizer.setMinWordLength(4);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/new_york_positive.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalizer.load(getInputStream("text/new_york_negative.txt"));

        final PolarWordCloud wordCloud = new PolarWordCloud(800, 600, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new RectangleBackground(800, 600));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies, wordFrequencies2);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/polar_newyork_rectangle_blur.png");
    }

    private static Set<String> loadStopWords() {
        try {
            final List<String> lines = IOUtils.readLines(getInputStream("text/stop_words.txt"));
            return new HashSet<>(lines);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Collections.emptySet();
    }

    private static InputStream getInputStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

}
