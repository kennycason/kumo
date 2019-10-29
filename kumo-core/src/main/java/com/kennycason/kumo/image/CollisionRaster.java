package com.kennycason.kumo.image;

import com.kennycason.kumo.draw.Dimension;
import com.kennycason.kumo.draw.Image;
import com.kennycason.kumo.draw.Point;

import java.util.BitSet;

/**
 * Created by kenny on 7/4/14.
 */
public class CollisionRaster {

    private final BitSet data;

    private final Dimension dimension;

    public CollisionRaster(final Image image) {
        this(new Dimension(image.getWidth(), image.getHeight()));

        for (int y = 0; y < dimension.getHeight(); y++) {
            for (int x = 0; x < dimension.getWidth(); x++) {
                boolean pixelIsTransparent = (image.getColor(x, y) & 0xFF000000) == 0x00000000;

                if (!pixelIsTransparent) {
                    setPixelIsNotTransparent(x, y);
                }
            }
        }
    }

    public CollisionRaster(final Dimension dimension) {
        this.dimension = dimension;

        data = new BitSet(dimension.getWidth() * dimension.getHeight());
    }


    public CollisionRaster(CollisionRaster other) {
        this.dimension = other.dimension;
        this.data = (BitSet) other.data.clone();
    }

    private int computeIndex(final int x, final int y) {
        if (x < 0 || x >= dimension.getWidth()) {
            throw new IllegalArgumentException("x is out of bounds");
        } else if (y < 0 || y >= dimension.getHeight()) {
            throw new IllegalArgumentException("y is out of bounds");
        }

        return (y * dimension.getWidth()) + x;
    }

    public final void setPixelIsNotTransparent(final int x, final int y) {
        data.set(computeIndex(x, y));
    }

    public void mask(final CollisionRaster collisionRaster, final Point point) {
        final int maxHeight = Math.min(point.getY() + collisionRaster.getDimension().getHeight(), dimension.getHeight());
        final int maxWidth = Math.min(point.getX() + collisionRaster.getDimension().getWidth(), dimension.getWidth());
        for (int offY = point.getY(), offY2 = 0; offY < maxHeight; offY++, offY2++) {
            // we can't set the "line is not transparent" flag here,
            // the maxWidth might be smaller than the collisionRaster's width
            for (int offX = point.getX(), offX2 = 0; offX < maxWidth; offX++, offX2++) {
                if (!collisionRaster.isTransparent(offX2, offY2)) {
                    setPixelIsNotTransparent(offX, offY);
                }
            }
        }
    }


    /**
     * @param minX (inclusive) start of the pixels to test
     * @param maxX (exclusive) end of the pixels to test
     * @param y the line to check
     */
    public int nextNotTransparentPixel(final int minX, final int maxX, int y) {
        if (maxX > dimension.getWidth()) {
            throw new IllegalArgumentException("maxX is out of bounds");
        }

        int idx = computeIndex(minX, y);
        int set = data.nextSetBit(idx);

        if (set != -1 && set < idx + maxX - minX) {
            return (set - idx) + minX;
        } else {
            return -1;
        }
    }

    public boolean isTransparent(final int x, final int y) {
        return !data.get(computeIndex(x, y));
    }

    public Dimension getDimension() {
        return dimension;
    }

}
