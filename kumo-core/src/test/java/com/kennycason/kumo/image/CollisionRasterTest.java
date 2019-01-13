package com.kennycason.kumo.image;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

    /**
     * this test ensures setting one pixel doen't affect another pixel and not other line.
     */
    @Test
    public void correctPixelIsTransparent() {
        final int width = 90;
        final int height = 30;

        for (int ySet = 0; ySet < height; ySet++) {
            for (int xSet = 0; xSet < width; xSet++) {
                final Dimension dimension = new Dimension(width, height);
                final CollisionRaster collisionRaster = new CollisionRaster(dimension);

                collisionRaster.setPixelIsNotTransparent(xSet, ySet);

                for (int y = 0; y < height; y++) {
                    if (y == ySet) {
                        assertFalse(collisionRaster.lineIsTransparent(y));
                    } else {
                        assertTrue(collisionRaster.lineIsTransparent(y));
                    }
                    
                    for (int x = 0; x < width; x++) {
                        if (x == xSet && y == ySet) {
                            assertFalse(collisionRaster.isTransparent(x, y));
                        } else {
                            assertTrue(collisionRaster.isTransparent(x, y));
                        }
                    }
                }
            }
        }
    }
}
