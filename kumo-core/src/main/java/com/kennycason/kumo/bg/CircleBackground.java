package com.kennycason.kumo.bg;

import com.kennycason.kumo.draw.Point;
import com.kennycason.kumo.collide.Collidable;

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
        final Point position = collidable.getPosition();
        return inCircle(position.getX(), position.getY())
                && inCircle(position.getX() + collidable.getDimension().getWidth(), position.getY())
                && inCircle(position.getX(), position.getY() + collidable.getDimension().getHeight())
                && inCircle(position.getX() + collidable.getDimension().getWidth(), position.getY() + collidable.getDimension().getHeight());
    }

    private boolean inCircle(final int x, final int y) {
        final int centerX = x - radius ;
        final int centerY = y - radius;
        return  (centerX * centerX) + (centerY * centerY) <= radius * radius;
    }

}
