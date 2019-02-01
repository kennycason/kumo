package com.kennycason.kumo;

import com.kennycason.kumo.abst.*;
import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.exception.KumoException;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.FontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.placement.RectangleWordPlacer;
import com.kennycason.kumo.wordstart.WordStartStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by kenny on 7/5/14.
 */
public class LayeredWordCloud {
    private static final Logger LOGGER = LoggerFactory.getLogger(LayeredWordCloud.class);

    private final DimensionAbst dimension;

    private final List<WordCloud> wordClouds = new ArrayList<>();

    private ColorAbst backgroundColor = ColorAbst.get(0, 0, 0);

    public LayeredWordCloud(final int layers, final DimensionAbst dimension, final CollisionMode collisionMode) {
        this.dimension = dimension;

        for (int i = 0; i < layers; i++) {
            final WordCloud wordCloud = new WordCloud(dimension, collisionMode);
            wordCloud.setBackgroundColor(null);
            wordClouds.add(wordCloud);
        }
    }

    public void build(final int layer, final List<WordFrequency> wordFrequencies) {
        wordClouds.get(layer).build(wordFrequencies);
    }

    public void setPadding(final int layer, final int padding) {
        this.wordClouds.get(layer).setPadding(padding);
    }

    public void setColorPalette(final int layer, final ColorPalette colorPalette) {
        this.wordClouds.get(layer).setColorPalette(colorPalette);
    }

    public void setBackground(final int layer, final Background background) {
        this.wordClouds.get(layer).setBackground(background);
    }

    public void setFontScalar(final int layer, final FontScalar fontScalar) {
        this.wordClouds.get(layer).setFontScalar(fontScalar);
    }

    public void setKumoFont(final int layer, final KumoFont kumoFont) {
        this.wordClouds.get(layer).setKumoFont(kumoFont);
    }

    public void setAngleGenerator(final int layer, final AngleGenerator angleGenerator) {
        this.wordClouds.get(layer).setAngleGenerator(angleGenerator);
    }

    public void setWordPlacer(final int layer, final RectangleWordPlacer wordPlacer) {
        this.wordClouds.get(layer).setWordPlacer(wordPlacer);
    }

    public void setBackgroundColor(final ColorAbst backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public ImageAbst getImage() {
        final ImageAbst image = ImageAbst.get(dimension.getWidth(), dimension.getHeight());
        final GraphicsAbst graphics = GraphicsAbst.get(image);
        graphics.drawRect(backgroundColor, 0, 0, dimension.getWidth(), dimension.getHeight());

        for (final WordCloud wordCloud : wordClouds) {
            graphics.drawImg(wordCloud.getImage(), 0, 0);
        }

        return image;
    }
    
    public WordCloud getLayer(final int layer) {
        return wordClouds.get(layer);
    }
    
    public WordCloud getAt(final int layer) {
        return getLayer(layer);
    }

    public Set<Word> getSkipped(final int layer) {
        return wordClouds.get(layer).getSkipped();
    }

    public void writeToFile(final String outputFileName) {
        String extension = "";
        final int i = outputFileName.lastIndexOf('.');
        if (i > 0) {
            extension = outputFileName.substring(i + 1);
        }
        try {
            LOGGER.info("Saving Layered WordCloud to: {}", outputFileName);
            ImageWriterAbst.get().write(getImage(), extension, new FileOutputStream(new File(outputFileName)));

        } catch (final IOException e) {
            throw new KumoException(e);
        }
    }
    
    public void setWordStartStrategy(final int layer, final WordStartStrategy scheme) {
        wordClouds.get(layer).setWordStartStrategy(scheme);
    }
    
    public int getLayers() {
        return wordClouds.size();
    }

}
