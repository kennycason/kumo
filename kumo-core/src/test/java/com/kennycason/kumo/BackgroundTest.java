package com.kennycason.kumo;

import com.kennycason.kumo.font.FontWeight;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.*;
import com.kennycason.kumo.palette.*;
import com.kennycason.kumo.bg.PixelBoundaryBackground;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BackgroundTest {
    private static final String DEFAULT_FONT = "Impact";
    private static InputStream getInputStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    @Test
    public void opaqueBackgroundNotTransparentized() throws IOException{
        InputStream inputStream = null;
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/empty.txt"),true, "Dararara");
        final Dimension dimension = new Dimension(1500, 1000);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setBackgroundColor(Color.WHITE);
        wordCloud.setPadding(2);
        try {
            inputStream = ImageProcessor.readImage("backgrounds/whale_not_transparent.png", 1500, 1000, "png");
            wordCloud.setBackground(new PixelBoundaryBackground(inputStream));
        } finally {
            inputStream.close();
        }
        wordCloud.setKumoFont(new KumoFont(DEFAULT_FONT, FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output_test/whale_not_transparentized.png");
    }

    @Test
    public void opaqueBackgroundTransparentized() throws IOException{
        InputStream inputStream = null;
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/empty.txt"),true, "Dararara");
        final Dimension dimension = new Dimension(1500, 1000);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setBackgroundColor(Color.WHITE);
        wordCloud.setPadding(2);
        try {
            inputStream = ImageProcessor.readImage("backgrounds/whale_not_transparent.png", 1500, 1000, "png");
            wordCloud.setBackground(new PixelBoundaryBackground(inputStream,5));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
        wordCloud.setKumoFont(new KumoFont(DEFAULT_FONT, FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output_test/whale_transparentized.png");
    }
    @Test
    public void transparentBackground() throws IOException{
        InputStream inputStream = null;
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(5);
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("text/empty.txt"),true, "Dararara");
        final Dimension dimension = new Dimension(1500, 1000);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setBackgroundColor(Color.WHITE);
        wordCloud.setPadding(2);
        try {
            inputStream = ImageProcessor.readImage("backgrounds/whale.png", 1500, 1000, "png");
            wordCloud.setBackground(new PixelBoundaryBackground(inputStream));
        } finally {
            inputStream.close();
        }
        wordCloud.setKumoFont(new KumoFont(DEFAULT_FONT, FontWeight.PLAIN));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x000000)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("output_test/transparent_whale.png");
    }
}

