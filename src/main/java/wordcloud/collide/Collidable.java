package wordcloud.collide;

import java.awt.image.BufferedImage;

/**
 * Created by kenny on 6/29/14.
 */
public interface Collidable {
    boolean collide(Collidable collidable);
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    BufferedImage getBufferedImage();
}
