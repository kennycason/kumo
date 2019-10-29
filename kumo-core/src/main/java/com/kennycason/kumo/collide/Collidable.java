package com.kennycason.kumo.collide;

import com.kennycason.kumo.draw.Dimension;
import com.kennycason.kumo.draw.Point;
import com.kennycason.kumo.image.CollisionRaster;

/**
 * Created by kenny on 6/29/14.
 */
public interface Collidable {
    boolean collide(Collidable collidable);
    Point getPosition();
    Dimension getDimension();
    CollisionRaster getCollisionRaster();
}
