package com.kennycason.kumo.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.BitSet;

/**
 * Created by kenny on 7/4/14.
 */
public class CollisionRaster {

    private final BitSet data;
    private final BitSet lines;

    private final Dimension dimension;

    public CollisionRaster(final BufferedImage bufferedImage) {
        this(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));

        for (int y = 0; y < dimension.height; y++) {
            for (int x = 0; x < dimension.width; x++) {
                boolean pixelIsTransparent = (bufferedImage.getRGB(x, y) & 0xFF000000) == 0x00000000;

                if (!pixelIsTransparent) {
                    setPixelIsNotTransparent(x, y);
                }
            }
        }
    }

    public CollisionRaster(final Dimension dimension) {
        this.dimension = dimension;

        data = new BitSet(dimension.width * dimension.height);
        lines = new BitSet(dimension.height);
    }

    
    public CollisionRaster(CollisionRaster other) {
        this.dimension = other.dimension;
        this.data = (BitSet) other.data.clone();
        this.lines = (BitSet) other.lines.clone();
    }

    private int computeIndex(final int x, final int y) {
        if (x < 0 || x >= dimension.width) {
            throw new IllegalArgumentException("x is out of bounds");
        } else if (y < 0 || y >= dimension.height) {
            throw new IllegalArgumentException("x is out of bounds");
        }
        
        return (y * dimension.width) + x;
    }

    public final void setPixelIsNotTransparent(final int x, final int y) {
        data.set(computeIndex(x, y));
        lines.set(y);
    }

    public void mask(final CollisionRaster collisionRaster, final Point point) {
        final int maxHeight = Math.min(point.y + collisionRaster.getDimension().height, dimension.height);
        final int maxWidth = Math.min(point.x + collisionRaster.getDimension().width, dimension.width);
        
        for (int offY = point.y, offY2 = 0; offY < maxHeight; offY++, offY2++) {
            // we can't set the "line is not transparent" flag here, 
            // the maxWidth might be smaller than the collisionRaster's width
            if (!collisionRaster.lineIsTransparent(offY2)) {
                for (int offX = point.x, offX2 = 0; offX < maxWidth; offX++, offX2++) {
                    if (!collisionRaster.isTransparent(offX2, offY2)) {
                        setPixelIsNotTransparent(offX, offY);
                    }
                }
            }
        }
    }

    public boolean lineIsTransparent(int y) {
       return !lines.get(y);
    }

    public boolean isTransparent(final int x, final int y) {
        return !data.get(computeIndex(x, y));
    }

    public Dimension getDimension() {
        return dimension;
    }
    
}
