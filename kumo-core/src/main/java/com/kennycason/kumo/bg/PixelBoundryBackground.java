package com.kennycason.kumo.bg;

import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.collide.RectanglePixelCollidable;
import com.kennycason.kumo.image.CollisionRaster;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class creates a Background Mode based on the transparent Pixel-boundaries of a loaded image
 * @author kenny
 * @version 2014.06.30
 */
public class PixelBoundryBackground implements Background {

    private final CollisionRaster collisionRaster;
    
    private final Point position = new Point(0, 0);

    /**
     * Creates a PixelBoundaryBackground using an InputStream to load an image
     * 
     * @param imageInputStream
     *            InputStream containing an image file
     * @throws IOException when fails to open file stream
     */
    public PixelBoundryBackground(final InputStream imageInputStream) throws IOException {
        final BufferedImage bufferedImage = ImageIO.read(imageInputStream);
        this.collisionRaster = new CollisionRaster(bufferedImage);
    }
    
    /**
     * Creates a PixelBoundaryBackground using an image from the input file
     * 
     * @param file
     *            a File pointing to an image
     * @throws IOException when fails to open file stream
     */
    public PixelBoundryBackground(final File file) throws IOException {
        this(new FileInputStream(file));
    }

    /**
     * Creates a PixelBoundaryBackground using an image-path
     * 
     * @param filepath
     *            path to an image file
     * @throws IOException when fails to open file stream
     */
    public PixelBoundryBackground(final String filepath) throws IOException {
        this(new File(filepath));
    }
    
    @Override
    public void mask(RectanglePixelCollidable background) {
        Dimension dimensionOfShape = collisionRaster.getDimension();
        Dimension dimensionOfBackground = background.getDimension();
        
        int minY = Math.max(position.y, 0);
        int minX = Math.max(position.x, 0);
        
        int maxY = Math.min(
                dimensionOfShape.height - minY, 
                dimensionOfBackground.height
        );
        int maxX = Math.min(
                dimensionOfShape.width - minX, 
                dimensionOfBackground.width
        );
        
        CollisionRaster rasterOfBackground = background.getCollisionRaster();
        
        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                if (collisionRaster.isTransparent(x, y)) {
                     rasterOfBackground.setPixelIsNotTransparent(
                             position.x + x, position.y + y
                     );
                }
            }
        }
    }
    
/*
    @Override
    public boolean isInBounds(final Collidable collidable) {
        // check if bounding boxes intersect
        if (!this.rectangleBackground.isInBounds(collidable)) {
            return false;
        }
        final Point position = collidable.getPosition();
        // get the overlapping box
        final int startX = Math.max(position.x, 0);
        final int endX = Math.min(position.x + collidable.getDimension().width, collisionRaster.getDimension().width);

        final int startY = Math.max(position.y, 0);
        final int endY = Math.min(position.y + collidable.getDimension().height, collisionRaster.getDimension().height);

        final CollisionRaster rasterOfCollidable = collidable.getCollisionRaster();

        for (int y = startY; y < endY; y++) {
            final int yOfCollidable = y - position.y;
            
            if (rasterOfCollidable.lineIsTransparent(yOfCollidable)) {
                continue;
            }
            
            for (int x = startX; x < endX; x++) {
                // compute offsets for surface
                if (collisionRaster.isTransparent(x, y) &&
                        !rasterOfCollidable.isTransparent(x - position.x, yOfCollidable)) {
                    return false;
                }
            }
        }
        return true;
    }
*/
}
