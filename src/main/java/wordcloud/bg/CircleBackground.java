package wordcloud.bg;

import wordcloud.collide.Collidable;
import wordcloud.collide.Vector2d;

/**
 * Created by kenny on 6/30/14.
 */
public class CircleBackground implements Background {

    private final int radius;

    public CircleBackground(final int radius) {
        this.radius = radius;
    }

    @Override
    public boolean isInBounds(final Collidable collidable) {
        final Vector2d position = collidable.getPosition();
        return inCircle(position.getX(), position.getY())
                && inCircle(position.getX() + collidable.getWidth(), position.getY())
                && inCircle(position.getX(), position.getY() + collidable.getHeight())
                && inCircle(position.getX() + collidable.getWidth(), position.getY() + collidable.getHeight());
    }

    private boolean inCircle(final int x, final int y) {
        final int centerX = x - radius ;
        final int centerY = y - radius;
        return  (centerX * centerX) + (centerY * centerY) <= radius * radius;
    }

}
