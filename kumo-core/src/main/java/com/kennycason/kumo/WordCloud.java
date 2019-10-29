package com.kennycason.kumo;

import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.collide.RectanglePixelCollidable;
import com.kennycason.kumo.collide.checkers.CollisionChecker;
import com.kennycason.kumo.collide.checkers.RectangleCollisionChecker;
import com.kennycason.kumo.collide.checkers.RectanglePixelCollisionChecker;
import com.kennycason.kumo.draw.*;
import com.kennycason.kumo.exception.KumoException;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.FontScalar;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.image.CollisionRaster;
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

    protected final Dimension dimension;
    protected final CollisionMode collisionMode;
    protected final CollisionChecker collisionChecker;
    protected final RectanglePixelCollidable backgroundCollidable;
    protected final CollisionRaster collisionRaster;
    protected final Image image;
    protected final Padder padder;
    protected final Set<Word> skipped = new HashSet<>();
    protected int padding;
    protected Background background;
    protected Color backgroundColor = new Color(0, 0, 0);
    protected FontScalar fontScalar = new LinearFontScalar(10, 40);
    protected KumoFont kumoFont = new KumoFont("Comic Sans MS", FontFace.BOLD);
    protected AngleGenerator angleGenerator = new AngleGenerator();
    protected RectangleWordPlacer wordPlacer = new RTreeWordPlacer();
    protected ColorPalette colorPalette = new ColorPalette(
            new Color(2,182,242), new Color(55,194,240), new Color(124,203,230), new Color(196,231,242), new Color(255,255,255));
    protected WordStartStrategy wordStartStrategy = new RandomWordStart();
    
    public WordCloud(final Dimension dimension, final CollisionMode collisionMode) {
        this.collisionMode = collisionMode;
        this.padder = derivePadder(collisionMode);
        this.collisionChecker = deriveCollisionChecker(collisionMode);
        this.collisionRaster = new CollisionRaster(dimension);
        this.image = new Image(dimension.getWidth(), dimension.getHeight());
        this.backgroundCollidable = new RectanglePixelCollidable(collisionRaster, new Point(0, 0));
        this.dimension = dimension;
        this.background = new RectangleBackground(dimension);
    }

    public void build(final List<WordFrequency> wordFrequencies) {
        Collections.sort(wordFrequencies);

        wordPlacer.reset();
        skipped.clear();

        // the background masks all none usable pixels and we can only check this raster
        background.mask(backgroundCollidable);

        int currentWord = 1;
        for (final Word word : buildWords(wordFrequencies, this.colorPalette)) {
            final Point point = wordStartStrategy.getStartingPoint(dimension, word);
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
            new ImageWriter().write(image, extension, new FileOutputStream(new File(outputFileName)));

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
            new ImageWriter().write(image, format, outputStream);
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

        final Image backgroundImage = new Image(dimension.getWidth(), dimension.getHeight());
        final Graphics graphics = new Graphics(backgroundImage);

        // draw current color
        graphics.drawRect(backgroundColor, 0, 0, dimension.getWidth(), dimension.getHeight());
        graphics.drawImg(image, 0, 0);

        // draw back to original
        final Graphics graphics2 = new Graphics(image);
        graphics2.drawImg(backgroundImage, 0, 0);
    }

    /**
     * compute the maximum radius for the placing spiral
     *
     * @param dimension the size of the backgound
     * @param start the center of the spiral
     * @return the maximum usefull radius
     */
    static int computeRadius(Dimension dimension, Point start) {
        int maxDistanceX = Math.max(start.getX(), dimension.getWidth() - start.getX()) + 1;
        int maxDistanceY = Math.max(start.getY(), dimension.getHeight() - start.getY()) + 1;

        // we use the pythagorean theorem to determinate the maximum radius
        return (int) Math.ceil(Math.sqrt(maxDistanceX * maxDistanceX + maxDistanceY * maxDistanceY));
    }

    /**
     * try to place in center, build out in a spiral trying to place words for N steps
     * @param word the word being placed
     * @param start the place to start trying to place the word
     */
    protected boolean place(final Word word, final Point start) {
        final Graphics graphics = new Graphics(image);

        final int maxRadius = computeRadius(dimension, start);
        final Point position = word.getPosition();

        for (int r = 0; r < maxRadius; r += 2) {
            for (int x = Math.max(-start.getX(), -r); x <= Math.min(r, dimension.getWidth() - start.getX() - 1); x++) {
                position.setX(start.getX() + x);

                final int offset = (int) Math.sqrt(r * r - x * x);

                // try positive root
                position.setY(start.getY() + offset);
                if (position.getY() >= 0 && position.getY() < dimension.getHeight() && canPlace(word)) {
                    collisionRaster.mask(word.getCollisionRaster(), position);
                    graphics.drawImg(word.getImage(), position.getX(), position.getY());
                    return true;
                }

                // try negative root (if offset != 0)
                position.setY(start.getY() - offset);
                if (offset != 0 && position.getY() >= 0 && position.getY() < dimension.getHeight() && canPlace(word)) {
                    collisionRaster.mask(word.getCollisionRaster(), position);
                    graphics.drawImg(word.getImage(), position.getX(), position.getY());
                    return true;
                }
            }
        }

        return false;
    }

    private boolean canPlace(final Word word) {

        Point position = word.getPosition();
        Dimension dimensionOfWord = word.getDimension();

        // are we inside the background?
        if (position.getY() < 0 || position.getY() + dimensionOfWord.getHeight() > dimension.getHeight()) {
            return false;
        } else if (position.getX() < 0 || position.getX() + dimensionOfWord.getWidth() > dimension.getWidth()) {
            return false;
        }

        switch (collisionMode) {
            case RECTANGLE:
                return !backgroundCollidable.collide(word) // is there a collision with the background shape?
                        && wordPlacer.place(word); // is there a collision with the existing words?
            case PIXEL_PERFECT:
                return !backgroundCollidable.collide(word); // is there a collision with the background shape?
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
        final Graphics graphics = new Graphics(image);

        final int frequency = wordFrequency.getFrequency();
        final float fontHeight = this.fontScalar.scale(frequency, 0, maxFrequency);
        final Font font = kumoFont.getFont();
        font.setSize(fontHeight);
        graphics.setFont(font);
        final FontMetrics fontMetrics = graphics.getFontMetrics();

        final double theta = angleGenerator.randomNext();

        final Word word = new Word(
                wordFrequency.getWord(), colorPalette.next(),
                fontMetrics, this.collisionChecker, theta
        );

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

    public void setBackgroundColor(final Color backgroundColor) {
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

    public Image getImage() {
        return image;
    }

    public Set<Word> getSkipped() {
        return skipped;
    }
    
    public void setWordStartStrategy(final WordStartStrategy wordStartStrategy) {
        this.wordStartStrategy = wordStartStrategy;
    }

    public void setWordPlacer(final RectangleWordPlacer wordPlacer) {
        this.wordPlacer = wordPlacer;
    }

}
