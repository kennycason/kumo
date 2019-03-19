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
     * @param imageInputStream InputStream containing an image file
     * @throws IOException when fails to open file stream
     */
    public PixelBoundryBackground(final InputStream imageInputStream) throws IOException {
        final BufferedImage bufferedImage = ImageIO.read(imageInputStream);
        this.collisionRaster = new CollisionRaster(bufferedImage);
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
        
        int minY = Math.max(position.y, 0);
        int minX = Math.max(position.x, 0);

        int maxY = dimensionOfShape.height - position.y - 1;
        int maxX = dimensionOfShape.width - position.x - 1;

        CollisionRaster rasterOfBackground = background.getCollisionRaster();

        for (int y = 0; y < dimensionOfBackground.height; y++) {
            if (y < minY || y > maxY) {
                for (int x = 0; x < dimensionOfBackground.width; x++) {
                    rasterOfBackground.setPixelIsNotTransparent(
                            position.x + x, position.y + y
                    );
                }
            } else {
                for (int x = 0; x < dimensionOfBackground.width; x++) {
                    if (x < minX || x > maxX || collisionRaster.isTransparent(x, y)) {
                        rasterOfBackground.setPixelIsNotTransparent(
                                position.x + x, position.y + y
                        );
                    }
                }
            }

        }
    }
}
