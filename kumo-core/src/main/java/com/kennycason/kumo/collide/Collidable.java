package com.kennycason.kumo.collide;

import com.kennycason.kumo.image.CollisionRaster;
import com.kennycason.kumo.interfaces.DimensionAbst;
import com.kennycason.kumo.interfaces.PointAbst;

/**
 * Created by kenny on 6/29/14.
 */
public interface Collidable {
    boolean collide(Collidable collidable);
    PointAbst getPosition();
    DimensionAbst getDimension();
    CollisionRaster getCollisionRaster();
}
