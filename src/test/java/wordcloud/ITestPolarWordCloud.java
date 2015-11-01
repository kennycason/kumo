package wordcloud;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import wordcloud.bg.CircleBackground;
import wordcloud.bg.PixelBoundryBackground;
import wordcloud.bg.RectangleBackground;
import wordcloud.font.CloudFont;
import wordcloud.font.FontWeight;
import wordcloud.font.scale.LinearFontScalar;
import wordcloud.font.scale.SqrtFontScalar;
import wordcloud.nlp.FrequencyAnalyzer;
import wordcloud.nlp.tokenizer.ChineseWordTokenizer;
import wordcloud.palette.ColorPalette;

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
public class ITestPolarWordCloud {

    private static final Logger LOGGER = Logger.getLogger(ITestWordCloud.class);

    @Test
    public void whaleImgLargePolarTest() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequencesToReturn(600);
        frequencyAnalyzer.setMinWordLength(4);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/new_york_positive.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/new_york_negative.txt"));

        final PolarWordCloud wordCloud = new PolarWordCloud(990, 618, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackgroundColor(Color.WHITE);
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/whale.png")));
        wordCloud.setFontScalar(new LinearFontScalar(15, 50));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies, wordFrequencies2);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/polar_newyork_whale_large_blur.png");
    }

    @Test
    public void newyorkPolarCircle() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequencesToReturn(750);
        frequencyAnalyzer.setMinWordLength(4);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/new_york_positive.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/new_york_negative.txt"));

        final PolarWordCloud wordCloud = new PolarWordCloud(600, 600, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies, wordFrequencies2);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/polar_newyork_circle_blur_sqrt_font.png");
    }

    @Test
    public void newyorkPolarRectangle() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequencesToReturn(750);
        frequencyAnalyzer.setMinWordLength(4);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/new_york_positive.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/new_york_negative.txt"));

        final PolarWordCloud wordCloud = new PolarWordCloud(800, 600, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new RectangleBackground(800, 600));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies, wordFrequencies2);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/polar_newyork_rectangle_blur.png");
    }

    @Test
    public void chineseVsEnglishTideComments() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequencesToReturn(750);
        frequencyAnalyzer.setMinWordLength(3);
        frequencyAnalyzer.setStopWords(loadStopWords());
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/english_tide.txt"));

        final FrequencyAnalyzer chineseFrequencyAnalyzer = new FrequencyAnalyzer();
        chineseFrequencyAnalyzer.setWordFrequencesToReturn(750);
        chineseFrequencyAnalyzer.setMinWordLength(2);
        chineseFrequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        final List<WordFrequency> wordFrequencies2 = chineseFrequencyAnalyzer.load(getInputStream("text/chinese_tide.txt"));

        final PolarWordCloud wordCloud = new PolarWordCloud(800, 600, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new RectangleBackground(800, 600));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 70));

        final ColorPalette colorPalette = new ColorPalette(new Color(0xD5CFFA), new Color(0xBBB1FA), new Color(0x9A8CF5), new Color(0x806EF5));
        final ColorPalette colorPalette2 = new ColorPalette(new Color(0xFA8E8E), new Color(0xF77979), new Color(0xF55F5F), new Color(0xF24949));
        wordCloud.setColorPalette(colorPalette);
        wordCloud.setColorPalette2(colorPalette2);

        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies, wordFrequencies2);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/polar_tide_chinese_vs_english2.png");
    }

    @Test
    public void tidyCatLitter() throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequencesToReturn(400);
        frequencyAnalyzer.setMinWordLength(4);
        frequencyAnalyzer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/tidy_cat_litter_positive.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalyzer.load(getInputStream("text/tidy_cat_litter_negative.txt"));

        final PolarWordCloud wordCloud = new PolarWordCloud(600, 600, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
        wordCloud.setPadding(2);
        wordCloud.setCloudFont(new CloudFont("Cairo", FontWeight.BOLD));
        wordCloud.setBackground(new PixelBoundryBackground(getInputStream("backgrounds/cat.bmp")));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        final long startTime = System.currentTimeMillis();
        wordCloud.build(wordFrequencies, wordFrequencies2);
        LOGGER.info("Took " + (System.currentTimeMillis() - startTime) + "ms to build");
        wordCloud.writeToFile("output/tidy_cat_litter_cat_shape3.png");

        // horrible times, 400 words, total 800 words
        // pixel perfect
        // loading from png 1335661ms
        // loading from bmp 359172ms
        // rectangle
        // loading from bmp 464401ms

        // after optimization one
        // pixel perfect
        // loading from bmp 35213ms
        // now, 18110ms
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
