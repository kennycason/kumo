package com.kennycason.kumo;

import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.placement.LinearWordPlacer;
import com.kennycason.kumo.placement.RTreeWordPlacer;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by kenny on 6/29/14.
 *
 * Diminishing returns on placement is primarily caused by WordCloud's method of determining to
 * re-place words in new locations. These results demonstrate the R-Tree is faster than linear brute-force.
 *
 100 frequencies (average)
 ===============
 linear 1090ms to build
 rtree 120ms to build

 500 frequencies (average)
 ===============
 linear 1315ms to build
 rtree 372ms to build

 800 frequencies (average)
 ==============
 linear 2169ms to build
 rtree 1626ms to build

 1000 frequencies (average)
 ==============
 linear 19596ms to build
 rtree 16798ms to build
 */
@Category(IntegrationTest.class)
public class WordPlacerPerfTest {
    private static final Logger LOGGER = Logger.getLogger(WordPlacerPerfTest.class);

    private static final int FREQUENCIES_TO_PLACE = 500;

    @Test
    public void linearPlacement() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(FREQUENCIES_TO_PLACE);

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final WordCloud wordCloud = new WordCloud(new Dimension(1000, 1000), CollisionMode.RECTANGLE);
        wordCloud.setWordPlacer(new LinearWordPlacer());

        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("linear " + (System.currentTimeMillis() - startTime) + "ms to build");

    }

    @Test
    public void rTreePlacement() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(FREQUENCIES_TO_PLACE);

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final WordCloud wordCloud = new WordCloud(new Dimension(1000, 1000), CollisionMode.RECTANGLE);
        wordCloud.setWordPlacer(new RTreeWordPlacer());

        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("rtree " + (System.currentTimeMillis() - startTime) + "ms to build");
    }

    private static InputStream getInputStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
