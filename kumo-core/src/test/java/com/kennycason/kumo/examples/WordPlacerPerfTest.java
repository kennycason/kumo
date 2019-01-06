package com.kennycason.kumo.examples;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.abst.InstanceCreator;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.placement.LinearWordPlacer;
import com.kennycason.kumo.placement.RTreeWordPlacer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class WordPlacerPerfTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordPlacerPerfTest.class);

    private static final int FREQUENCIES_TO_PLACE = 500;

    @Test
    public void linearPlacement() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(FREQUENCIES_TO_PLACE);

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final WordCloud wordCloud = new WordCloud(InstanceCreator.dimension(1000, 1000), CollisionMode.RECTANGLE);
        wordCloud.setWordPlacer(new LinearWordPlacer());

        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("linear: {}ms to build", System.currentTimeMillis() - startTime);

    }

    @Test
    public void rTreePlacement() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(FREQUENCIES_TO_PLACE);

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final WordCloud wordCloud = new WordCloud(InstanceCreator.dimension(1000, 1000), CollisionMode.RECTANGLE);
        wordCloud.setWordPlacer(new RTreeWordPlacer());

        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("rtree: {}ms to build", System.currentTimeMillis() - startTime);
    }

    private static InputStream getInputStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

}
