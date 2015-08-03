package wordcloud;

import org.apache.log4j.Logger;
import org.junit.Test;
import wordcloud.bg.CircleBackground;
import wordcloud.font.scale.LinearFontScalar;
import wordcloud.nlp.FrequencyAnalyzer;
import wordcloud.nlp.normalize.StringToHexNormalizer;
import wordcloud.nlp.normalize.UpperCaseNormalizer;
import wordcloud.nlp.normalize.UpsideDownNormalizer;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by kenny on 6/29/14.
 */
public class ITestWordCloudNormalizers {

    private static final Logger LOGGER = Logger.getLogger(ITestWordCloudNormalizers.class);

    @Test
    public void upperCaseNormalizer() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequencesToReturn(750);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.addNormalizer(new UpperCaseNormalizer());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new URL("http://kennycason.com"));

        final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_uppercase.png");
    }

    @Test
    public void upsideDownNormalizer() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequencesToReturn(750);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.addNormalizer(new UpsideDownNormalizer());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new URL("http://kennycason.com"));

        final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_upsidedown.png");
    }

    @Test
    public void hexStringNormalizer() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequencesToReturn(750);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.addNormalizer(new StringToHexNormalizer());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new URL("http://kennycason.com"));

        final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_hex.png");
    }

    @Test
    public void noNormalizationOrFilters() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequencesToReturn(750);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.clearFilters();
        frequencyAnalyzer.clearNormalizers();

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new URL("http://kennycason.com"));

        final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_normalization_or_filter.png");
    }

}
