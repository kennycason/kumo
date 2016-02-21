package com.kennycason.kumo.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by kenny on 7/4/14.
 */
public class CollisionRaster {

    private final int[][] data;

    private final Dimension dimension;

    public CollisionRaster(final BufferedImage bufferedImage) {
        this.dimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());

        data = new int[dimension.width][dimension.height];
        for (int y = 0; y < dimension.height; y++) {
            for (int x = 0; x < dimension.width; x++) {
                data[x][y] = bufferedImage.getRGB(x, y);
            }
        }
    }

    public CollisionRaster(final Dimension dimension) {
        this.dimension = dimension;
        data = new int[dimension.width][dimension.height];
    }

    public int getRGB(final int x, final int y) {
        return data[x][y];
    }

    public void setRGB(final int x, final int y, final int rgb) {
        data[x][y] = rgb;
    }

    public void mask(final CollisionRaster collisionRaster, final Point point) {
        final int maxHeight = Math.min(point.y + collisionRaster.getDimension().height, dimension.height);
        final int maxWidth = Math.min(point.x + collisionRaster.getDimension().width, dimension.width);
        for (int offY = point.y, offY2 = 0; offY < maxHeight; offY++, offY2++) {
            for (int offX = point.x, offX2 = 0; offX < maxWidth; offX++, offX2++) {
                if (!collisionRaster.isTransparent(offX2, offY2)) {
                    data[offX][offY] = collisionRaster.getRGB(offX2, offY2);
                }
            }
        }
    }

    public boolean isTransparent(final int x, final int y) {
        return (data[x][y] & 0xFF000000) == 0x00000000;
    }

    public Dimension getDimension() {
        return dimension;
    }

}
