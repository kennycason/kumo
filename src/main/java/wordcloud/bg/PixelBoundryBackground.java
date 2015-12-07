package wordcloud.bg;

import wordcloud.collide.Collidable;
import wordcloud.collide.Vector2d;
import wordcloud.image.CollisionRaster;

import javax.imageio.ImageIO;

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

    private final RectangleBackground rectangleBackground;

    /**
     * Creates a PixelBoundaryBackground using an InputStream to load an image
     * 
     * @param imageInputStream
     *            InputStream containing an image file
     * @throws IOException
     */
    public PixelBoundryBackground(final InputStream imageInputStream) throws IOException {
        final BufferedImage bufferedImage = ImageIO.read(imageInputStream);
        this.collisionRaster = new CollisionRaster(bufferedImage);
        this.rectangleBackground = new RectangleBackground(bufferedImage.getWidth(), bufferedImage.getHeight());
    }
    
    /**
     * Creates a PixelBoundaryBackground using an image from the input file
     * 
     * @param file
     *            a File pointing to an image
     * @throws IOException
     */
    public PixelBoundryBackground(final File file) throws IOException {
        this(new FileInputStream(file));
    }

    /**
     * Creates a PixelBoundaryBackground using an image-path
     * 
     * @param filepath
     *            path to an image file
     * @throws IOException
     */
    public PixelBoundryBackground(final String filepath) throws IOException {
        this(new File(filepath));
    }

    @Override
    public boolean isInBounds(Collidable collidable) {
        // check if bounding boxes intersect
        if(!this.rectangleBackground.isInBounds(collidable)) {
            return false;
        }
        final Vector2d position = collidable.getPosition();
        // get the overlapping box
        int startX = Math.max(position.getX(), 0);
        int endX = Math.min(position.getX() + collidable.getWidth(), collisionRaster.getWidth());

        int startY = Math.max(position.getY(), 0);
        int endY = Math.min(position.getY() + collidable.getHeight(), collisionRaster.getHeight());

        for(int y = startY ; y < endY ; y++) {
            for(int x = startX ; x < endX ; x++) {
                // compute offsets for surface
                if(collisionRaster.isTransparent(x - 0, y - 0) &&
                        !collidable.getCollisionRaster().isTransparent(x - position.getX(), y - position.getY())) {
                    return false;
                }
            }
        }
        return true;
    }

}
