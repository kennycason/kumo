import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import wordcloud.CollisionMode;
import wordcloud.LayeredWordCloud;
import wordcloud.WordFrequency;
import wordcloud.bg.PixelBoundryBackground;
import wordcloud.font.FontOptions;
import wordcloud.font.scale.SqrtFontScalar;
import wordcloud.nlp.FrequencyAnalizer;
import wordcloud.palette.ColorPalette;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by kenny on 7/5/14.
 */
public class TestLayeredWordCloud {

    private static final Logger LOGGER = Logger.getLogger(TestLayeredWordCloud.class);

    private static final Random RANDOM = new Random();

    @Test
    public void layeredExample() throws IOException {
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(300);
        frequencyAnalizer.setMinWordLength(5);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/new_york_positive.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalizer.load(getInputStream("text/new_york_negative.txt"));

        final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(2, 600, 386, CollisionMode.PIXEL_PERFECT);

        layeredWordCloud.setPadding(0, 1);
        layeredWordCloud.setPadding(1, 1);

        layeredWordCloud.setFontOptions(0, new FontOptions("LICENSE PLATE", Font.BOLD));
        layeredWordCloud.setFontOptions(1, new FontOptions("Comic Sans MS", Font.BOLD));

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
        final FrequencyAnalizer frequencyAnalizer = new FrequencyAnalizer();
        frequencyAnalizer.setWordFrequencesToReturn(300);
        frequencyAnalizer.setMinWordLength(5);
        frequencyAnalizer.setStopWords(loadStopWords());

        final List<WordFrequency> wordFrequencies = frequencyAnalizer.load(getInputStream("text/haskell_hate.txt"));
        final List<WordFrequency> wordFrequencies2 = frequencyAnalizer.load(getInputStream("text/haskell_love.txt"));

        final LayeredWordCloud layeredWordCloud = new LayeredWordCloud(2, 600, 424, CollisionMode.PIXEL_PERFECT);
        layeredWordCloud.setBackgroundColor(Color.WHITE);

        layeredWordCloud.setPadding(0, 1);
        layeredWordCloud.setPadding(1, 1);

        layeredWordCloud.setFontOptions(0, new FontOptions("LICENSE PLATE", Font.BOLD));
        layeredWordCloud.setFontOptions(1, new FontOptions("Comic Sans MS", Font.BOLD));

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
