package wordcloud.bg;

import wordcloud.collide.Collidable;
import wordcloud.collide.Vector2d;

/**
 * Created by kenny on 6/30/14.
 */
public class RectangleBackground implements Background {

    private final int width;

    private final int height;

    public RectangleBackground(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isInBounds(Collidable collidable) {
        final Vector2d position = collidable.getPosition();
        return position.getX() >= 0 &&
                position.getX() + collidable.getWidth() < width &&
                position.getY() >= 0 &&
                position.getY() + collidable.getHeight() < height;
    }

}
