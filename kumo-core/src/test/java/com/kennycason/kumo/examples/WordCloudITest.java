package com.kennycason.kumo.examples;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.abst.ColorAbst;
import com.kennycason.kumo.abst.DimensionAbst;
import com.kennycason.kumo.abst.FontAbst;
import com.kennycason.kumo.abst.InstanceCreator;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by kenny on 6/29/14.
 */
public class WordCloudITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordCloudITest.class);

    private static final Random RANDOM = new Random();

    @Test
    public void simpleCircleTest() throws IOException {
        final List<WordFrequency> wordFrequencies = buildWordFrequencies().subList(0, 150);
        final DimensionAbst dimension = InstanceCreator.dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(buildRandomColorPalette(20));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/wordcloud_circle.png");
    }

    @Test
    public void simpleRectangleTest() throws IOException {
        final List<WordFrequency> wordFrequencies = buildWordFrequencies().subList(0, 150);
        final DimensionAbst dimension = InstanceCreator.dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new RectangleBackground(dimension));
        wordCloud.setColorPalette(buildRandomColorPalette(20));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/wordcloud_rectangle2.png");
    }

    @Test
    public void loadWikipediaFromUrl() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(1000);
        frequencyAnalyzer.setMinWordLength(1);

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new URL("http://en.wikipedia.org/wiki/Main_Page"));
        final DimensionAbst dimension = InstanceCreator.dimension(1000, 1000);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackground(new RectangleBackground(dimension));
        wordCloud.setKumoFont(new KumoFont("Impact", FontAbst.Face.PLAIN));
       // wordCloud.setAngleGenerator(new AngleGenerator(-60, 60, 5));
        wordCloud.setColorPalette(buildRandomColorPalette(5));
        wordCloud.setFontScalar(new LinearFontScalar(18, 70));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/wikipedia.png");
    }

    @Test
    public void readCNN() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(500);
        frequencyAnalyzer.setMinWordLength(3);

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new URL("http://www.cnn.com/"));
        final DimensionAbst dimension = InstanceCreator.dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackground(new RectangleBackground(dimension));
        wordCloud.setKumoFont(new KumoFont("Impact", FontAbst.Face.PLAIN));
        // wordCloud.setAngleGenerator(new AngleGenerator(-60, 60, 5));
        wordCloud.setColorPalette(buildRandomColorPalette(5));
        wordCloud.setFontScalar(new LinearFontScalar(18, 70));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/cnn.png");
    }

    @Test
    public void whaleImgLargeTest() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(990, 618);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackgroundColor(InstanceCreator.color(255, 255, 255));
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale.png")));
        wordCloud.setKumoFont(new KumoFont("Impact", FontAbst.Face.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(InstanceCreator.color(0x4055F1), InstanceCreator.color(0x408DF1), InstanceCreator.color(0x40AAF1), InstanceCreator.color(0x40C5F1), InstanceCreator.color(0x40D3F1), InstanceCreator.color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/whale_wordcloud_large_impact.png");
    }

    @Test
    public void whaleImgSmallTest() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(500, 312);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale_small.png")));
        wordCloud.setColorPalette(new ColorPalette(InstanceCreator.color(0x4055F1), InstanceCreator.color(0x408DF1), InstanceCreator.color(0x40AAF1), InstanceCreator.color(0x40C5F1), InstanceCreator.color(0x40D3F1), InstanceCreator.color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/whale_wordcloud_small.png");
    }

    @Test
    public void whaleImgSmallAnglesTest() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(100);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(500, 312);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setAngleGenerator(new AngleGenerator(-90, 90, 10));
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale_small.png")));
        wordCloud.setColorPalette(new ColorPalette(InstanceCreator.color(0x4055F1), InstanceCreator.color(0x408DF1), InstanceCreator.color(0x40AAF1), InstanceCreator.color(0x40C5F1), InstanceCreator.color(0x40D3F1), InstanceCreator.color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(10, 30));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/whale_wordcloud_small_angles4.png");
    }

    @Test
    public void whaleImgLargeAnglesTest() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(990, 618);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setAngleGenerator(new AngleGenerator(-90, 90, 10));
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale.png")));
        wordCloud.setColorPalette(new ColorPalette(InstanceCreator.color(0x4055F1), InstanceCreator.color(0x408DF1), InstanceCreator.color(0x40AAF1), InstanceCreator.color(0x40C5F1), InstanceCreator.color(0x40D3F1), InstanceCreator.color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(20, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/whale_wordcloud_large_angles2.png");
    }

    @Test
    public void datarankCircle() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(750);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(new ColorPalette(InstanceCreator.color(0x4055F1), InstanceCreator.color(0x408DF1), InstanceCreator.color(0x40AAF1), InstanceCreator.color(0x40C5F1), InstanceCreator.color(0x40D3F1), InstanceCreator.color(0xFFFFFF)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
        wordCloud.writeToFile("output/datarank_wordcloud_circle_sqrt_font.png");
    }

    @Test
    public void datarankCircleLarge() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(750);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(1000, 1000);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(500));
        wordCloud.setColorPalette(new ColorPalette(InstanceCreator.color(0x4055F1), InstanceCreator.color(0x408DF1), InstanceCreator.color(0x40AAF1), InstanceCreator.color(0x40C5F1), InstanceCreator.color(0x40D3F1), InstanceCreator.color(0xFFFFFF)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
        wordCloud.writeToFile("output/datarank_wordcloud_circle_large2.png");
    }

    @Test
    public void datarankEarthImage() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setKumoFont(new KumoFont("Simple Slumg__G", FontAbst.Face.BOLD));
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/earth.bmp")));
        wordCloud.setColorPalette(buildRandomColorPalette(3));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
        wordCloud.writeToFile("output/earth_image_word_cloud_different_fonts.png");
    }

    @Test
    public void catImage() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/tidy_cat_litter_top.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setKumoFont(new KumoFont("Marker Felt", FontAbst.Face.PLAIN));
        //wordCloud.setAngleGenerator(new AngleGenerator(0));
        wordCloud.setBackgroundColor(InstanceCreator.color(0, 0, 0));
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/cat.bmp")));
        wordCloud.setColorPalette(new ColorPalette(InstanceCreator.color(0xcccccc), InstanceCreator.color(0xdddddd), InstanceCreator.color(0xffffff)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took {}ms to build", System.currentTimeMillis() - startTime);
        wordCloud.writeToFile("output/tidy_cat_litter_black_cat.png");
    }

    @Ignore
    @Test
    public void datarankCode() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new FileInputStream("/tmp/code.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(990, 618);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackgroundColor(InstanceCreator.color(255, 255, 255));
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale.png")));
        wordCloud.setColorPalette(buildRandomColorPalette(3));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("/tmp/datarank_code.png");
    }

    @Ignore
    @Test
    public void largeCircleTest() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(5000);
        frequencyAnalyzer.setMinWordLength(2);

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new FileInputStream("/tmp/code.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackground(new CircleBackground(500));
        wordCloud.setColorPalette(buildRandomColorPalette(20));
        wordCloud.setFontScalar(new LinearFontScalar(10, 100));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/wordcloud_large_code_circle.png");
    }

    @Ignore
    @Test
    public void matchOnlineExample() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new FileInputStream("/tmp/code.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setBackgroundColor(InstanceCreator.color(255, 255, 255));
        wordCloud.setColorPalette(buildRandomColorPalette(2));
        wordCloud.setKumoFont(new KumoFont("Helvitica", FontAbst.Face.PLAIN));
        wordCloud.setFontScalar(new LinearFontScalar(8, 130));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/wordcloud_match_online_example.png");
    }

    @Test
    public void anotherRectangleTest() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(250);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/sample.txt"));
        final DimensionAbst dimension = InstanceCreator.dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new RectangleBackground(dimension));
        wordCloud.setColorPalette(buildRandomColorPalette(4));
        wordCloud.setFontScalar(new LinearFontScalar(30, 60));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/random_sample.png");
    }

    private static ColorPalette buildRandomColorPalette(final int n) {
        final ColorAbst[] colors = new ColorAbst[n];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = InstanceCreator.color(RANDOM.nextInt(230) + 25, RANDOM.nextInt(230) + 25, RANDOM.nextInt(230) + 25);
        }
        return new ColorPalette(colors);
    }

    private static List<WordFrequency> buildWordFrequencies() throws IOException {
        final List<String> pokemonNames = getPokemonNames();
        final List<WordFrequency> wordFrequencies = new ArrayList<>();
        for (final String pokemon : pokemonNames) {
            wordFrequencies.add(new WordFrequency(pokemon, RANDOM.nextInt(100) + 1));
        }
        return wordFrequencies;
    }

    private static List<String> getPokemonNames() throws IOException {
        return IOUtils.readLines(getInputStream("text/pokemon.txt"));
    }

    private static Set<String> loadStopWords() {
        try {
            final List<String> lines = IOUtils.readLines(getInputStream("text/stop_words.txt"));
            return new HashSet<>(lines);

        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Collections.emptySet();
    }

    private static InputStream getInputStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

}
