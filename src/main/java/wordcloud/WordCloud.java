package wordcloud;

import ch.lambdaj.Lambda;
import org.apache.log4j.Logger;
import wordcloud.bg.Background;
import wordcloud.bg.RectangleBackground;
import wordcloud.collide.RectanglePixelCollidable;
import wordcloud.collide.Vector2d;
import wordcloud.collide.checkers.CollisionChecker;
import wordcloud.collide.checkers.RectangleCollisionChecker;
import wordcloud.collide.checkers.RectanglePixelCollisionChecker;
import wordcloud.font.CloudFont;
import wordcloud.font.FontWeight;
import wordcloud.font.scale.FontScalar;
import wordcloud.font.scale.LinearFontScalar;
import wordcloud.image.AngleGenerator;
import wordcloud.image.CollisionRaster;
import wordcloud.image.ImageRotation;
import wordcloud.padding.Padder;
import wordcloud.padding.RectanglePadder;
import wordcloud.padding.WordPixelPadder;
import wordcloud.palette.ColorPalette;
import wordcloud.spiral.RectangleSpiralFunction;
import wordcloud.spiral.SpiralFunction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static ch.lambdaj.Lambda.on;

/**
 * Created by kenny on 6/29/14.
 */
public class WordCloud {

    private static final Logger LOGGER = Logger.getLogger(WordCloud.class);

    protected static final Random RANDOM = new Random();

    protected final int width;

    protected final int height;

    protected final CollisionMode collisionMode;

    protected final CollisionChecker collisionChecker;

    protected final CollisionRaster collisionRaster;

    protected final BufferedImage bufferedImage;

    protected final Set<Word> placedWords = new HashSet<>();

    protected final Set<Word> skipped = new HashSet<>();

    protected final Padder padder;

    protected int padding = 0;

    protected Background background;

    protected final RectanglePixelCollidable backgroundCollidable;

    protected Color backgroundColor = Color.BLACK;

    protected FontScalar fontScalar = new LinearFontScalar(10, 40);

    protected CloudFont cloudFont = new CloudFont("Comic Sans MS", FontWeight.BOLD);

    protected AngleGenerator angleGenerator = new AngleGenerator();

    protected ColorPalette colorPalette = new ColorPalette(Color.ORANGE, Color.WHITE, Color.YELLOW, Color.GRAY, Color.GREEN);

    public WordCloud(int width, int height, CollisionMode collisionMode) {
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

    public void build(List<WordFrequency> wordFrequencies) {
        Collections.sort(wordFrequencies);

        for(final Word word : buildwords(wordFrequencies, this.colorPalette)) {
            final int startX = RANDOM.nextInt(Math.max(width - word.getWidth(), width));
            final int startY = RANDOM.nextInt(Math.max(height - word.getHeight(), height));
            place(word, new Vector2d(startX, startY));

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
    protected void place(final Word word, final Vector2d start) {
        final Graphics graphics = this.bufferedImage.getGraphics();

        final SpiralFunction spiralFunction = new RectangleSpiralFunction(start, width);

        while(spiralFunction.hasNext()) {
            final Vector2d point = spiralFunction.next();
            if(point.getX() < 0 ||
                    point.getX() >= width ||
                    point.getY() < 0 ||
                    point.getY() >= height) {
                continue;
            }

            word.setXY(point);
            if(tryToPlace(word)) {
                collisionRaster.mask(word.getCollisionRaster(), word.getX(), word.getY());
                graphics.drawImage(word.getBufferedImage(), word.getX(), word.getY(), null);
                return;
            }
        }
        LOGGER.info("skipped: " + word.getWord());
        skipped.add(word);
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
                LOGGER.info("place: " + word.getWord());
                placedWords.add(word);
                return true;

            case PIXEL_PERFECT:
                if(backgroundCollidable.collide(word)) { return false; }
                LOGGER.info("place: " + word.getWord());
                placedWords.add(word);
                return true;

        }
        return false;
    }

    protected List<Word> buildwords(final List<WordFrequency> wordFrequencies, final ColorPalette colorPalette) {
        final int maxFrequency = maxFrequency(wordFrequencies);

        final List<Word> words = new ArrayList<>();
        for(final WordFrequency wordFrequency : wordFrequencies) {
            words.add(buildWord(wordFrequency, maxFrequency, colorPalette));
        }
        return words;
    }

    private Word buildWord(final WordFrequency wordFrequency, int maxFrequency, final ColorPalette colorPalette) {
        final Graphics graphics = this.bufferedImage.getGraphics();

        final int frequency = wordFrequency.getFrequency();
        final float fontHeight = this.fontScalar.scale(frequency, 0, maxFrequency);
        final Font font = cloudFont.getFont().deriveFont(fontHeight);

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

    public void setCloudFont(CloudFont cloudFont) {
        this.cloudFont = cloudFont;
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
}
