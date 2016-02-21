package com.kennycason.kumo;

import ch.lambdaj.Lambda;
import com.kennycason.kumo.collide.checkers.CollisionChecker;
import com.kennycason.kumo.collide.checkers.RectangleCollisionChecker;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.FontScalar;
import com.kennycason.kumo.image.ImageRotation;
import com.kennycason.kumo.wsc.RandomWordStart;
import com.kennycason.kumo.wsc.WordStartScheme;
import org.apache.log4j.Logger;
import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.collide.RectanglePixelCollidable;
import com.kennycason.kumo.collide.checkers.RectanglePixelCollisionChecker;
import com.kennycason.kumo.font.FontWeight;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.image.CollisionRaster;
import com.kennycason.kumo.padding.Padder;
import com.kennycason.kumo.padding.RectanglePadder;
import com.kennycason.kumo.padding.WordPixelPadder;
import com.kennycason.kumo.palette.ColorPalette;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.lambdaj.Lambda.on;

/**
 * Created by kenny on 6/29/14.
 */
public class WordCloud {

    private static final Logger LOGGER = Logger.getLogger(WordCloud.class);

    protected final int width;

    protected final int height;

    protected final CollisionMode collisionMode;

    protected final CollisionChecker collisionChecker;

    protected final Padder padder;

    protected int padding;

    protected Background background;

    protected final RectanglePixelCollidable backgroundCollidable;

    protected Color backgroundColor = Color.BLACK;

    protected FontScalar fontScalar = new LinearFontScalar(10, 40);

    protected KumoFont kumoFont = new KumoFont("Comic Sans MS", FontWeight.BOLD);

    protected AngleGenerator angleGenerator = new AngleGenerator();

    protected final CollisionRaster collisionRaster;

    protected final BufferedImage bufferedImage;

    protected final Set<Word> placedWords = new HashSet<>();

    protected final Set<Word> skipped = new HashSet<>();

    protected ColorPalette colorPalette = new ColorPalette(Color.ORANGE, Color.WHITE, Color.YELLOW, Color.GRAY, Color.GREEN);
    
    protected WordStartScheme startscheme = new RandomWordStart();
    
    public WordCloud(final int width, final int height, final CollisionMode collisionMode) {
        this.width = width;
        this.height = height;
        this.collisionMode = collisionMode;
        switch(collisionMode) {
            case PIXEL_PERFECT:
                this.padder = new WordPixelPadder();
                this.collisionChecker = new RectanglePixelCollisionChecker();
                break;

            case RECTANGLE:
            default:
                this.padder = new RectanglePadder();
                this.collisionChecker = new RectangleCollisionChecker();
                break;
        }
        this.collisionRaster = new CollisionRaster(width, height);
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.backgroundCollidable = new RectanglePixelCollidable(collisionRaster, 0, 0);
        this.background = new RectangleBackground(width, height);
    }

    public void build(final List<WordFrequency> wordFrequencies) {
        Collections.sort(wordFrequencies);
        int currentWord = 1;
        final Dimension dimensions = new Dimension(width, height);
        for(final Word word : buildwords(wordFrequencies, this.colorPalette)) {
            final Point p = startscheme.getStartingPoint(dimensions, word);
            final boolean placed = place(word, p.x, p.y);

            if (placed) {
                LOGGER.info("placed: " + word.getWord() + " (" + currentWord + "/" + wordFrequencies.size() + ")");
            } else {
                LOGGER.info("skipped: " + word.getWord() + " (" + currentWord + "/" + wordFrequencies.size() + ")");
            }
                
            currentWord++;
        }
        drawForgroundToBackground();
    }

    public void writeToFile(final String outputFileName) {
        String extension = "";
        int i = outputFileName.lastIndexOf('.');
        if (i > 0) {
            extension = outputFileName.substring(i + 1);
        }
        try {
            LOGGER.info("Saving WordCloud to " + outputFileName);
            ImageIO.write(bufferedImage, extension, new File(outputFileName));
        } catch (IOException e) {
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
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Could not write wordcloud to outputstream due to an IOException", e);
        }
    }

