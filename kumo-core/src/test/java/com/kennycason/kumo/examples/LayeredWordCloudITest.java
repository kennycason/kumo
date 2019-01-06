package com.kennycason.kumo.examples;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.LayeredWordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.abst.DimensionAbst;
import com.kennycason.kumo.abst.FontAbst;
import com.kennycason.kumo.abst.InstanceCreator;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kenny on 7/5/14.
 */
public class LayeredWordCloudITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LayeredWordCloudITest.class);

    @Test
    public void layeredExample() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/new_york_positive.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/new_york_negative.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(600, 386);
        final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(2, dimension, CollisionMode.PIXEL_PERFECT);

        layeredWordCloud.setPadding(0, 1);
        layeredWordCloud.setPadding(1, 1);

        layeredWordCloud.setKumoFont(0, new KumoFont("LICENSE PLATE", FontAbst.Face.BOLD));
        layeredWordCloud.setKumoFont(1, new KumoFont("Comic Sans MS", FontAbst.Face.BOLD));

        layeredWordCloud.setBackground(0, new PixelBoundryBackground(getInputStream("backgrounds/cloud_bg.bmp")));
        layeredWordCloud.setBackground(1, new PixelBoundryBackground(getInputStream("backgrounds/cloud_fg.bmp")));

        layeredWordCloud.setColorPalette(0, new ColorPalette(InstanceCreator.color(0xABEDFF), InstanceCreator.color(0x82E4FF), InstanceCreator.color(0x55D6FA)));
        layeredWordCloud.setColorPalette(1, new ColorPalette(InstanceCreator.color(0xFFFFFF), InstanceCreator.color(0xDCDDDE), InstanceCreator.color(0xCCCCCC)));

        layeredWordCloud.setFontScalar(0, new SqrtFontScalar(10, 40));
        layeredWordCloud.setFontScalar(1, new SqrtFontScalar(10, 40));

        final long startTime = System.currentTimeMillis();
        layeredWordCloud.build(0, wordFrequencies);
        layeredWordCloud.build(1, wordFrequencies2);
        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
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
        final DimensionAbst dimension = InstanceCreator.dimension(600, 424);
        final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(2, dimension, CollisionMode.PIXEL_PERFECT);
        layeredWordCloud.setBackgroundColor(InstanceCreator.color(255, 255, 255));

        layeredWordCloud.setPadding(0, 1);
        layeredWordCloud.setPadding(1, 1);

        layeredWordCloud.setKumoFont(0, new KumoFont("LICENSE PLATE", FontAbst.Face.BOLD));
        layeredWordCloud.setKumoFont(1, new KumoFont("Comic Sans MS", FontAbst.Face.BOLD));

        layeredWordCloud.setBackground(0, new PixelBoundryBackground(getInputStream("backgrounds/haskell_1.bmp")));
        layeredWordCloud.setBackground(1, new PixelBoundryBackground(getInputStream("backgrounds/haskell_2.bmp")));

        layeredWordCloud.setColorPalette(0, new ColorPalette(InstanceCreator.color(0xFA6C07), InstanceCreator.color(0xFF7614), InstanceCreator.color(0xFF8936)));
        layeredWordCloud.setColorPalette(1, new ColorPalette(InstanceCreator.color(0x080706), InstanceCreator.color(0x3B3029), InstanceCreator.color(0x47362A)));

        layeredWordCloud.setFontScalar(0, new SqrtFontScalar(10, 40));
        layeredWordCloud.setFontScalar(1, new SqrtFontScalar(10, 40));

        final long startTime = System.currentTimeMillis();
        layeredWordCloud.build(0, wordFrequencies);
        layeredWordCloud.build(1, wordFrequencies2);
        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
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

        final DimensionAbst dimension = InstanceCreator.dimension(1000, 976);
        final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(4, dimension, CollisionMode.PIXEL_PERFECT);
        layeredWordCloud.setBackgroundColor(InstanceCreator.color(0x333333));

        layeredWordCloud.setPadding(0, 1);
        layeredWordCloud.setPadding(1, 1);
        layeredWordCloud.setPadding(2, 1);
        layeredWordCloud.setPadding(3, 1);

        layeredWordCloud.setKumoFont(0, new KumoFont("Comic Sans MS", FontAbst.Face.PLAIN));
        layeredWordCloud.setKumoFont(1, new KumoFont("Comic Sans MS", FontAbst.Face.BOLD));
        layeredWordCloud.setKumoFont(2, new KumoFont("Comic Sans MS", FontAbst.Face.ITALIC));
        layeredWordCloud.setKumoFont(3, new KumoFont("Comic Sans MS", FontAbst.Face.BOLD));

        layeredWordCloud.setBackground(0, new PixelBoundryBackground(getInputStream("backgrounds/pho_1.bmp")));
        layeredWordCloud.setBackground(1, new PixelBoundryBackground(getInputStream("backgrounds/pho_2.bmp")));
        layeredWordCloud.setBackground(2, new PixelBoundryBackground(getInputStream("backgrounds/pho_3.bmp")));
        layeredWordCloud.setBackground(3, new PixelBoundryBackground(getInputStream("backgrounds/pho_4.bmp")));

        layeredWordCloud.setColorPalette(0, new ColorPalette(InstanceCreator.color(0x26A621), InstanceCreator.color(0x21961D), InstanceCreator.color(0x187A15)));
        layeredWordCloud.setColorPalette(1, new ColorPalette(InstanceCreator.color(0x5963F0), InstanceCreator.color(0x515CF0), InstanceCreator.color(0x729FED)));
        layeredWordCloud.setColorPalette(2, new ColorPalette(InstanceCreator.color(0xEDC672), InstanceCreator.color(0xDBB258), InstanceCreator.color(0xDE7C1B)));
        layeredWordCloud.setColorPalette(3, new ColorPalette(InstanceCreator.color(0x70572B), InstanceCreator.color(0x857150), InstanceCreator.color(0xB09971)));

        layeredWordCloud.setFontScalar(0, new SqrtFontScalar(8, 30));
        layeredWordCloud.setFontScalar(1, new SqrtFontScalar(8, 40));
        layeredWordCloud.setFontScalar(2, new SqrtFontScalar(8, 30));
        layeredWordCloud.setFontScalar(3, new SqrtFontScalar(8, 30));

        final long startTime = System.currentTimeMillis();
        layeredWordCloud.build(0, wordFrequencies);
        layeredWordCloud.build(1, wordFrequencies2);
        layeredWordCloud.build(2, wordFrequencies3);
        layeredWordCloud.build(3, wordFrequencies4);

        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
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
