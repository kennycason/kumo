package wordcloud.bg;

import wordcloud.collide.Collidable;

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
        return collidable.getX() >= 0 &&
                collidable.getX() + collidable.getWidth() < width &&
                collidable.getY() >= 0 &&
                collidable.getY() + collidable.getHeight() < height;
    }

}
