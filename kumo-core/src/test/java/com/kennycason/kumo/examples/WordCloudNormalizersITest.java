package com.kennycason.kumo.examples;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.draw.Dimension;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.normalize.BubbleTextNormalizer;
import com.kennycason.kumo.nlp.normalize.StringToHexNormalizer;
import com.kennycason.kumo.nlp.normalize.UpperCaseNormalizer;
import com.kennycason.kumo.nlp.normalize.UpsideDownNormalizer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by kenny on 6/29/14.
 */
public class WordCloudNormalizersITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordCloudNormalizersITest.class);

    @Test
    public void upperCaseNormalizer() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(750);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.addNormalizer(new UpperCaseNormalizer());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new URL("http://kennycason.com"));
        final Dimension dimension = new Dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_uppercase.png");
    }

    @Test
    public void upsideDownNormalizer() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(750);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.addNormalizer(new UpsideDownNormalizer());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new URL("http://kennycason.com"));
        final Dimension dimension = new Dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_upsidedown.png");
    }

    @Test
    public void hexStringNormalizer() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(750);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.addNormalizer(new StringToHexNormalizer());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new URL("http://kennycason.com"));
        final Dimension dimension = new Dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_hex.png");
    }

    @Test
    public void noNormalizationOrFilters() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(750);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.clearFilters();
        frequencyAnalyzer.clearNormalizers();

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new URL("http://kennycason.com"));
        final Dimension dimension = new Dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
        wordCloud.writeToFile("output/kennycason_com_wordcloud_circle_normalization_or_filter.png");
    }

    @Test
    public void bubbleText() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(750);
        frequencyAnalyzer.addNormalizer(new BubbleTextNormalizer());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new URL("http://kennycason.com"));
        final Dimension dimension = new Dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
        wordCloud.writeToFile("output/bubbletext.png");
    }

}
