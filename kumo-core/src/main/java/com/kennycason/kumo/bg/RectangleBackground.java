package com.kennycason.kumo.bg;

import com.kennycason.kumo.draw.Dimension;
import com.kennycason.kumo.draw.Point;
import com.kennycason.kumo.collide.Collidable;

/**
 * A Background Collision Mode in the shape of a rectangle
 * 
 * @author kenny, wolfposd
 * @version 2015.11.26
 */
public class RectangleBackground implements Background {
    private static final Point ZERO = new Point(0, 0);

    private final Point position;
    
    private final Dimension dimension;

    /**
     * Creates a rectangle background starting at (0|0) with specified width/height
     * @param dimension dimension of background
     */
    public RectangleBackground(final Dimension dimension) {
        this(ZERO, dimension);
    }

    /**
     * Creates a rectangle background using {@link Point} and {@link Dimension} for starting points and width/height
     * @param position the point where the rectangle lives on screen
     * @param dimension dimension of background
     */
    public RectangleBackground(final Point position, final Dimension dimension) {
        this.position = position;
        this.dimension = dimension;
    }

    @Override
    public boolean isInBounds(final Collidable collidable) {
        final Point position = collidable.getPosition();
        return position.getX() >= this.position.getX()
               && position.getX() + collidable.getDimension().getWidth() < (this.position.getX() + dimension.getWidth())
               && position.getY() >= this.position.getY()
               && position.getY() + collidable.getDimension().getHeight() < (this.position.getY() + dimension.getHeight());
    }

    @Override
    public String toString() {
        return "RectangleBackground [x=" + position.getX() + ", y=" + position.getY() +
                ", width=" + dimension.getWidth() + ", height=" + dimension.getHeight() + "]";
    }

}
