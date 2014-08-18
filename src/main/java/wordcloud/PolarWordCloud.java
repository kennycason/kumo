package wordcloud;

import wordcloud.collide.Vector2d;
import wordcloud.palette.ColorPalette;

import java.awt.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kenny on 6/29/14.
 */
public class PolarWordCloud extends WordCloud {

    private static final ColorPalette DEFAULT_POSITIVE_COLORS = new ColorPalette(new Color(0x1BE000), new Color(0x1AC902), new Color(0x15B000), new Color(0x129400), new Color(0x0F7A00), new Color(0x0B5E00));

    private static final ColorPalette DEFAULT_NEGATIVE_COLORS = new ColorPalette(new Color(0xF50000), new Color(0xDE0000), new Color(0xC90202), new Color(0xB50202), new Color(0x990202), new Color(0x800101));

    private final PolarBlendMode polarBlendMode;

    private ColorPalette colorPalette2;

    public PolarWordCloud(int width, int height, CollisionMode collisionMode) {
        this(width, height, collisionMode, PolarBlendMode.EVEN);
        this.colorPalette = DEFAULT_POSITIVE_COLORS;
        this.colorPalette2 = DEFAULT_NEGATIVE_COLORS;
    }

    public PolarWordCloud(int width, int height, CollisionMode collisionMode, PolarBlendMode polarBlendMode) {
        super(width, height, collisionMode);
        this.polarBlendMode = polarBlendMode;
        this.colorPalette = DEFAULT_POSITIVE_COLORS;
        this.colorPalette2 = DEFAULT_NEGATIVE_COLORS;
    }

    public void build(List<WordFrequency> wordFrequencies, List<WordFrequency> wordFrequencies2) {
        Collections.sort(wordFrequencies);
        Collections.sort(wordFrequencies2);

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

                place(word, startPosition);
            }
            if(wordIterator2.hasNext()) {
                final Word word = wordIterator2.next();
                final Vector2d startPosition = getStartPosition(pole2);

                place(word, startPosition);
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

    public void setColorPalette2(ColorPalette colorPalette2) {
        this.colorPalette2 = colorPalette2;
    }

}
