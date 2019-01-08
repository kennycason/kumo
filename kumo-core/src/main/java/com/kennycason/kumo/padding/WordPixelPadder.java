package com.kennycason.kumo.padding;

import com.kennycason.kumo.Word;
import com.kennycason.kumo.image.CollisionRaster;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kenny on 7/1/14.
 * Add pixel padding around the numbers
 */
public class WordPixelPadder implements Padder {

    private RectanglePadder rectanglePadder = new RectanglePadder();

    public void pad(final Word word, final int padding) {
        if (padding <= 0) { return; }
        rectanglePadder.pad(word, padding);

        final CollisionRaster collisionRaster = word.getCollisionRaster();
        // create a copy of the original raster
        final CollisionRaster originalRaster = new CollisionRaster(collisionRaster);

        final int width = originalRaster.getDimension().width;
        final int height = originalRaster.getDimension().height;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (shouldPad(originalRaster, x, y, padding)) {
                    // update the raster based on the shouldPad result of the originalRaster
                    collisionRaster.setPixelIsNotTransparent(x, y);
                }
            }
        }
    }

    private boolean shouldPad(final CollisionRaster collisionRaster, final int cx, final int cy, final int padding) {
        if (!collisionRaster.isTransparent(cx, cy)) { return false; }

        for (int y = cy - padding; y <= cy + padding; y++) {
            for (int x = cx - padding; x <= cx + padding; x++) {
                if (x == cx && y == cy) { continue; }
                if (inBounds(collisionRaster, x, y)) {
                    if (!collisionRaster.isTransparent(x, y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean inBounds(final CollisionRaster collisionRaster, final int x, final int y) {
        return x >= 0
                && y >= 0
                && x < collisionRaster.getDimension().width
                && y < collisionRaster.getDimension().height;
    }

}
