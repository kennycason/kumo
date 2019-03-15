package com.kennycason.kumo.examples;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.LayeredWordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.draw.Color;
import com.kennycason.kumo.draw.Dimension;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.draw.FontFace;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kenny on 2/24/16.
 */
public class DataRankWordCloudITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataRankWordCloudITest.class);

    @Test
    public void datarank() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(200);
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));

        final Dimension dimension = new Dimension(990, 618);
        final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(4, dimension, CollisionMode.PIXEL_PERFECT);
        layeredWordCloud.setBackgroundColor(new Color(0x000000FF, true));

        layeredWordCloud.setPadding(0, 1);
        layeredWordCloud.setPadding(1, 1);

        layeredWordCloud.setKumoFont(0, new KumoFont("Comic Sans MS", FontFace.BOLD));
        layeredWordCloud.setKumoFont(1, new KumoFont("Comic Sans MS", FontFace.BOLD));

        layeredWordCloud.setBackground(0, new PixelBoundryBackground(getInputStream("backgrounds/datarank-1.png")));
        layeredWordCloud.setBackground(1, new PixelBoundryBackground(getInputStream("backgrounds/datarank-2.png")));

        layeredWordCloud.setColorPalette(0, new ColorPalette(new Color(0x0891d1)));
        layeredWordCloud.setColorPalette(1, new ColorPalette(new Color(0x76beea)));

        layeredWordCloud.setFontScalar(0, new SqrtFontScalar(10, 60));
        layeredWordCloud.setFontScalar(1, new SqrtFontScalar(10, 60));

        final long startTime = System.currentTimeMillis();
        layeredWordCloud.build(0, wordFrequencies);
        layeredWordCloud.build(1, wordFrequencies);

        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
        layeredWordCloud.writeToFile("output/datarank.png");
    }

    private static Set<String> loadStopWords() throws IOException {
        return new HashSet<>(IOUtils.readLines(getInputStream("text/stop_words.txt")));
    }

    private static InputStream getInputStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

}
