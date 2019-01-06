package com.kennycason.kumo.collide;

import com.kennycason.kumo.image.CollisionRaster;

import java.awt.Point;
import java.awt.Dimension;

/**
 * Created by kenny on 6/29/14.
 */
public interface Collidable {
    boolean collide(Collidable collidable);
    Point getPosition();
    Dimension getDimension();
    CollisionRaster getCollisionRaster();
}
