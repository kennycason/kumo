package com.kennycason.kumo.image;

import org.junit.Test;

import java.awt.*;

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
        final Dimension dimension = new Dimension(width, height);
        final CollisionRaster collisionRaster = new CollisionRaster(dimension);

        assertEquals(width, collisionRaster.getDimension().width);
        assertEquals(height, collisionRaster.getDimension().height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                assertTrue(collisionRaster.isTransparent(x, y));
            }
        }
    }

}
