package com.kennycason.kumo;

import com.kennycason.kumo.palette.ColorPalette;

import java.awt.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by kenny on 6/29/14.
 */
public class PolarWordCloud extends WordCloud {
    private static final Random RANDOM = new Random();

    private static final ColorPalette DEFAULT_POSITIVE_COLORS = new ColorPalette(new Color(0x1BE000), new Color(0x1AC902), new Color(0x15B000), new Color(0x129400), new Color(0x0F7A00), new Color(0x0B5E00));

    private static final ColorPalette DEFAULT_NEGATIVE_COLORS = new ColorPalette(new Color(0xF50000), new Color(0xDE0000), new Color(0xC90202), new Color(0xB50202), new Color(0x990202), new Color(0x800101));

    private final PolarBlendMode polarBlendMode;

    private ColorPalette colorPalette2;

    public PolarWordCloud(final Dimension dimension, final CollisionMode collisionMode) {
        this(dimension, collisionMode, PolarBlendMode.EVEN);
        this.colorPalette = DEFAULT_POSITIVE_COLORS;
        this.colorPalette2 = DEFAULT_NEGATIVE_COLORS;
    }

    public PolarWordCloud(final Dimension dimension,
                          final CollisionMode collisionMode,
                          final PolarBlendMode polarBlendMode) {
        super(dimension, collisionMode);
        this.polarBlendMode = polarBlendMode;
        this.colorPalette = DEFAULT_POSITIVE_COLORS;
        this.colorPalette2 = DEFAULT_NEGATIVE_COLORS;
    }

    public void build(final List<WordFrequency> wordFrequencies, final List<WordFrequency> wordFrequencies2) {
        Collections.sort(wordFrequencies);
        Collections.sort(wordFrequencies2);

        final List<Word> words = buildWords(wordFrequencies, colorPalette);
        final List<Word> words2 = buildWords(wordFrequencies2, colorPalette2);

        final Iterator<Word> wordIterator = words.iterator();
        final Iterator<Word> wordIterator2 = words2.iterator();

        final Point[] poles = getRandomPoles();
        final Point pole1 = poles[0];
        final Point pole2 = poles[1];
        
        // the background masks all none usable pixels and we can only check this raster
        background.mask(backgroundCollidable);
        
        while (wordIterator.hasNext() || wordIterator2.hasNext()) {

            if (wordIterator.hasNext()) {
                final Word word = wordIterator.next();
                final Point startPosition = getStartPosition(pole1);

                place(word, startPosition);
            }
            if (wordIterator2.hasNext()) {
                final Word word = wordIterator2.next();
                final Point startPosition = getStartPosition(pole2);

                place(word, startPosition);
            }
        }

        drawForegroundToBackground();
    }

    private Point getStartPosition(final Point pole) {
        switch (polarBlendMode) {
            case BLUR:
                final int blurX = dimension.width / 2;
                final int blurY = dimension.height / 2;
                return new Point(
                    pole.x + -blurX + RANDOM.nextInt(blurX * 2),
                    pole.y + -blurY + RANDOM.nextInt(blurY * 2)
                );

            case EVEN:
                return pole;
        }
        throw new IllegalArgumentException("PolarBlendMode must not be null");
    }

    private Point[] getRandomPoles() {
        final Point[] max = new Point[2];
        double maxDistance = 0.0;
        for (int i = 0; i < 100; i++) {
            final int x = RANDOM.nextInt(dimension.width);
            final int y = RANDOM.nextInt(dimension.height);
            final int x2 = RANDOM.nextInt(dimension.width);
            final int y2 = RANDOM.nextInt(dimension.height);
            final double distance = Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
            if (distance > maxDistance) {
                maxDistance = distance;
                max[0] = new Point(x, y);
                max[1] = new Point(x2, y2);
            }
        }
        return max;
    }

    public void setColorPalette2(final ColorPalette colorPalette2) {
        this.colorPalette2 = colorPalette2;
    }

}
