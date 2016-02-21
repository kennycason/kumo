package com.kennycason.kumo.bg;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.collide.Vector2d;

/**
 * A Background Collision Mode in the shape of a rectangle
 * 
 * @author kenny, wolfposd
 * @version 2015.11.26
 */
public class RectangleBackground implements Background {

    private final int x;
    
    private final int y;
    
    private final int width;

    private final int height;

    /**
     * Creates a rectangle background starting at (0|0) with specified width/height
     * @param width
     * @param height
     */
    public RectangleBackground(int width, int height) {
        this(0, 0, width, height);
    }
    
    /**
     * Creates a rectangle background with specified starting points and width/height
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public RectangleBackground(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a rectangle background using {@link Point} and {@link Dimension} for starting points and width/height
     * @param startingPoint
     * @param dimensions
     */
    public RectangleBackground(Point startingPoint, Dimension dimensions) {
        this(startingPoint.x, startingPoint.y, dimensions.width, dimensions.height);
    }
    
    /**
     * Creates a rectangle background using {@link Rectangle} for starting points and width/height
     * @param rectangle
     */
    public RectangleBackground(Rectangle rectangle) {
        this(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    @Override
    public boolean isInBounds(Collidable collidable) {
        final Vector2d position = collidable.getPosition();
        return position.getX() >= x && 
               position.getX() + collidable.getWidth() < (x + width)  && 
               position.getY() >= y && 
               position.getY() + collidable.getHeight() < (y + height);
    }

    @Override
    public String toString() {
        return "RectangleBackground [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
    }

}
