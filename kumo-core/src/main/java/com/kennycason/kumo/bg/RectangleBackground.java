package com.kennycason.kumo.bg;

import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.abst.DimensionAbst;
import com.kennycason.kumo.abst.InstanceCreator;
import com.kennycason.kumo.abst.PointAbst;

/**
 * A Background Collision Mode in the shape of a rectangle
 * 
 * @author kenny, wolfposd
 * @version 2015.11.26
 */
public class RectangleBackground implements Background {
    private static final PointAbst ZERO = InstanceCreator.point(0, 0);

    private final PointAbst position;
    
    private final DimensionAbst dimension;

    /**
     * Creates a rectangle background starting at (0|0) with specified width/height
     * @param dimension dimension of background
     */
    public RectangleBackground(final DimensionAbst dimension) {
        this(ZERO, dimension);
    }

    /**
     * Creates a rectangle background using {@link PointAbst} and {@link DimensionAbst} for starting points and width/height
     * @param position the point where the rectangle lives on screen
     * @param dimension dimension of background
     */
    public RectangleBackground(final PointAbst position, final DimensionAbst dimension) {
        this.position = position;
        this.dimension = dimension;
    }

    @Override
    public boolean isInBounds(final Collidable collidable) {
        final PointAbst position = collidable.getPosition();
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
