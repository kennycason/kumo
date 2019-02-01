package com.kennycason.kumo.image;

import com.kennycason.kumo.abst.DimensionAbst;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kenny on 7/4/14.
 */
public class CollisionRasterTest {

    @Test
    public void basicTests() {
        final int width = 10;
        final int height = 5;
        final DimensionAbst dimension = DimensionAbst.get(width, height);
        final CollisionRaster collisionRaster = new CollisionRaster(dimension);

        assertEquals(width, collisionRaster.getDimension().getWidth());
        assertEquals(height, collisionRaster.getDimension().getHeight());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                assertTrue(collisionRaster.isTransparent(x, y));
            }
        }
    }

}
