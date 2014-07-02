package wordcloud.collide;

import java.awt.image.BufferedImage;

/**
 * Created by kenny on 6/29/14.
 */
public interface Collidable {
    boolean collide(Collidable collidable);
    Vector2d getPosition();
    int getWidth();
    int getHeight();
    BufferedImage getBufferedImage();
}
