package com.kennycason.kumo.bg;

import com.kennycason.kumo.collide.RectanglePixelCollidable;
import com.kennycason.kumo.draw.Dimension;
import com.kennycason.kumo.draw.Image;
import com.kennycason.kumo.draw.Point;
import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.collide.RectanglePixelCollidable;
import com.kennycason.kumo.image.CollisionRaster;

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

    private final Point position = new Point(0, 0);

    /**
     * Creates a PixelBoundaryBackground using an InputStream to load an image
     *
     *
     * @param imageInputStream InputStream containing an image file
     * @throws IOException when fails to open file stream
     */
    public PixelBoundryBackground(final InputStream imageInputStream) throws IOException {
        final Image image = new Image(imageInputStream);
        this.collisionRaster = new CollisionRaster(image);
    }

    /**
     * Creates a PixelBoundaryBackground using an image from the input file
     * 
     * @param file a File pointing to an image
     * @throws IOException when fails to open file stream
     */
    public PixelBoundryBackground(final File file) throws IOException {
        this(new FileInputStream(file));
    }

    /**
     * Creates a PixelBoundaryBackground using an image-path
     * 
     * @param filepath path to an image file
     * @throws IOException when fails to open file stream
     */
    public PixelBoundryBackground(final String filepath) throws IOException {
        this(new File(filepath));
    }
    
    @Override
    public void mask(RectanglePixelCollidable background) {
        Dimension dimensionOfShape = collisionRaster.getDimension();
        Dimension dimensionOfBackground = background.getDimension();

        int minY = Math.max(position.getY(), 0);
        int minX = Math.max(position.getX(), 0);

        int maxY = dimensionOfShape.getHeight() - position.getY() - 1;
        int maxX = dimensionOfShape.getWidth() - position.getX() - 1;

        CollisionRaster rasterOfBackground = background.getCollisionRaster();

        for (int y = 0; y < dimensionOfBackground.getHeight(); y++) {
            if (y < minY || y > maxY) {
                for (int x = 0; x < dimensionOfBackground.getWidth(); x++) {
                    rasterOfBackground.setPixelIsNotTransparent(
                            position.getX() + x, position.getY() + y
                    );
                }
            } else {
                for (int x = 0; x < dimensionOfBackground.getWidth(); x++) {
                    if (x < minX || x > maxX || collisionRaster.isTransparent(x, y)) {
                        rasterOfBackground.setPixelIsNotTransparent(
                                position.getX() + x, position.getY() + y
                        );
                    }
                }
            }

        }
    }
}
