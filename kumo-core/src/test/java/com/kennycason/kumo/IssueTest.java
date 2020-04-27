package com.kennycason.kumo;

import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.examples.WordCloudITest;
import com.kennycason.kumo.font.FontWeight;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IssueTest {

    //我们要有四个测试样例，分别对应没有文字，自动填充，文字过少，复制扩充
    //图像大小不合适，重新调整，小图调大，和大图调小，都要测试一次



    @Test
    public void whaleImgLargeToSmallTest() throws IOException,InterruptedException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());
        int width = 150;
        int height = 100;

//        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final Dimension dimension = new Dimension(width, height);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackgroundColor(Color.WHITE);
        String input_path ="backgrounds/whale.png";
        InputStream is = ImageProcessor.readImage(input_path, width, height, "png");

        wordCloud.setBackground(new PixelBoundryBackground(is));
        wordCloud.setKumoFont(new KumoFont("Impact", FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/a_whale_large_to_small.png");
    }
    @Test
    public void whaleImgSmallToLargeTest() throws IOException,InterruptedException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());
        int width = 1500;
        int height = 1000;

//        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final Dimension dimension = new Dimension(width, height);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackgroundColor(Color.WHITE);
        String input_path ="backgrounds/whale.png";
        InputStream is = ImageProcessor.readImage(input_path, width, height, "png");

        wordCloud.setBackground(new PixelBoundryBackground(is));
        wordCloud.setKumoFont(new KumoFont("Impact", FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/a_whale_small_to_large.png");
    }

    @Test
    public void whaleImgWordTooFewTest() throws IOException,InterruptedException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());
        int width = 1500;
        int height = 1000;

//        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/hello_world.txt"), true);
        final Dimension dimension = new Dimension(width, height);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackgroundColor(Color.WHITE);
        String input_path ="backgrounds/whale.png";
        InputStream is = ImageProcessor.readImage(input_path, width, height, "png");

        wordCloud.setBackground(new PixelBoundryBackground(is));
        wordCloud.setKumoFont(new KumoFont("Impact", FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/a_whale_word_too_few.png");
    }

    @Test
    public void whaleImgNoWordAfterFilterWithCustomAutoFillTest() throws IOException,InterruptedException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());
        int width = 1500;
        int height = 1000;

//        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/empty.txt"), true, "Dararara");
        final Dimension dimension = new Dimension(width, height);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackgroundColor(Color.WHITE);
        String input_path ="backgrounds/whale.png";
        InputStream is = ImageProcessor.readImage(input_path, width, height, "png");

        wordCloud.setBackground(new PixelBoundryBackground(is));
        wordCloud.setKumoFont(new KumoFont("Impact", FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/a_whale_no_word_with_dararara.png");
    }
    @Test
    public void whaleImgNoWordAfterFilterTest() throws IOException,InterruptedException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());
        int width = 1500;
        int height = 1000;

//        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/empty.txt"), true);
        final Dimension dimension = new Dimension(width, height);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackgroundColor(Color.WHITE);
        String input_path ="backgrounds/whale.png";
        InputStream is = ImageProcessor.readImage(input_path, width, height, "png");

        wordCloud.setBackground(new PixelBoundryBackground(is));
        wordCloud.setKumoFont(new KumoFont("Impact", FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/a_whale_no_word.png");
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
    private static final Logger LOGGER = LoggerFactory.getLogger(WordCloudITest.class);
    private static InputStream getInputStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
