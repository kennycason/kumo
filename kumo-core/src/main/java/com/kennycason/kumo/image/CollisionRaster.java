package com.kennycason.kumo.image;

import com.kennycason.kumo.interfaces.DimensionAbst;
import com.kennycason.kumo.interfaces.ImageAbst;
import com.kennycason.kumo.interfaces.InstanceCreator;
import com.kennycason.kumo.interfaces.PointAbst;

/**
 * Created by kenny on 7/4/14.
 */
public class CollisionRaster {

    private final int[][] data;

    private final DimensionAbst dimension;

    public CollisionRaster(final ImageAbst bufferedImage) {
        this.dimension = InstanceCreator.dimension(bufferedImage.getWidth(), bufferedImage.getHeight());

        data = new int[dimension.getWidth()][dimension.getHeight()];
        for (int y = 0; y < dimension.getHeight(); y++) {
            for (int x = 0; x < dimension.getWidth(); x++) {
                data[x][y] = bufferedImage.getColor(x, y);
            }
        }
    }

    public CollisionRaster(final DimensionAbst dimension) {
        this.dimension = dimension;
        data = new int[dimension.getWidth()][dimension.getHeight()];
    }

    public int getRGB(final int x, final int y) {
        return data[x][y];
    }

    public void setRGB(final int x, final int y, final int rgb) {
        data[x][y] = rgb;
    }

    public void mask(final CollisionRaster collisionRaster, final PointAbst point) {
        final int maxHeight = Math.min(point.getY() + collisionRaster.getDimension().getHeight(), dimension.getHeight());
        final int maxWidth = Math.min(point.getX() + collisionRaster.getDimension().getWidth(), dimension.getWidth());
        for (int offY = point.getY(), offY2 = 0; offY < maxHeight; offY++, offY2++) {
            for (int offX = point.getX(), offX2 = 0; offX < maxWidth; offX++, offX2++) {
                if (!collisionRaster.isTransparent(offX2, offY2)) {
                    data[offX][offY] = collisionRaster.getRGB(offX2, offY2);
                }
            }
        }
    }

    public boolean isTransparent(final int x, final int y) {
        return (data[x][y] & 0xFF000000) == 0x00000000;
    }

    public DimensionAbst getDimension() {
        return dimension;
    }

}
