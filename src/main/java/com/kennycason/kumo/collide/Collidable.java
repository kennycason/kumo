package com.kennycason.kumo.collide;

import com.kennycason.kumo.image.CollisionRaster;

/**
 * Created by kenny on 6/29/14.
 */
public interface Collidable {
    boolean collide(Collidable collidable);
    Vector2d getPosition();
    int getWidth();
    int getHeight();
    CollisionRaster getCollisionRaster();
}
