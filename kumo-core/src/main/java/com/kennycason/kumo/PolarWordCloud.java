package com.kennycason.kumo;

import com.kennycason.kumo.interfaces.DimensionAbst;
import com.kennycason.kumo.interfaces.InstanceCreator;
import com.kennycason.kumo.interfaces.PointAbst;
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

    private static final ColorPalette DEFAULT_POSITIVE_COLORS = new ColorPalette(InstanceCreator.color(0x1BE000), InstanceCreator.color(0x1AC902), InstanceCreator.color(0x15B000), InstanceCreator.color(0x129400), InstanceCreator.color(0x0F7A00), InstanceCreator.color(0x0B5E00));

    private static final ColorPalette DEFAULT_NEGATIVE_COLORS = new ColorPalette(InstanceCreator.color(0xF50000), InstanceCreator.color(0xDE0000), InstanceCreator.color(0xC90202), InstanceCreator.color(0xB50202), InstanceCreator.color(0x990202), InstanceCreator.color(0x800101));

    private final PolarBlendMode polarBlendMode;

    private ColorPalette colorPalette2;

    public PolarWordCloud(final DimensionAbst dimension, final CollisionMode collisionMode) {
        this(dimension, collisionMode, PolarBlendMode.EVEN);
        this.colorPalette = DEFAULT_POSITIVE_COLORS;
        this.colorPalette2 = DEFAULT_NEGATIVE_COLORS;
    }

    public PolarWordCloud(final DimensionAbst dimension,
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

        final PointAbst[] poles = getRandomPoles();
        final PointAbst pole1 = poles[0];
        final PointAbst pole2 = poles[1];

        while (wordIterator.hasNext() || wordIterator2.hasNext()) {

            if (wordIterator.hasNext()) {
                final Word word = wordIterator.next();
                final PointAbst startPosition = getStartPosition(pole1);

                place(word, startPosition);
            }
            if (wordIterator2.hasNext()) {
                final Word word = wordIterator2.next();
                final PointAbst startPosition = getStartPosition(pole2);

                place(word, startPosition);
            }
        }

        drawForegroundToBackground();
    }

    private PointAbst getStartPosition(final PointAbst pole) {
        switch (polarBlendMode) {
            case BLUR:
                final int blurX = dimension.getWidth() / 2;
                final int blurY = dimension.getHeight() / 2;
                return InstanceCreator.point(
                    pole.getX() + -blurX + RANDOM.nextInt(blurX * 2),
                    pole.getY() + -blurY + RANDOM.nextInt(blurY * 2)
                );

            case EVEN:
                return pole;
        }
        throw new IllegalArgumentException("PolarBlendMode must not be null");
    }

    private PointAbst[] getRandomPoles() {
        final PointAbst[] max = new PointAbst[2];
        double maxDistance = 0.0;
        for (int i = 0; i < 100; i++) {
            final int x = RANDOM.nextInt(dimension.getWidth());
            final int y = RANDOM.nextInt(dimension.getHeight());
            final int x2 = RANDOM.nextInt(dimension.getWidth());
            final int y2 = RANDOM.nextInt(dimension.getHeight());
            final double distance = Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
            if (distance > maxDistance) {
                maxDistance = distance;
                max[0] = InstanceCreator.point(x, y);
                max[1] = InstanceCreator.point(x2, y2);
            }
        }
        return max;
    }

    public void setColorPalette2(final ColorPalette colorPalette2) {
        this.colorPalette2 = colorPalette2;
    }

}
