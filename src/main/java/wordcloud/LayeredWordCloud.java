package wordcloud;

import org.apache.log4j.Logger;
import wordcloud.bg.Background;
import wordcloud.font.FontOptions;
import wordcloud.font.scale.FontScalar;
import wordcloud.image.AngleGenerator;
import wordcloud.palette.ColorPalette;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenny on 7/5/14.
 */
public class LayeredWordCloud {

    private static final Logger LOGGER = Logger.getLogger(LayeredWordCloud.class);

    private final int layers;

    private final int width;

    private final int height;

    private final List<WordCloud> wordClouds = new ArrayList<>();

    private Color backgroundColor = Color.BLACK;

    public LayeredWordCloud(int layers, int width, int height, CollisionMode collisionMode) {
        this.layers = layers;
        this.width = width;
        this.height = height;
        for(int i = 0; i < layers; i++) {
            final WordCloud wordCloud = new WordCloud(width, height, collisionMode);
            wordCloud.setBackgroundColor(null);
            wordClouds.add(wordCloud);
        }
    }

    public void build(int layer, List<WordFrequency> wordFrequencies) {
        wordClouds.get(layer).build(wordFrequencies);
    }

    public void setPadding(int layer, int padding) {
        this.wordClouds.get(layer).setPadding(padding);
    }

    public void setColorPalette(int layer, ColorPalette colorPalette) {
        this.wordClouds.get(layer).setColorPalette(colorPalette);
    }

    public void setBackground(int layer, Background background) {
        this.wordClouds.get(layer).setBackground(background);
    }

    public void setFontScalar(int layer, FontScalar fontScalar) {
        this.wordClouds.get(layer).setFontScalar(fontScalar);
    }

    public void setFontOptions(int layer, FontOptions fontOptions) {
        this.wordClouds.get(layer).setFontOptions(fontOptions);
    }

    public void setAngleGenerator(int layer, AngleGenerator angleGenerator) {
        this.wordClouds.get(layer).setAngleGenerator(angleGenerator);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public BufferedImage getBufferedImage() {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);
        for(WordCloud wordCloud : wordClouds) {
            graphics.drawImage(wordCloud.getBufferedImage(), 0, 0, null);
        }

        return bufferedImage;
    }

    public void writeToFile(final String outputFileName) {
        String extension = "";
        int i = outputFileName.lastIndexOf('.');
        if (i > 0) {
            extension = outputFileName.substring(i + 1);
        }
        try {
            LOGGER.info("Saving Layered WordCloud to " + outputFileName);
            ImageIO.write(getBufferedImage(), extension, new File(outputFileName));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