    /**
     * create background, then draw current word cloud on top of it.
     * Doing it this way preserves the transparency of the this.bufferedImage's pixels
     * for a more flexible pixel perfect collision
     */
    protected void drawForgroundToBackground() {
        if(backgroundColor == null) { return; }

        final BufferedImage backgroundBufferedImage = new BufferedImage(width, height, this.bufferedImage.getType());
        final Graphics graphics = backgroundBufferedImage.getGraphics();

        // draw current color
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);
        graphics.drawImage(bufferedImage, 0, 0, null);

        // draw back to original
        final Graphics graphics2 = bufferedImage.getGraphics();
        graphics2.drawImage(backgroundBufferedImage, 0, 0, null);
    }

    /**
     * try to place in center, build out in a spiral trying to place words for N steps
     * @param word
     */
    protected boolean place(final Word word, final int startX, final int startY) {
        final Graphics graphics = this.bufferedImage.getGraphics();

        final int maxRadius = width;

        for(int r = 0; r < maxRadius; r += 2) {
            for(int x = -r; x <= r; x++) {
                if(startX + x < 0) { continue; }
                if(startX + x >= width) { continue; }

                boolean placed = false;
                word.setX(startX + x);

                // try positive root
                int y1 = (int) Math.sqrt(r * r - x * x);
                if(startY + y1 >= 0 && startY + y1 < height) {
                    word.setY(startY + y1);
                    placed = tryToPlace(word);
                }
                // try negative root
                int y2 = -y1;
                if(!placed && startY + y2 >= 0 && startY + y2 < height) {
                    word.setY(startY + y2);
                    placed = tryToPlace(word);
                }
                if(placed) {
                    collisionRaster.mask(word.getCollisionRaster(), word.getX(), word.getY());
                    graphics.drawImage(word.getBufferedImage(), word.getX(), word.getY(), null);
                    return true;
                }

            }
        }
        skipped.add(word);
        return false;
    }

    private boolean tryToPlace(final Word word) {
        if(!background.isInBounds(word)) { return false; }

        switch(this.collisionMode) {
            case RECTANGLE:
                for(Word placeWord : this.placedWords) {
                    if(placeWord.collide(word)) {
                        return false;
                    }
                }
                placedWords.add(word);
                return true;

            case PIXEL_PERFECT:
                if(backgroundCollidable.collide(word)) { return false; }
                placedWords.add(word);
                return true;

        }
        return false;
    }

    protected List<Word> buildwords(final List<WordFrequency> wordFrequencies, final ColorPalette colorPalette) {
        final int maxFrequency = maxFrequency(wordFrequencies);

        final List<Word> words = new ArrayList<>();
        for(final WordFrequency wordFrequency : wordFrequencies) {
            if(!wordFrequency.getWord().isEmpty()) {
                words.add(buildWord(wordFrequency, maxFrequency, colorPalette));
            }
        }
        return words;
    }

    private Word buildWord(final WordFrequency wordFrequency, int maxFrequency, final ColorPalette colorPalette) {
        final Graphics graphics = this.bufferedImage.getGraphics();

        final int frequency = wordFrequency.getFrequency();
        final float fontHeight = this.fontScalar.scale(frequency, 0, maxFrequency);
        final Font font = kumoFont.getFont().deriveFont(fontHeight);

        final FontMetrics fontMetrics = graphics.getFontMetrics(font);
        final Word word = new Word(wordFrequency.getWord(), colorPalette.next(), fontMetrics, this.collisionChecker);

        final double theta = angleGenerator.randomNext();
        if(theta != 0) {
            word.setBufferedImage(ImageRotation.rotate(word.getBufferedImage(), theta));
        }
        if(padding > 0) {
            padder.pad(word, padding);
        }
        return word;
    }

    private int maxFrequency(final Collection<WordFrequency> wordFrequencies) {
        if(wordFrequencies.isEmpty()) { return 1; }
        return Lambda.max(wordFrequencies, on(WordFrequency.class).getFrequency());
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setColorPalette(ColorPalette colorPalette) {
        this.colorPalette = colorPalette;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public void setFontScalar(FontScalar fontScalar) {
        this.fontScalar = fontScalar;
    }

    public void setKumoFont(KumoFont kumoFont) {
        this.kumoFont = kumoFont;
    }

    public void setAngleGenerator(AngleGenerator angleGenerator) {
        this.angleGenerator = angleGenerator;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public Set<Word> getSkipped() {
        return skipped;
    }
    
    public void setWordStartScheme(WordStartScheme startscheme) {
        this.startscheme = startscheme;
    }
}
