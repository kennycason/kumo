package com.kennycason.kumo;

import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.FontWeight;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kenny on 7/5/14.
 */
@Category(IntegrationTest.class)
public class LayeredWordCloudITest {

    private static final Logger LOGGER = Logger.getLogger(LayeredWordCloudITest.class);

    @Test
    public void layeredExample() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/new_york_positive.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/new_york_negative.txt"));
        final Dimension dimension = new Dimension(600, 386);
        final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(2, dimension, CollisionMode.PIXEL_PERFECT);

        layeredWordCloud.setPadding(0, 1);
        layeredWordCloud.setPadding(1, 1);

        layeredWordCloud.setFontOptions(0, new KumoFont("LICENSE PLATE", FontWeight.BOLD));
        layeredWordCloud.setFontOptions(1, new KumoFont("Comic Sans MS", FontWeight.BOLD));

        layeredWordCloud.setBackground(0, new PixelBoundryBackground(getInputStream("backgrounds/cloud_bg.bmp")));
        layeredWordCloud.setBackground(1, new PixelBoundryBackground(getInputStream("backgrounds/cloud_fg.bmp")));

        layeredWordCloud.setColorPalette(0, new ColorPalette(new Color(0xABEDFF), new Color(0x82E4FF), new Color(0x55D6FA)));
        layeredWordCloud.setColorPalette(1, new ColorPalette(new Color(0xFFFFFF), new Color(0xDCDDDE), new Color(0xCCCCCC)));

        layeredWordCloud.setFontScalar(0, new SqrtFontScalar(10, 40));
        layeredWordCloud.setFontScalar(1, new SqrtFontScalar(10, 40));

        final long startTime = System.currentTimeMillis();
        layeredWordCloud.build(0, wordFrequencies);
        layeredWordCloud.build(1, wordFrequencies2);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        layeredWordCloud.writeToFile("output/layered_word_cloud.png");
    }

    @Test
    public void layeredHaskellExample() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/haskell_hate.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/haskell_love.txt"));
        final Dimension dimension = new Dimension(600, 424);
        final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(2, dimension, CollisionMode.PIXEL_PERFECT);
        layeredWordCloud.setBackgroundColor(Color.WHITE);

        layeredWordCloud.setPadding(0, 1);
        layeredWordCloud.setPadding(1, 1);

        layeredWordCloud.setFontOptions(0, new KumoFont("LICENSE PLATE", FontWeight.BOLD));
        layeredWordCloud.setFontOptions(1, new KumoFont("Comic Sans MS", FontWeight.BOLD));

        layeredWordCloud.setBackground(0, new PixelBoundryBackground(getInputStream("backgrounds/haskell_1.bmp")));
        layeredWordCloud.setBackground(1, new PixelBoundryBackground(getInputStream("backgrounds/haskell_2.bmp")));

        layeredWordCloud.setColorPalette(0, new ColorPalette(new Color(0xFA6C07), new Color(0xFF7614), new Color(0xFF8936)));
        layeredWordCloud.setColorPalette(1, new ColorPalette(new Color(0x080706), new Color(0x3B3029), new Color(0x47362A)));

        layeredWordCloud.setFontScalar(0, new SqrtFontScalar(10, 40));
        layeredWordCloud.setFontScalar(1, new SqrtFontScalar(10, 40));

        final long startTime = System.currentTimeMillis();
        layeredWordCloud.build(0, wordFrequencies);
        layeredWordCloud.build(1, wordFrequencies2);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        layeredWordCloud.writeToFile("output/layered_haskell.png");
    }


    @Test
    public void layeredPhoBowl() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(1000);
        frequencyAnalyzer.setMinWordLength(3);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/pho_history.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/pho_history_viet.txt"));
        final List<WordFrequency> wordFrequencies3 = frequencyAnalyzer.load(getInputStream("text/pho_recipee.txt"));
        final List<WordFrequency> wordFrequencies4 = frequencyAnalyzer.load(getInputStream("text/pho_chopsticks.txt"));

        final Dimension dimension = new Dimension(1000, 976);
        final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(4, dimension, CollisionMode.PIXEL_PERFECT);
        layeredWordCloud.setBackgroundColor(new Color(0x333333));

        layeredWordCloud.setPadding(0, 1);
        layeredWordCloud.setPadding(1, 1);
        layeredWordCloud.setPadding(2, 1);
        layeredWordCloud.setPadding(3, 1);

        layeredWordCloud.setFontOptions(0, new KumoFont("Comic Sans MS", FontWeight.PLAIN));
        layeredWordCloud.setFontOptions(1, new KumoFont("Comic Sans MS", FontWeight.BOLD));
        layeredWordCloud.setFontOptions(2, new KumoFont("Comic Sans MS", FontWeight.ITALIC));
        layeredWordCloud.setFontOptions(3, new KumoFont("Comic Sans MS", FontWeight.BOLD));

        layeredWordCloud.setBackground(0, new PixelBoundryBackground(getInputStream("backgrounds/pho_1.bmp")));
        layeredWordCloud.setBackground(1, new PixelBoundryBackground(getInputStream("backgrounds/pho_2.bmp")));
        layeredWordCloud.setBackground(2, new PixelBoundryBackground(getInputStream("backgrounds/pho_3.bmp")));
        layeredWordCloud.setBackground(3, new PixelBoundryBackground(getInputStream("backgrounds/pho_4.bmp")));

        layeredWordCloud.setColorPalette(0, new ColorPalette(new Color(0x26A621), new Color(0x21961D), new Color(0x187A15)));
        layeredWordCloud.setColorPalette(1, new ColorPalette(new Color(0x5963F0), new Color(0x515CF0), new Color(0x729FED)));
        layeredWordCloud.setColorPalette(2, new ColorPalette(new Color(0xEDC672), new Color(0xDBB258), new Color(0xDE7C1B)));
        layeredWordCloud.setColorPalette(3, new ColorPalette(new Color(0x70572B), new Color(0x857150), new Color(0xB09971)));

        layeredWordCloud.setFontScalar(0, new SqrtFontScalar(8, 30));
        layeredWordCloud.setFontScalar(1, new SqrtFontScalar(8, 40));
        layeredWordCloud.setFontScalar(2, new SqrtFontScalar(8, 30));
        layeredWordCloud.setFontScalar(3, new SqrtFontScalar(8, 30));

        final long startTime = System.currentTimeMillis();
        layeredWordCloud.build(0, wordFrequencies);
        layeredWordCloud.build(1, wordFrequencies2);
        layeredWordCloud.build(2, wordFrequencies3);
        layeredWordCloud.build(3, wordFrequencies4);

        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        layeredWordCloud.writeToFile("output/layered_pho_bowl.png");
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

    private static InputStream getInputStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

}
