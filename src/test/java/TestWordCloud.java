import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import wordcloud.CollisionMode;
import wordcloud.WordCloud;
import wordcloud.WordFrequency;
import wordcloud.bg.CircleBackground;
import wordcloud.bg.PixelBoundryBackground;
import wordcloud.bg.RectangleBackground;
import wordcloud.font.FontOptions;
import wordcloud.font.scale.LinearFontScalar;
import wordcloud.font.scale.SqrtFontScalar;
import wordcloud.image.AngleGenerator;
import wordcloud.nlp.FrequencyAnalizer;
import wordcloud.nlp.tokenizer.ChineseWordTokenizer;
import wordcloud.palette.ColorPalette;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by kenny on 6/29/14.
 */
public class TestWordCloud {

    private static final Logger LOGGER = Logger.getLogger(TestWordCloud.class);

    private static final Random RANDOM = new Random();

    @Test
    public void simpleCircleTest() throws IOException {
        final List<WordFrequency> wordFrequencies = buildWordFrequences().subList(0, 150);

        final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(buildRandomColorPallete(20));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/wordcloud_circle.png");
    }

    @Test
    public void simpleRectangleTest() throws IOException {
        final List<WordFrequency> wordFrequencies = buildWordFrequences().subList(0, 150);

        final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new RectangleBackground(600, 600));
        wordCloud.setColorPalette(buildRandomColorPallete(20));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/wordcloud_rectangle.png");
    }

    @Test
    public void whaleImgLargeTest() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(600);
        frequencyAnalizer.setMinWordLength(5);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/datarank.txt"));
        final WordCloud wordCloud = new WordCloud(990, 618, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackgroundColor(Color.WHITE);
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale.png")));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
//        wordCloud.setThetas(new double[] { 0 });
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/whale_wordcloud_large3.png");
    }

    @Test
    public void whaleImgSmallTest() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(300);
        frequencyAnalizer.setMinWordLength(5);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/datarank.txt"));
        final WordCloud wordCloud = new WordCloud(500, 312, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale_small.png")));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/whale_wordcloud_small.png");
    }

    @Test
    public void whaleImgSmallAnglesTest() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(100);
        frequencyAnalizer.setMinWordLength(5);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/datarank.txt"));
        final WordCloud wordCloud = new WordCloud(500, 312, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setAngleGenerator(new AngleGenerator(-90, 90, 10));
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale_small.png")));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(10, 30));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/whale_wordcloud_small_angles3.png");
    }

    @Test
    public void whaleImgLargeAnglesTest() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(600);
        frequencyAnalizer.setMinWordLength(5);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/datarank.txt"));
        final WordCloud wordCloud = new WordCloud(990, 618, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setAngleGenerator(new AngleGenerator(-90, 90, 10));
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale.png")));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(20, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/whale_wordcloud_large_angles2.png");
    }

    @Test
    public void datarankCircle() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(750);
        frequencyAnalizer.setMinWordLength(5);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/datarank.txt"));
        final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/datarank_wordcloud_circle_sqrt_font.png");
    }

    @Test
    public void datarankCircleLarge() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(750);
        frequencyAnalizer.setMinWordLength(5);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/datarank.txt"));
        final WordCloud wordCloud = new WordCloud(1000, 1000, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(500));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/datarank_wordcloud_circle_large2.png");
    }

    @Test
    public void chineseCircle() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(600);
        frequencyAnalizer.setMinWordLength(2);
        frequencyAnalizer.setWordTokenizer(new ChineseWordTokenizer());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/chinese_language.txt"));
        final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(new ColorPalette(new Color(0xD5CFFA), new Color(0xBBB1FA), new Color(0x9A8CF5), new Color(0x806EF5)));
        wordCloud.setFontScalar(new SqrtFontScalar(12, 45));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/chinese_language_circle.png");
    }

    @Test
    public void datarankEarthImage() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(300);
        frequencyAnalizer.setMinWordLength(5);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/datarank.txt"));
        final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setFontOptions(new FontOptions("Simple Slumg__G", Font.BOLD));
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/earth.bmp")));
        wordCloud.setColorPalette(buildRandomColorPallete(3));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/earth_image_word_cloud_different_fonts.png");
    }

    @Test
    public void catImage() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(300);
        frequencyAnalizer.setMinWordLength(5);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/tidy_cat_litter_top.txt"));
        final WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setFontOptions(new FontOptions("LICENSE PLATE", Font.BOLD));
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/cat.bmp")));
        wordCloud.setColorPalette(buildRandomColorPallete(3));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/tidy_cat_litter.png");
    }

    private static ColorPalette buildRandomColorPallete(int n) {
        final Color[] colors = new Color[n];
        for(int i = 0; i < colors.length; i++) {
            colors[i] = new Color(RANDOM.nextInt(230) + 25, RANDOM.nextInt(230) + 25, RANDOM.nextInt(230) + 25);
        }
        return new ColorPalette(colors);
    }

    private static List<WordFrequency> buildWordFrequences() throws IOException {
        final List<String> pokemonNames = getPokemonNames();
        final List<WordFrequency> wordFrequencies = new ArrayList<>();
        for(String pokemon : pokemonNames) {
            wordFrequencies.add(new WordFrequency(pokemon, RANDOM.nextInt(100) + 1));
        }
        return wordFrequencies;
    }

    private static List<String> getPokemonNames() throws IOException {
        return IOUtils.readLines(getInputStream("pokemon.txt"));
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
