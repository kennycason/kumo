package wordcloud.bg;

import wordcloud.collide.Collidable;
import wordcloud.collide.Vector2d;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kenny on 6/30/14.
 */
public class PixelBoundryBackground implements Background {

    private final BufferedImage bufferedImage;

    private final RectangleBackground rectangleBackground;

    public PixelBoundryBackground(final InputStream imageInputStream) throws IOException {
        this.bufferedImage = ImageIO.read(imageInputStream);
        this.rectangleBackground = new RectangleBackground(this.bufferedImage.getWidth(), this.bufferedImage.getHeight());
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
        int endX = Math.min(position.getX() + collidable.getWidth(), this.bufferedImage.getWidth());

        int startY = Math.max(position.getY(), 0);
        int endY = Math.min(position.getY() + collidable.getHeight(), this.bufferedImage.getHeight());

        for(int y = startY ; y < endY ; y++) {
            for(int x = startX ; x < endX ; x++) {
                // compute offsets for surface
                if(isTransparent(this.bufferedImage, x - 0, y - 0) &&
                        !isTransparent(collidable.getBufferedImage(), x - position.getX(), y - position.getY())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isTransparent(BufferedImage bufferedImage, int x, int y) {
        int pixel = bufferedImage.getRGB(x, y);
        if((pixel & 0xFF000000) == 0x00000000) {
            return true;
        }
        return false;
    }
}
