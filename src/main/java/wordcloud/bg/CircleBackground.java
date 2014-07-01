package wordcloud.bg;

import wordcloud.collide.Collidable;

/**
 * Created by kenny on 6/30/14.
 */
public class CircleBackground implements Background {

    private final int radius;

    public CircleBackground(int radius) {
        this.radius = radius;
    }

    @Override
    public boolean isInBounds(Collidable collidable) {
        return inCircle(collidable.getX(), collidable.getY()) &&
                inCircle(collidable.getX() + collidable.getWidth(), collidable.getY()) &&
                inCircle(collidable.getX(), collidable.getY() + collidable.getHeight()) &&
                inCircle(collidable.getX() + collidable.getWidth(), collidable.getY() + collidable.getHeight());
    }

    private boolean inCircle(int x, int y) {
        x -= radius ;
        y -= radius;
        return  x * x + y * y <= radius * radius;
    }

}
