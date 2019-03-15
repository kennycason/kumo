package com.kennycason.kumo;

import com.kennycason.kumo.draw.Color;
import com.kennycason.kumo.draw.Dimension;
import com.kennycason.kumo.draw.Point;
import com.kennycason.kumo.palette.ColorPalette;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by kenny on 6/29/14.
 */
public class PolarWordCloud extends WordCloud {
    private static final Random RANDOM = new Random();

    private static final ColorPalette DEFAULT_POSITIVE_COLORS = new ColorPalette(new Color(27,224,0), new Color(26,201,2), new Color(21,176,0), new Color(18,148,0), new Color(15,122,0), new Color(11,94,0));

    private static final ColorPalette DEFAULT_NEGATIVE_COLORS = new ColorPalette(new Color(245,0,0), new Color(222,0,0), new Color(201,2,2), new Color(181,2,2), new Color(153,2,2), new Color(128,1,1));

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
                final int blurX = dimension.getWidth() / 2;
                final int blurY = dimension.getHeight() / 2;
                return new Point(
                    pole.getX() + -blurX + RANDOM.nextInt(blurX * 2),
                    pole.getY() + -blurY + RANDOM.nextInt(blurY * 2)
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
            final int x = RANDOM.nextInt(dimension.getWidth());
            final int y = RANDOM.nextInt(dimension.getHeight());
            final int x2 = RANDOM.nextInt(dimension.getWidth());
            final int y2 = RANDOM.nextInt(dimension.getHeight());
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
