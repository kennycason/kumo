package com.kennycason.kumo;

import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.collide.RectanglePixelCollidable;
import com.kennycason.kumo.collide.checkers.CollisionChecker;
import com.kennycason.kumo.collide.checkers.RectangleCollisionChecker;
import com.kennycason.kumo.collide.checkers.RectanglePixelCollisionChecker;
import com.kennycason.kumo.exception.KumoException;
import com.kennycason.kumo.font.FontWeight;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

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
    protected final BufferedImage bufferedImage;
    protected final Padder padder;
    protected final Set<Word> skipped = new HashSet<>();
    protected int padding;
    protected Background background;
    protected Color backgroundColor = Color.BLACK;
    protected FontScalar fontScalar = new LinearFontScalar(10, 40);
    protected KumoFont kumoFont = new KumoFont("Comic Sans MS", FontWeight.BOLD);
    protected AngleGenerator angleGenerator = new AngleGenerator();
    protected RectangleWordPlacer wordPlacer = new RTreeWordPlacer();
    protected ColorPalette colorPalette = new ColorPalette(0x02B6F2, 0x37C2F0, 0x7CCBE6, 0xC4E7F2, 0xFFFFFF);
    protected WordStartStrategy wordStartStrategy = new RandomWordStart();
    
    public WordCloud(final Dimension dimension, final CollisionMode collisionMode) {
        this.collisionMode = collisionMode;
        this.padder = derivePadder(collisionMode);
        this.collisionChecker = deriveCollisionChecker(collisionMode);
        this.collisionRaster = new CollisionRaster(dimension);
        this.bufferedImage = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
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
            ImageIO.write(bufferedImage, extension, new File(outputFileName));

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
            ImageIO.write(bufferedImage, format, outputStream);
            LOGGER.debug("Done writing WordCloud image data to output stream");

        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new KumoException("Could not write wordcloud to outputstream due to an IOException", e);
        }
    }

    /**
     * create background, then draw current word cloud on top of it.
     * Doing it this way preserves the transparency of the this.bufferedImage's pixels
     * for a more flexible pixel perfect collision
     */
    protected void drawForegroundToBackground() {
        if (backgroundColor == null) { return; }

        final BufferedImage backgroundBufferedImage = new BufferedImage(dimension.width, dimension.height, this.bufferedImage.getType());
        final Graphics graphics = backgroundBufferedImage.getGraphics();

        // draw current color
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, dimension.width, dimension.height);
        graphics.drawImage(bufferedImage, 0, 0, null);

        // draw back to original
        final Graphics graphics2 = bufferedImage.getGraphics();
        graphics2.drawImage(backgroundBufferedImage, 0, 0, null);
    }

    /**
     * compute the maximum radius for the placing spiral
     *
     * @param dimension the size of the backgound
     * @param start the center of the spiral
     * @return the maximum usefull radius
     */
    static int computeRadius(final Dimension dimension, final Point start) {
        final int maxDistanceX = Math.max(start.x, dimension.width - start.x) + 1;
        final int maxDistanceY = Math.max(start.y, dimension.height - start.y) + 1;
        
        // we use the pythagorean theorem to determinate the maximum radius
        return (int) Math.ceil(Math.sqrt(maxDistanceX * maxDistanceX + maxDistanceY * maxDistanceY));
    }
    
    /**
     * try to place in center, build out in a spiral trying to place words for N steps
     * @param word the word being placed
     * @param start the place to start trying to place the word
     */
    protected boolean place(final Word word, final Point start) {
        final Graphics graphics = this.bufferedImage.getGraphics();

        final int maxRadius = computeRadius(dimension, start);
        final Point position = word.getPosition();
        
        for (int r = 0; r < maxRadius; r += 2) {
            for (int x = Math.max(-start.x, -r); x <= Math.min(r, dimension.width - start.x - 1); x++) {
                position.x = start.x + x;

                final int offset = (int) Math.sqrt(r * r - x * x);
                
                // try positive root
                position.y = start.y + offset;
                if (position.y >= 0 && position.y < dimension.height && canPlace(word)) {
                    collisionRaster.mask(word.getCollisionRaster(), position);
                    graphics.drawImage(word.getBufferedImage(), position.x, position.y, null);
                    return true;
                }
                
                // try negative root (if offset != 0)
                position.y = start.y - offset;
                if (offset != 0 && position.y >= 0 && position.y < dimension.height && canPlace(word)) {
                    collisionRaster.mask(word.getCollisionRaster(), position);
                    graphics.drawImage(word.getBufferedImage(), position.x, position.y, null);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean canPlace(final Word word) {
        final Point position = word.getPosition();
        final Dimension dimensionOfWord = word.getDimension();
        
        // are we inside the background?
        if (position.y < 0 || position.y + dimensionOfWord.height > dimension.height) {
            return false;
        } else if (position.x < 0 || position.x + dimensionOfWord.width > dimension.width) {
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
        final Graphics2D graphics = this.bufferedImage.createGraphics();
        
        // set the rendering hint here to ensure the font metrics are correct
        graphics.setRenderingHints(Word.getRenderingHints());
        
        final int frequency = wordFrequency.getFrequency();
        final float fontHeight = this.fontScalar.scale(frequency, 0, maxFrequency);
        final Font font = (wordFrequency.hasFont() ? wordFrequency.getFont() : kumoFont).getFont().deriveFont(fontHeight);
        final FontMetrics fontMetrics = graphics.getFontMetrics(font);
        
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

    public BufferedImage getBufferedImage() {
        return bufferedImage;
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
