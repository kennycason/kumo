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
import java.util.*;
import java.util.List;

public class IssueTest {

    //我们要有四个测试样例，分别对应没有文字，自动填充，文字过少，复制扩充
    //图像大小不合适，重新调整，小图调大，和大图调小，都要测试一次


    static final String INPUT_PATH ="backgrounds/whale.png";
    static final String DEFAULT_FONT = "Impact";
    static final String DEFAULT_IMAGE_TYPE = "png";
    @Test
    public void imageProcessorInstanceTest(){
        assert ImageProcessor.getInstance() != null;
    }

    @Test
    public void whaleImgLargeToSmallTest() throws IOException,InterruptedException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();

        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());
        final int width = 150;
        final int height = 100;
        final String textPath = "text/datarank.txt";
//        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream(textPath));
        final Dimension dimension = new Dimension(width, height);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackgroundColor(Color.WHITE);
        InputStream inputStream = null;
        try{
            inputStream = ImageProcessor.readImage(INPUT_PATH, width, height, DEFAULT_IMAGE_TYPE);

            wordCloud.setBackground(new PixelBoundryBackground(inputStream));

        }finally {
            inputStream.close();
        }

        wordCloud.setKumoFont(new KumoFont(DEFAULT_FONT, FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/a_whale_large_to_small.png");
        inputStream.close();
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
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"), false);
        final Dimension dimension = new Dimension(width, height);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackgroundColor(Color.WHITE);
        InputStream inputStream = null;
        try{
            inputStream = ImageProcessor.readImage(INPUT_PATH, width, height, DEFAULT_IMAGE_TYPE);

            wordCloud.setBackground(new PixelBoundryBackground(inputStream));

        }finally {
            inputStream.close();
        }

        wordCloud.setKumoFont(new KumoFont(DEFAULT_FONT, FontWeight.PLAIN));
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
        InputStream inputStream = null;
        try{
            inputStream = ImageProcessor.readImage(INPUT_PATH, width, height, DEFAULT_IMAGE_TYPE);

            wordCloud.setBackground(new PixelBoundryBackground(inputStream));

        }finally {
            inputStream.close();
        }
        wordCloud.setKumoFont(new KumoFont(DEFAULT_FONT, FontWeight.PLAIN));
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
        InputStream inputStream = null;
        try{
            inputStream = ImageProcessor.readImage(INPUT_PATH, width, height, DEFAULT_IMAGE_TYPE);

            wordCloud.setBackground(new PixelBoundryBackground(inputStream));

        }finally {
            inputStream.close();
        }
        wordCloud.setKumoFont(new KumoFont(DEFAULT_FONT, FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/a_whale_no_word_with_dararara.png");
    }

    @Test
    public void whaleImgNoWordAfterFilterWithDefaultAutoFillTest() throws IOException,InterruptedException {
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
        InputStream inputStream = null;
        try{
            inputStream = ImageProcessor.readImage(INPUT_PATH, width, height, DEFAULT_IMAGE_TYPE);

            wordCloud.setBackground(new PixelBoundryBackground(inputStream));

        }finally {
            inputStream.close();
        }
        wordCloud.setKumoFont(new KumoFont(DEFAULT_FONT, FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/a_whale_no_word_with_default_autofill.png");
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
        InputStream inputStream = null;
        try{
            inputStream = ImageProcessor.readImage(INPUT_PATH, width, height, DEFAULT_IMAGE_TYPE);

            wordCloud.setBackground(new PixelBoundryBackground(inputStream));

        }finally {
            inputStream.close();
        }
        wordCloud.setKumoFont(new KumoFont(DEFAULT_FONT, FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/a_whale_no_word.png");
    }

    @Test
    public void whaleImgWithListOfStringTest() throws IOException,InterruptedException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        frequencyAnalyzer.setStopWords(loadStopWords());
        int width = 1500;
        int height = 1000;
        List<String> texts = new ArrayList<>();
        texts.add("hello");
        texts.add("world");
//        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/datarank.txt"));
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(texts, true);
        final Dimension dimension = new Dimension(width, height);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(1);
        wordCloud.setBackgroundColor(Color.WHITE);
        InputStream inputStream = null;
        try{
            inputStream = ImageProcessor.readImage(INPUT_PATH, width, height, DEFAULT_IMAGE_TYPE);

            wordCloud.setBackground(new PixelBoundryBackground(inputStream));

        }finally {
            inputStream.close();
        }
        wordCloud.setKumoFont(new KumoFont(DEFAULT_FONT, FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output/a_whale_with_string_list.png");
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
