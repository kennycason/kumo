package wordcloud;

import ch.lambdaj.Lambda;
import org.apache.log4j.Logger;
import wordcloud.bg.Background;
import wordcloud.bg.RectangleBackground;
import wordcloud.collide.CollisionChecker;
import wordcloud.collide.RectangleCollisionChecker;
import wordcloud.font.FontScalar;
import wordcloud.font.LinearFontScalar;
import wordcloud.image.ImageRotation;
import wordcloud.padding.Padder;
import wordcloud.padding.RectanglePadder;
import wordcloud.palette.ColorPalette;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

    private static final Random RANDOM = new Random();

    private final int width;

    private final int height;

    private Background background;

    private Color backgroundColor = Color.BLACK;

    private int padding = 0;

    private Padder padder = new RectanglePadder();

    private ColorPalette colorPalette = new ColorPalette(Color.ORANGE, Color.WHITE, Color.YELLOW, Color.GRAY, Color.GREEN);

    private FontScalar fontScalar = new LinearFontScalar(10, 40);

    private CollisionChecker collisionChecker = new RectangleCollisionChecker();

    private double[] thetas = new double[] {0, -Math.PI / 2, Math.PI / 2};

    private final BufferedImage bufferedImage;

    private final Set<Word> placedWords = new HashSet<>();

    private final Set<Word> skipped = new HashSet<>();

    public WordCloud(int width, int height) {
        this.width = width;
        this.height = height;
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.background = new RectangleBackground(width, height);
    }

    public void build(List<WordFrequency> wordFrequencies) {
        drawBackground();
        Collections.sort(wordFrequencies, WORD_FREQUENCE_COMPARATOR);

        final List<Word> words = buildWordRectangles(wordFrequencies);

        int i = 0;
        for(final Word word : words) {
            final double theta = thetas[i % thetas.length];
            if(theta != 0) {
                word.setBufferedImage(ImageRotation.rotate(word.getBufferedImage(), theta));
            }
            place(word);
            i++;
        }
    }

    public void writeToFile(final String outputFileName) {
        String extension = "";
        int i = outputFileName.lastIndexOf('.');
        if (i > 0) {
            extension = outputFileName.substring(i + 1);
        }
        try {
            ImageIO.write(bufferedImage, extension, new File(outputFileName));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    private void drawBackground() {
        final Graphics graphics = this.bufferedImage.getGraphics();
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);
    }

    /**
     * try to place in center, build out in a spiral trying to place words for N steps
     * @param word
     */
    private void place(final Word word) {
        final int maxRadius = width;
        final int startX = RANDOM.nextInt(width - word.getWidth());
        final int startY = RANDOM.nextInt(height - word.getHeight());

        final Graphics graphics = this.bufferedImage.getGraphics();

        for(int r = 0; r < maxRadius; r++) {
            for(int x = 0; x <= r; x++) {
                int y1 = (int) Math.sqrt(r * r - x * x);
                int y2 = - y1;

                boolean placed;
                word.setX(startX + x);
                word.setY(startY + y1);
                placed = tryToPlace(word);
                if(!placed) {
                    word.setY(startY + y2);
                    placed = tryToPlace(word);
                }
                if(placed) {
                    word.draw(graphics);
                    return;
                }

            }
        }
        LOGGER.info("skipped: " + word.getWord());
        skipped.add(word);
    }

    private boolean tryToPlace(final Word word) {
        boolean collided = false;
        if(!background.isInBounds(word)) { return false; }
        for(Word placeWord : placedWords) {
            if(word.collide(placeWord)) {
                collided = true;
                break;
            }
        }
        if(!collided) {
            LOGGER.info("placed: " + word.getWord());
            placedWords.add(word);
            return true;
        }
        return false;
    }

    private List<Word> buildWordRectangles(final List<WordFrequency> wordFrequencies) {
        final int maxFrequency = maxFrequency(wordFrequencies);

        final List<Word> words = new ArrayList<>();
        for(final WordFrequency wordFrequency : wordFrequencies) {
            words.add(buildWordRectangle(wordFrequency, maxFrequency));
        }
        return words;
    }

    private Word buildWordRectangle(final WordFrequency wordFrequency, int maxFrequency) {
        final Graphics graphics = this.bufferedImage.getGraphics();

        final int frequency = wordFrequency.getFrequency();
        final int fontHeight = this.fontScalar.scale(frequency, 0, maxFrequency);

        final Font font = new Font("Comic Sans MS", Font.BOLD, fontHeight);
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

    private static final Comparator<WordFrequency> WORD_FREQUENCE_COMPARATOR = new Comparator<WordFrequency>() {
        @Override
        public int compare(WordFrequency o1, WordFrequency o2) {
            return o2.getFrequency() - o1.getFrequency();
        }
    };

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setPadder(Padder padder) {
        this.padder = padder;
    }

    public void setCollisionChecker(CollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
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

    public void setThetas(double[] thetas) {
        this.thetas = thetas;
    }
}
