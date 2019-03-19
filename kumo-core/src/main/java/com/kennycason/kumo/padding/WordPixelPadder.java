package com.kennycason.kumo.padding;

import com.kennycason.kumo.Word;
import com.kennycason.kumo.image.CollisionRaster;

/**
 * Created by kenny on 7/1/14.
 * Add pixel padding around the numbers
 */
public class WordPixelPadder implements Padder {

    private RectanglePadder rectanglePadder = new RectanglePadder();

    public void pad(final Word word, final int padding) {
        if (padding <= 0) {
            return;
        }
        rectanglePadder.pad(word, padding);

        final CollisionRaster collisionRaster = word.getCollisionRaster();
        // create a copy of the original raster
        final CollisionRaster originalRaster = new CollisionRaster(collisionRaster);

        final int width = originalRaster.getDimension().width;
        final int height = originalRaster.getDimension().height;

        // this is the array with the sum of all set pixels in the padding area.
        // if the padding area is changed, we only need partial updates
        final int[] pixelsSetInPaddingPerColumn = new int[width];

        for (int x = 0; x < width; x++) {
            // create an array with the number of not transparent pixels in 
            // each coll of the padding area of point 0, 0
            pixelsSetInPaddingPerColumn[x] = countNotTransparentPixels(
                    originalRaster, 0, Math.min(padding, height -1), x
            );
        }

        for (int y = 0; y < height; y++) {
            // is the line inside the image?
            if (y - padding >= 0) {
                // the line (y - padding) is now outside the padding area, we need to update our index
                final int line = y - padding;
                int set = -1;  
                
                while ((set = originalRaster.nextNotTransparentPixel(set + 1, width, line)) != -1) {
                    pixelsSetInPaddingPerColumn[set]--;
                }
            }
            
            // is the line inside the image?
            if (y > 0 && y + padding < height) {
                // the line (y + padding) is now inside the padding area, we need to update our index
                final int line = y + padding;
                int set = -1;  
                
                while ((set = originalRaster.nextNotTransparentPixel(set + 1, width, line)) != -1) {
                    pixelsSetInPaddingPerColumn[set]++;
                }
            }
            
            int pixelsSetInPaddingArea = 0;
            
            for (int x = 0, n = Math.min(padding, width - 1); x < n; x++) {
                // create the sum of all columns in the padding area of the pixel at 0,y
                pixelsSetInPaddingArea += pixelsSetInPaddingPerColumn[x];
            }
            
            for (int x = 0; x < width; x++) {
                if (x - padding >= 0) {
                    // the column (x - padding) is now outside the padding area, we need to update our counter
                    pixelsSetInPaddingArea -= pixelsSetInPaddingPerColumn[x - padding];       
                }
                if (x > 0 && x + padding < width) {
                    // the column (x + padding) is now inside the padding area, we need to update our counter
                    pixelsSetInPaddingArea += pixelsSetInPaddingPerColumn[x + padding];
                }
                
                // do we have any none transparent pixels in this area?
                if (pixelsSetInPaddingArea > 0) {
                    collisionRaster.setPixelIsNotTransparent(x, y);
                }
            }
        }
    }

    private int countNotTransparentPixels(final CollisionRaster originalRaster, final int minY, final int maxY, final int x) {
        int n = 0;
        
        for (int y = minY; y <= maxY; y++) {
            if (!originalRaster.isTransparent(x, y)) {
                n++;
            }
        }
        
        return n;
    }
}
