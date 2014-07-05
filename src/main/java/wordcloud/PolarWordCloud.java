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
import wordcloud.font.FontOptions;
import wordcloud.font.scale.FontScalar;
import wordcloud.font.scale.LinearFontScalar;
import wordcloud.image.AngleGenerator;
import wordcloud.image.CollisionRaster;
import wordcloud.image.ImageRotation;
import wordcloud.padding.Padder;
import wordcloud.padding.RectanglePadder;
import wordcloud.padding.WordPixelPadder;
import wordcloud.palette.ColorPalette;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static ch.lambdaj.Lambda.on;

/**
 * Created by kenny on 6/29/14.
 */
public class PolarWordCloud {

    private static final Logger LOGGER = Logger.getLogger(PolarWordCloud.class);

    private static final Random RANDOM = new Random();

    private final int width;

    private final int height;

    private final CollisionMode collisionMode;

    private final PolarBlendMode polarBlendMode;

    private final Padder padder;

    private final CollisionChecker collisionChecker;

    private Background background;

    private final RectanglePixelCollidable backgroundCollidable;

    private Color backgroundColor = Color.BLACK;

    private int padding = 0;

    private ColorPalette colorPalette = new ColorPalette(new Color(0x1BE000), new Color(0x1AC902), new Color(0x15B000), new Color(0x129400), new Color(0x0F7A00), new Color(0x0B5E00));

    private ColorPalette colorPalette2 = new ColorPalette(new Color(0xF50000), new Color(0xDE0000), new Color(0xC90202), new Color(0xB50202), new Color(0x990202), new Color(0x800101));

    private FontScalar fontScalar = new LinearFontScalar(10, 40);

    private FontOptions fontOptions = new FontOptions("Comic Sans MS", Font.BOLD);

    private AngleGenerator angleGenerator = new AngleGenerator();

    private final CollisionRaster collisionRaster;

    private final BufferedImage bufferedImage; // only used for getting graphics to render font

    private final Set<Word> placedWords = new HashSet<>();

    private final Set<Word> skipped = new HashSet<>();

    public PolarWordCloud(int width, int height, CollisionMode collisionMode) {
        this(width, height, collisionMode, PolarBlendMode.EVEN);
    }

    public PolarWordCloud(int width, int height, CollisionMode collisionMode, PolarBlendMode polarBlendMode) {
        this.width = width;
        this.height = height;
        this.collisionMode = collisionMode;
        this.polarBlendMode = polarBlendMode;
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

    public void build(List<WordFrequency> wordFrequencies, List<WordFrequency> wordFrequencies2) {
        Collections.sort(wordFrequencies, WordCloud.WORD_FREQUENCE_COMPARATOR);
        Collections.sort(wordFrequencies2, WordCloud.WORD_FREQUENCE_COMPARATOR);

        final List<Word> words = buildwords(wordFrequencies, colorPalette);
        final List<Word> words2 = buildwords(wordFrequencies2, colorPalette2);

        final Iterator<Word> wordIterator = words.iterator();
        final Iterator<Word> wordIterator2 = words2.iterator();

        final Vector2d[] poles = getRandomPoles();
        final Vector2d pole1 = poles[0];
        final Vector2d pole2 = poles[1];

        while(wordIterator.hasNext() || wordIterator2.hasNext()) {

            if(wordIterator.hasNext()) {
                final Word word = wordIterator.next();
                final Vector2d startPosition = getStartPosition(pole1);

                final double theta = angleGenerator.randomNext();
                word.setBufferedImage(ImageRotation.rotate(word.getBufferedImage(), theta));
                place(word, startPosition.getX(), startPosition.getY());
            }
            if(wordIterator2.hasNext()) {
                final Word word = wordIterator2.next();
                final Vector2d startPosition = getStartPosition(pole2);

                final double theta = angleGenerator.randomNext();
                word.setBufferedImage(ImageRotation.rotate(word.getBufferedImage(), theta));
                place(word, startPosition.getX(), startPosition.getY());
            }
        }

        drawForgroundToBackground();
    }

    private Vector2d getStartPosition(Vector2d pole) {
        switch(polarBlendMode) {
            case BLUR:
                final int blurX = width / 2;
                final int blurY = height / 2;
                return new Vector2d(
                    pole.getX() + -blurX + RANDOM.nextInt(blurX * 2),
                    pole.getY() + -blurY + RANDOM.nextInt(blurY * 2)
                );
            case EVEN:
            default:
                return pole;
        }
    }

    private Vector2d[] getRandomPoles() {
        final Vector2d[] max = new Vector2d[2];
        double maxDistance = 0.0;
        for(int i = 0; i < 100; i++) {
            final int x = RANDOM.nextInt(width);
            final int y = RANDOM.nextInt(height);
            final int x2 = RANDOM.nextInt(width);
            final int y2 = RANDOM.nextInt(height);
            final double distance = Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
            if(distance > maxDistance) {
                maxDistance = distance;
                max[0] = new Vector2d(x, y);
                max[1] = new Vector2d(x2, y2);
            }
        }
        return max;
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
    private void drawForgroundToBackground() {
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
    private void place(final Word word, final int startX, final int startY) {
        final int maxRadius = width;

        final Graphics graphics = this.bufferedImage.getGraphics();


        for(int r = 0; r < maxRadius; r += 2) {
            for(int x = -r; x <= r; x++) {
                if(startX + x < 0 || startX + x >= width) { continue; }

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
                    return;
                }

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

    private List<Word> buildwords(final List<WordFrequency> wordFrequencies, final ColorPalette colorPalette) {
        final int maxFrequency = maxFrequency(wordFrequencies);

        final List<Word> words = new ArrayList<>();
        for(final WordFrequency wordFrequency : wordFrequencies) {
            words.add(buildWord(wordFrequency, maxFrequency, colorPalette));
        }
        return words;
    }

    private Word buildWord(final WordFrequency wordFrequency, final int maxFrequency, final ColorPalette colorPalette) {
        final Graphics graphics = this.bufferedImage.getGraphics();

        final int frequency = wordFrequency.getFrequency();
        final int fontHeight = this.fontScalar.scale(frequency, 0, maxFrequency);
        final Font font = new Font(fontOptions.getType(), fontOptions.getWeight(), fontHeight);

        final FontMetrics fontMetrics = graphics.getFontMetrics(font);
        final Word word = new Word(wordFrequency.getWord(), colorPalette.next(), fontMetrics, this.collisionChecker);
        if(padding > 0) {
            padder.pad(word, padding, backgroundColor);
        }
        return word;
    }

    private int maxFrequency(final Collection<WordFrequency> wordFrequencies) {
        return Lambda.max(wordFrequencies, on(WordFrequency.class).getFrequency());
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setColorPalettes(ColorPalette colorPalette, ColorPalette colorPalette2) {
        this.colorPalette = colorPalette;
        this.colorPalette2 = colorPalette2;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public void setFontScalar(FontScalar fontScalar) {
        this.fontScalar = fontScalar;
    }

    public void setFontOptions(FontOptions fontOptions) {
        this.fontOptions = fontOptions;
    }

    public void setAngleGenerator(AngleGenerator angleGenerator) {
        this.angleGenerator = angleGenerator;
    }

    public Set<Word> getSkipped() {
        return skipped;
    }

}
