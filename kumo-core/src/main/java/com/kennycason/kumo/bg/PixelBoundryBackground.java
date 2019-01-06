package com.kennycason.kumo.bg;

import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.image.CollisionRaster;
import com.kennycason.kumo.interfaces.ImageAbst;
import com.kennycason.kumo.interfaces.InstanceCreator;
import com.kennycason.kumo.interfaces.PointAbst;

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

    private final RectangleBackground rectangleBackground;

    /**
     * Creates a PixelBoundaryBackground using an InputStream to load an image
     * 
     * @param imageInputStream
     *            InputStream containing an image file
     * @throws IOException when fails to open file stream
     */
    public PixelBoundryBackground(final InputStream imageInputStream) throws IOException {
        final ImageAbst bufferedImage = InstanceCreator.image(imageInputStream);
        this.collisionRaster = new CollisionRaster(bufferedImage);
        this.rectangleBackground = new RectangleBackground(InstanceCreator.dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
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
    public boolean isInBounds(final Collidable collidable) {
        // check if bounding boxes intersect
        if (!this.rectangleBackground.isInBounds(collidable)) {
            return false;
        }
        final PointAbst position = collidable.getPosition();
        // get the overlapping box
        final int startX = Math.max(position.getX(), 0);
        final int endX = Math.min(position.getX() + collidable.getDimension().getWidth(), collisionRaster.getDimension().getWidth());

        final int startY = Math.max(position.getY(), 0);
        final int endY = Math.min(position.getY() + collidable.getDimension().getHeight(), collisionRaster.getDimension().getHeight());

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                // compute offsets for surface
                if (collisionRaster.isTransparent(x, y) &&
                        !collidable.getCollisionRaster().isTransparent(x - position.getX(), y - position.getY())) {
                    return false;
                }
            }
        }
        return true;
    }

}
