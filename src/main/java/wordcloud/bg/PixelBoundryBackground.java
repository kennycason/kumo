package wordcloud.bg;

import wordcloud.collide.Collidable;

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

        // get the overlapping box
        int inter_x0 = Math.max(collidable.getX(), 0);
        int inter_x1 = Math.min(collidable.getX() + collidable.getWidth(), this.bufferedImage.getWidth());

        int inter_y0 = Math.max(collidable.getY(), 0);
        int inter_y1 = Math.min(collidable.getY() + collidable.getHeight(), this.bufferedImage.getHeight());

        for(int y = inter_y0 ; y < inter_y1 ; y++) {
            for(int x = inter_x0 ; x < inter_x1 ; x++) {
                // compute offsets for surface
                if(isTransparent(this.bufferedImage, x - 0, y - 0) &&
                        !isTransparent(collidable.getBufferedImage(), x - collidable.getX(), y - collidable.getY())) {
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
