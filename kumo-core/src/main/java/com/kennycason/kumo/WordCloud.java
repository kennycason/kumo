package com.kennycason.kumo;

import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.collide.RectanglePixelCollidable;
import com.kennycason.kumo.collide.checkers.CollisionChecker;
import com.kennycason.kumo.collide.checkers.RectangleCollisionChecker;
import com.kennycason.kumo.collide.checkers.RectanglePixelCollisionChecker;
import com.kennycason.kumo.exception.KumoException;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.FontScalar;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.image.CollisionRaster;
import com.kennycason.kumo.abst.ImageRotatorAbst;
import com.kennycason.kumo.abst.*;
import com.kennycason.kumo.padding.Padder;
import com.kennycason.kumo.padding.RectanglePadder;
import com.kennycason.kumo.padding.WordPixelPadder;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.placement.RTreeWordPlacer;
import com.kennycason.kumo.placement.RectangleWordPlacer;
import com.kennycason.kumo.wordstart.RandomWordStart;
import com.kennycason.kumo.wordstart.WordStartStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by kenny on 6/29/14.
 */
public class WordCloud {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordCloud.class);

    protected final DimensionAbst dimension;
    protected final CollisionMode collisionMode;
    protected final CollisionChecker collisionChecker;
    protected final RectanglePixelCollidable backgroundCollidable;
    protected final CollisionRaster collisionRaster;
    protected final ImageAbst image;
    protected final Padder padder;
    protected final Set<Word> skipped = new HashSet<>();
    protected int padding;
    protected Background background;
    protected ColorAbst backgroundColor = InstanceCreator.color(0, 0, 0);
    protected FontScalar fontScalar = new LinearFontScalar(10, 40);
    protected KumoFont kumoFont = new KumoFont("Comic Sans MS", FontAbst.Face.BOLD);
    protected AngleGenerator angleGenerator = new AngleGenerator();
    protected RectangleWordPlacer wordPlacer = new RTreeWordPlacer();
    protected ColorPalette colorPalette = new ColorPalette(InstanceCreator.color(246, 162, 108), InstanceCreator.color(76, 38, 153), InstanceCreator.color(733, 69, 12), InstanceCreator.color(65, 234, 222), InstanceCreator.color(255, 255, 255));
    protected WordStartStrategy wordStartStrategy = new RandomWordStart();
    
    public WordCloud(final DimensionAbst dimension, final CollisionMode collisionMode) {
        this.collisionMode = collisionMode;
        this.padder = derivePadder(collisionMode);
        this.collisionChecker = deriveCollisionChecker(collisionMode);
        this.collisionRaster = new CollisionRaster(dimension);
        this.image = InstanceCreator.image(dimension.getWidth(), dimension.getHeight());
        this.backgroundCollidable = new RectanglePixelCollidable(collisionRaster, InstanceCreator.point(0, 0));
        this.dimension = dimension;
        this.background = new RectangleBackground(dimension);
    }

    public void build(final List<WordFrequency> wordFrequencies) {
        Collections.sort(wordFrequencies);

        wordPlacer.reset();
        skipped.clear();

        int currentWord = 1;
        for (final Word word : buildWords(wordFrequencies, this.colorPalette)) {
            final PointAbst point = wordStartStrategy.getStartingPoint(dimension, word);
            final boolean placed = place(word, point);

            if (placed) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("placed: {} ({}/{})", word.getWord(), currentWord, wordFrequencies.size());
                }
            } else {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("skipped: {} ({}/{})", word.getWord(), currentWord, wordFrequencies.size());
                }
                skipped.add(word);
            }
            currentWord++;
        }
        drawForegroundToBackground();
    }

    public void writeToFile(final String outputFileName) {
        String extension = "";
        final int i = outputFileName.lastIndexOf('.');
        if (i > 0) {
            extension = outputFileName.substring(i + 1);
        }
        try {
            LOGGER.info("Saving WordCloud to: {}", outputFileName);
            InstanceCreator.imageWriter().write(image, extension, new FileOutputStream(new File(outputFileName)));

        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Write to output stream as PNG
     *
     * @param outputStream the output stream to write the image data to
     */
    public void writeToStreamAsPNG(final OutputStream outputStream) {
        writeToStream("png", outputStream);
    }

    /**
     * Write wordcloud image data to stream in the given format
     *
     * @param format       the image format
     * @param outputStream the output stream to write image data to
     */
    public void writeToStream(final String format, final OutputStream outputStream) {
        try {
            LOGGER.debug("Writing WordCloud image data to output stream");
            InstanceCreator.imageWriter().write(image, format, outputStream);
            LOGGER.debug("Done writing WordCloud image data to output stream");

        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new KumoException("Could not write wordcloud to outputstream due to an IOException", e);
        }
    }

    /**
     * create background, then draw current word cloud on top of it.
     * Doing it this way preserves the transparency of the this.image's pixels
     * for a more flexible pixel perfect collision
     */
    protected void drawForegroundToBackground() {
        if (backgroundColor == null) { return; }

        final ImageAbst backgroundBufferedImage = InstanceCreator.image(dimension.getWidth(), dimension.getHeight());
        final GraphicsAbst graphics = InstanceCreator.graphics(backgroundBufferedImage);

        // draw current color
        graphics.drawRect(backgroundColor, 0, 0, dimension.getWidth(), dimension.getHeight());
        graphics.drawImg(image, 0, 0);

        // draw back to original
        final GraphicsAbst graphics2 = InstanceCreator.graphics(image);
        graphics2.drawImg(backgroundBufferedImage, 0, 0);
    }

    /**
     * try to place in center, build out in a spiral trying to place words for N steps
     * @param word the word being placed
     * @param start the place to start trying to place the word
     */
    protected boolean place(final Word word, final PointAbst start) {
        final GraphicsAbst graphics = InstanceCreator.graphics(image);

        final int maxRadius = dimension.getWidth();

        for (int r = 0; r < maxRadius; r += 2) {
            for (int x = -r; x <= r; x++) {
                if (start.getX() + x < 0) { continue; }
                if (start.getX() + x >= maxRadius) { continue; }

                boolean placed = false;
                word.getPosition().setX(start.getX() + x);

                // try positive root
                final int y1 = (int) Math.sqrt(r * r - x * x);
                if (start.getY() + y1 >= 0 && start.getY() + y1 < dimension.getHeight()) {
                    word.getPosition().setY(start.getY() + y1);
                    placed = canPlace(word);
                }
                // try negative root
                final int y2 = -y1;
                if (!placed && start.getY() + y2 >= 0 && start.getY() + y2 < dimension.getHeight()) {
                    word.getPosition().setY(start.getY() + y2);
                    placed = canPlace(word);
                }
                if (placed) {
                    collisionRaster.mask(word.getCollisionRaster(), word.getPosition());
                    graphics.drawImg(word.getBufferedImage(), word.getPosition().getX(), word.getPosition().getY());
                    return true;
                }

            }
        }

        return false;
    }

    private boolean canPlace(final Word word) {
        if (!background.isInBounds(word)) { return false; }

        switch (collisionMode) {
            case RECTANGLE:
                return wordPlacer.place(word);
            case PIXEL_PERFECT:
                return !backgroundCollidable.collide(word);
        }
        return false;
    }

    protected List<Word> buildWords(final List<WordFrequency> wordFrequencies, final ColorPalette colorPalette) {
        final int maxFrequency = maxFrequency(wordFrequencies);

        final List<Word> words = new ArrayList<>();
        for (final WordFrequency wordFrequency : wordFrequencies) {
            // the text shouldn't be empty, however, in case of bad normalizers/tokenizers, this may happen
            if (!wordFrequency.getWord().isEmpty()) {
                words.add(buildWord(wordFrequency, maxFrequency, colorPalette));
            }
        }
        return words;
    }

    private Word buildWord(final WordFrequency wordFrequency, final int maxFrequency, final ColorPalette colorPalette) {
        final GraphicsAbst graphics = InstanceCreator.graphics(image);
        final int frequency = wordFrequency.getFrequency();
        final float fontHeight = this.fontScalar.scale(frequency, 0, maxFrequency);
        final FontAbst font = kumoFont.getFont().withSize(fontHeight);
        graphics.setFont(font);
        final FontMetricsAbst fontMetrics = graphics.getFontMetrics();
        final Word word = new Word(wordFrequency.getWord(), colorPalette.next(), fontMetrics, this.collisionChecker);
        final double theta = angleGenerator.randomNext();
        if (theta != 0.0) {
            word.setImage(InstanceCreator.imageRotator().rotate(word.getBufferedImage(), theta));
        }
        if (padding > 0) {
            padder.pad(word, padding);
        }
        return word;
    }

    private static int maxFrequency(final List<WordFrequency> wordFrequencies) {
        if (wordFrequencies.isEmpty()) { return 1; }

        return wordFrequencies.get(0).getFrequency();
    }

    private static Padder derivePadder(final CollisionMode collisionMode) {
        switch (collisionMode) {
            case PIXEL_PERFECT:
                return new WordPixelPadder();
            case RECTANGLE:
                return new RectanglePadder();
        }
        throw new IllegalArgumentException("CollisionMode can not be null");
    }

    private static CollisionChecker deriveCollisionChecker(final CollisionMode collisionMode) {
        switch (collisionMode) {
            case PIXEL_PERFECT:
                return new RectanglePixelCollisionChecker();
            case RECTANGLE:
                return new RectangleCollisionChecker();
        }
        throw new IllegalArgumentException("CollisionMode can not be null");
    }

    public void setBackgroundColor(final ColorAbst backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setPadding(final int padding) {
        this.padding = padding;
    }

    public void setColorPalette(final ColorPalette colorPalette) {
        this.colorPalette = colorPalette;
    }

    public void setBackground(final Background background) {
        this.background = background;
    }

    public void setFontScalar(final FontScalar fontScalar) {
        this.fontScalar = fontScalar;
    }

    public void setKumoFont(final KumoFont kumoFont) {
        this.kumoFont = kumoFont;
    }

    public void setAngleGenerator(final AngleGenerator angleGenerator) {
        this.angleGenerator = angleGenerator;
    }

    public ImageAbst getImage() {
        return image;
    }

    public Set<Word> getSkipped() {
        return skipped;
    }
    
    public void setWordStartStrategy(final WordStartStrategy startscheme) {
        this.wordStartStrategy = startscheme;
    }

    public void setWordPlacer(final RectangleWordPlacer wordPlacer) {
        this.wordPlacer = wordPlacer;
    }

}
